package com.example.demo.src.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.src.feed.entitiy.FeedContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //파일변환
    public List<FeedContent> uploadFiles(List<MultipartFile> multipartFileList, String dirName) throws IOException {
        List<File> files = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFileList){
            if(multipartFile.getSize() < 10 * 1024 * 1024) { //파일의 크기가 10MB이하일때만 가능
                File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                        .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
                files.add(uploadFile);
            }
        }
        return upload(files, dirName);
    }

    public List<FeedContent> upload(List<File> uploadFile, String filePath) {
        List<FeedContent> fileList = new ArrayList<>();

        for(File a : uploadFile){
            String fileName = filePath + "/" + UUID.randomUUID() + a.getName();   // S3에 저장된 파일 이름
            String uploadImageUrl = putS3(a, fileName); // s3로 업로드
            removeNewFile(a);
            log.info("uploadFile = {}", a.getName());
            log.info("uploadImageUrl = {}", uploadImageUrl);

            FeedContent feedContent = new FeedContent(a.getName(),uploadImageUrl,a.length());
            fileList.add(feedContent);
        }
        return fileList;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info("로컬에 파일 업로드");
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if(convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 로컬에 저장된 이미지 지우기 (로컬에 이미지 저장 후 바로 삭제함)
    private void removeNewFile(File targetFile) {
        log.info("로컬에 이미지 삭제");
        if (targetFile.delete()) {
            log.info("로컬 이미지 삭제 성공");
            return;
        }
        log.info("로컬 이미지 삭제 실패");
    }


}

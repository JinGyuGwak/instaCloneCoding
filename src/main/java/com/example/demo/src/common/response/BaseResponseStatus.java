package com.example.demo.src.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */

    USERS_EMPTY_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일을 입력해주세요."),
    TEST_EMPTY_COMMENT(false, HttpStatus.BAD_REQUEST.value(), "코멘트를 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,HttpStatus.BAD_REQUEST.value(),"중복된 이메일입니다."),
    POST_TEST_EXISTS_MEMO(false,HttpStatus.BAD_REQUEST.value(),"중복된 메모입니다."),

    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    DUPLICATED_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    INVALID_MEMO(false,HttpStatus.NOT_FOUND.value(), "존재하지 않는 메모입니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 아이디거나 비밀번호가 틀렸습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    INVALID_OAUTH_TYPE(false, HttpStatus.BAD_REQUEST.value(), "알 수 없는 소셜 로그인 형식입니다."),

    INVALID_USER_INACTIVE(false, HttpStatus.BAD_REQUEST.value(), "비활성화 계정입니다."),
    INVALID_USER_SLEEP(false, HttpStatus.BAD_REQUEST.value(), "휴면 계정입니다."),
    INVALID_USER_DENIED(false, HttpStatus.BAD_REQUEST.value(), "차단 된 계정입니다."),


    NOT_FIND_FEED(false,HttpStatus.NOT_FOUND.value(),"게시글이 존재하지 않습니다."),
    INVALID_UPDATE_FEED(false, HttpStatus.BAD_REQUEST.value(), "해당 게시글이 존재하지 않습니다."),

    NOT_FIND_COMMENT(false,HttpStatus.NOT_FOUND.value(),"댓글이 존재하지 않습니다."),


    OVER_COMMENT(false,HttpStatus.NOT_FOUND.value(),"최대 글자수를 넘어갔습니다."),
    LEAST_COMMENT(false,HttpStatus.NOT_FOUND.value(),"글자가 입력되지 않았습니다."),

    OVER_IMAGE(false,HttpStatus.NOT_FOUND.value(),"이미지/영상은 최대 10개까지 가능합니다."),
    LEAST_IMAGE(false,HttpStatus.NOT_FOUND.value(),"최소 1개의 이미지를 올려야 합니다."),

    INVALID_MYPAGE(false,HttpStatus.FORBIDDEN.value(),"이미 존재하는 유저입니다."),

    NOT_FIND_CHATROOM(false,HttpStatus.NOT_FOUND.value(),"채티방이 존재하지 않습니다."),
    INVALID_CHATROOM(false,HttpStatus.NOT_FOUND.value(),"이미 존재하는 채팅방입니다."),
    NOT_FIND_MESSAGE(false,HttpStatus.NOT_FOUND.value(),"메시지가 존재하지 않습니다."),


    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),


    MODIFY_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    DELETE_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저 삭제 실패"),
    MODIFY_FAIL_MEMO(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"메모 수정 실패"),

    VALID_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "올바른 형식으로 입력해주세요."),
    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

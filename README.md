# Instagram Clone

## 🚀프로젝트 목적

백엔드 개발자가 되기 위해 학습을 하였지만 아직 실력이 부족하다고 느끼고 있었고 배운 내용을 다시 복습하는 것 보다는 실제로 무언가를 만들어 보는게 저의 실력향상에 더 도움이 될 것이라 생각하여 이번 프로젝트를 진행하게 되었습니다.

어느정도 구현을 하고 보니 중복되는 코드가 많고 아키텍처 구조가 굉장히 난잡하여 이를 리팩토링 하며 유지관리에 용이한 코드와 깨끗한 아키텍쳐에 대해 고민을 하게 되었습니다.

이번 프로젝트를 통해 ERD 설계부터 EC2를 이용한 배포까지 쥬니어 백엔드 개발자가 되기위해 필요한 최소한의 능력을 갖출 수 있게 되었습니다.

<br>

# 사용 기술

### 개발환경

- IDE : intelliJ IDEA
- JDK : java 8
- Project : Gradle
- SpringBoot 2.7.5
- AWS EC2
- AWS S3

### Dependency

- Spring Web
- Spring Data JPA
- JWT
- Lombok
- MySQL

<br>

# ER Diagram

<img width="100%" src="https://user-images.githubusercontent.com/104514223/226169023-00a08a7d-c331-4915-b099-d9325e42de72.png">

<br>
<br>

# 서비스 기능

https://github.com/JinGyuGwak/instaCloneCoding/wiki/%EC%97%94%EB%93%9C%ED%8F%AC%EC%9D%B8%ED%8A%B8

<br>
<br>

# 트러블 슈팅

## 1.EC2 CPU 사용률 100%로 인한 서버 다운

프리티어 환경에서 서버가 주기적으로 죽는 문제가 있어 이에 대한 트러블 슈팅 과정을 블로그에 정리하였습니다.

https://singsinggyu.tistory.com/2#comment13423223

<br>

## 2.DTO가 많아지는 문제

클라이언트에게 응답을 내려줄 때 필요한 정보만 줄 수 있도록 구현했습니다.

하지만 그 과정에서 엔드포인트가 늘어날수록 DTO가 많아져 관리하기 힘들어지고 네이밍 문제도 생기게 되었습니다.

이를 해결하기 위해 하나의 엔드포인트에서만 사용하거나 POST,UPDATE 처럼 필드가 겹치는 RequestDTO는 Inner Class로 처리하게 되었고 Request와 Response 패키지를 분리함으로써 관리를 수월하게 했습니다.

<br>

## 3.코드의 중복 제거와 스트림 사용

처음 계획했던 대로 구현을 한 후 코드를 살펴보니 신경쓰이는 부분이 있었습니다.

    User user = userRepository.findByIdAndState(userId,ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));

위의 코드는 userId를 통해 user를 조회하고 만약 DB에 값이 없는 경우 예외처리를 하는 메서드입니다.
서비스 특성상 User를 조회해야 하는 경우가 많은데 이 때마다 같은 코드를 계속 쓰는 건 비효율적이라고 생각하게 되었고 아래 처럼 FuncUser라는 클래스를 만들었습니다.

    public class FuncUser {
        private final UserRepository userRepository;

        public User findUserByIdAndState(Long userId){
            return userRepository.findByIdAndState(userId,ACTIVE)
                    .orElseThrow(()->new BaseException(NOT_FIND_USER));
        }
    }

그 후 기존에 사용하던 코드를 아래와 같이 줄일 수 있었습니다.

    User user = funcUser.findUserByIdAndState(userId);

<br>

## 4.프로젝트 아키텍처 수정

기존 아키텍처 구조는 레이어드 아키텍쳐 기반에 DDD의 흉내를 낸 이도저도 아닌 구조였습니다. 코드를 리팩토링 하는 과정에서 이 구조는 상당히 지저분하다고 느끼게 되었고 프로젝트를 처음 보는 사람은 구조 파악이 힘들것이라 생각하게 되어 어설프게 적용된 DDD를 버렸습니다.
<img width="100%" src="https://user-images.githubusercontent.com/104514223/226563923-6d2fed22-7793-4f9c-ac09-d614f8326575.png">

<br>

## 5.신고 누적 피드 블라인드 처리시 예상되는 문제

FEED의 신고 횟수가 5회가 넘어가면 유저들이 볼 수 없게 블라인드 처리를 하는 기능을 구현하였습니다. 이를 구현하기 위해 FEEDREPORT 테이블을 만들어 신고한 유저의 id와 신고된 FEED의 id 를 저장하였고 FEED를 조회할 때 FEEDREPORT의 외래키인 FEED의 id를 카운트하여 만약 5가 넘어갈 경우에는 반환하지 않도록 했습니다.

하지만 이렇게 되면 FEED를 전체 조회 할 때 FEED마다 연관된 FEEDREPORT 테이블을 where 절을 이용하여 조회하게 됩니다. 데이터가 적은 경우에는 성능에 큰 문제는 없겠지만 FEED와 FEEDREPORT의 수가 많아지면 문제가 생길 것으로 예상하여 FEED 엔티티에 누적신고횟수(reporCount) 필드를 추가하여 FEEDREPORT를 조회할 필요 없이 블라인드 처리를 할 수 있게 수정하였습니다.

<br>

수정 전 코드) Feed와 연관 된 FeedReport를 조회합니다.

    return feedRepository.findAllByState(ACTIVE, pageable)
        .stream()
        .filter(feed -> feed.getFeedReportList().size() < 6)
        .map(GetFeedRes::new)
        .collect(Collectors.toList());

수정 후 코드) Feed만 조회하고 FeedReport를 조회하지 않습니다.

    return feedRepository.findAllByState(ACTIVE, pageable)
        .stream()
        .filter(feed -> feed.getReportCount() < 6)
        .map(GetFeedRes::new)
        .collect(Collectors.toList());

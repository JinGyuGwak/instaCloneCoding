# Instagram Clone

## 🚀프로젝트 목적
최소한의 구현능력을 학습한 이후 개발 능력을 향상시키기 위한 최고의 방법은 직접 무언가를 만드는 것이라고 생각하였습니다.

처음 이 프로젝트를 시작했을 때에는 JPA를 이용하여로 단순한 CRUD 만을 구현하였지만 이후에 지속적으로 학습을 하며 Security적용, AWS와 Dokcer를 이용한 배포, 단위테스트와 통합테스트 코드를 작성하여 지금의 모습으로 발젼시켰으며, Jenkins를 이용한 CI/CD 파이프라인도 구축하였습니다.

여기서 끝내지 않고 이후에도 학습한 내용들을 프로젝트에 적용시키며 발전시킬 예정입니다.

<br>

# 사용 기술

### 개발환경

- IDE : intelliJ IDEA
- JDK : java 8
- Build tool : Gradle
- SpringBoot 2.7.5
- MariaDB (AWS RDS)

### Infra
- AWS EC2
- AWS S3
- Github WebHook
- Docker
- Jenkins

### 주요 라이브러리

- Spring Data JPA
- Spring Security
- JWT
- Spring Rest Docs
- H2 (테스트 용도)

<br>

# ER Diagram

<img width="100%" src="https://user-images.githubusercontent.com/104514223/226169023-00a08a7d-c331-4915-b099-d9325e42de72.png">

<br>
<br>

# 시스템 아키텍쳐

<img width="100%" src="https://github.com/JinGyuGwak/instaCloneCoding/assets/104514223/2b48aaae-d9aa-4be5-82df-c38d2ba8c5a7">

<br>
<br>

# 기능 명세서 및 API 문서

기능명세서 :
https://github.com/JinGyuGwak/instaCloneCoding/wiki/%EC%97%94%EB%93%9C%ED%8F%AC%EC%9D%B8%ED%8A%B8

API 문서 :
http://43.201.123.163:8080/docs/index.html


<br>
<br>

# 주요 구현 내용
- JPA를 이용한 CRUD
- Spring Security 와 JWT를 이용한 인증, 인가
- Junit과 Mockito를 이용한 단위테스트, 통합테스트
- Spring Rest Docs를 이용한 API 문서
- Jenkins를 이용한 CI/CD 파이프라인 구축

<br>
<br>

# 트러블 슈팅

## 1.EC2 CPU 사용률 100%로 인한 서버 다운

프리티어 환경에서 서버가 주기적으로 죽는 문제가 있어 이에 대한 트러블 슈팅 과정을 블로그에 정리하였습니다.

https://singsinggyu.tistory.com/2#comment13423223

<br>

## 2. 단위테스트와 통합테스트
처음 테스트 코드를 작성할 때에는 @SpringBootTest 어노테이션을 이용한 통합테스트 코드를 작성하였습니다.

이 방식은 프로젝트의 모든 Bean과 Component 스캔하며, 모든 계층간의 상호작용을 포함합니다.

하지만 Controller 계층에 대한 테스트는 올바른 형식의 요청을 받고, 올바른 형식의 응답을 반환하는 것을 테스트 하는것이라고 생각하였고, @WebMvcTest를 이용하여 테스트 하려는 Controller의 Conponent만 Scan 하는 것이 효율적이라 판단하였습니다. 이를 위해 컨트롤러가 의존하고 있는 Service 계층을 Mocking 처리 하여 단위 테스트를 수행하였습니다.

반면, Service 계층은 실제 비지니스 로직을 처리하기 위해 DB와의 상호작용이 필수적입니다. 이 때문에 Service 계층은 단위테스트가 아닌 DB와의 연동을 포함한 통합 테스트를 진행하여 실제 비지니스 로직과 데이터베이스간의 상호작용을 검증하였습니다.

코드 링크 :
https://github.com/JinGyuGwak/instaCloneCoding/blob/master/src/test/java/com/example/demo/user/controller/UserControllerTest.java

<br>

## 3.DTO가 많아지는 문제

공부한 내용을 프로젝트에 적용 시키면서 처음 프로젝트를 개발 하였을 당시와는 다르게 Controller 코드와 Service 코드가 방대해져 이에 따른 DTO 클래스의 수도 많아지게 되었습니다.

필드값이 서로 같은 DTO 클래스들은 하나로 묶으려 했지만 추후 새로운 필드를 추가하게 될 경우 다시 클래스를 분리해야 하는 번거로움이 있을 뿐 아니라 하나로 묶는 과정에서 네이밍으로 인한 문제가 발생할 것이라 생각했습니다.

이러한 문제를 해결하기 위해 각각의 주요 기능별로 DTO 클래스를 만들고 static 클래스를 사용하여 관련 DTO들을 그룹화 했습니다. 이렇게 함으로써 기존에 30개가 넘었던 DTO의 class파일을 6개로 줄여 관리를 더 효율적으로 할 수 있게 만들었습니다.

코드 링크 :
https://github.com/JinGyuGwak/instaCloneCoding/blob/master/src/main/java/com/example/demo/src/feed/dto/FeedDto.java

<br>

## 4.코드의 중복 제거와 스트림 사용

처음 계획했던 대로 구현을 한 후 코드를 살펴보니 신경쓰이는 부분이 있었습니다.

    User user = userRepository.findByIdAndState(userId,ACTIVE)
                .orElseThrow(()->new IllegalException(NOT_FIND_USER));

위의 코드는 userId를 통해 user를 조회하고 만약 DB에 값이 없는 경우 예외처리를 하는 메서드입니다.
서비스 특성상 User를 조회해야 하는 경우가 많은데 이 때마다 같은 코드를 계속 쓰는 건 비효율적이라고 생각하게 되었고 아래 처럼 FuncUser라는 클래스를 만들었습니다.

    public class FuncUser {
        private final UserRepository userRepository;

        public User findUserByIdAndState(Long userId){
            return userRepository.findByIdAndState(userId,ACTIVE)
                    .orElseThrow(()->new IllegalException(NOT_FIND_USER));
        }
    }

그 후 기존에 사용하던 코드를 아래와 같이 줄일 수 있었습니다.

    User user = funcUser.findUserByIdAndState(userId);

<br>

## 5.프로젝트 아키텍처 수정

기존 아키텍처 구조는 Controller, Service, Entity 등 하나의 패키지에서 관리하였지만 기능을 추가 할수록 오히려 관리하기 힘들다는 느낌을 받았습니다.

이를 해결하기 위해 기존의 구조에서 기능별로 패키지를 나누어 관리하게 되었습니다.

<img width="100%" src="https://github.com/JinGyuGwak/instaCloneCoding/assets/134255553/480b4ff3-10a5-4c91-aad5-72fc2f7901a6">

<br>

## 6. Spring Rest Docs 와 Swagger

백엔드 개발자가 다른 사람들과 협업할 때 가장 중요한 문서 중 하나가 API 문서라고 생각합니다.

많은 사람들이 사용하고 있는 Swagger는 사용하기 쉽고 시각적으로 이해하기 쉽다는 장점이 있지만, 실제 동작하는 코드에 어노테이션을 추가해야 하는 작업이 필요 합니다. 이러한 작업은 비지니스 코드의 가독성을 해친다고 판단하여 Spring Rest Docs를 적용하였습니다.

Spring Rest Docs는 테스트 코드를 바탕으로 API 문서를 생성합니다. Swagger에 비해 사용하기 어렵다는 단점이 있지만 강제적으로 테스트 코드를 작성하게 함으로써 API 문서의 정확성을 보장하는 장점을 가지고 있습니다.

API 문서 링크 : 
http://43.201.123.163:8080/docs/index.html

<br>

## 7. Test를 위한 yml 설정

현재 배포 중인 프로젝트의 DB는 AWS RDS를 이용하고 있지만 개발환경의 DB는 Docker 를 이용하여 MariaDB 컨테이너를 사용하고 있습니다.

이 때문에 서비스 계층에서 DB와의 연동을 검증하는 테스트 코드, 예를 들어 사용자 회원 가입 기능을 테스트하는 경우, 로컬 개발 환경에서 매번 Docker 컨테이너를 실행시켜야 합니다. 하지만 테스트 코드는 어떠한 환경에서도 일관되게 동작하며 올바른 로직을 검증할 수 있어야 합니다.

이러한 문제를 해결하기 위해 'application-test.yml' 설정 파일을 별도로 생성하여 테스트 실행 시 H2 인메모리 DB를 사용하도록 설정함으로써, DB를 따로 실행하지 않아도 해당 테스트를 수행할 수 있게 되었습니다.


    server:
        port: 8080

    spring:
        datasource:
            url: jdbc:h2:mem:Hub;MODE=MYSQL;NON_KEYWORDS=USER
            username: sa
            password:
            driver-class-name: org.h2.Driver
        jpa:
            hibernate:
                ddl-auto: create
            properties:
                hibernate:
                    format_sql: true
            generate-ddl: true
            show-sql: true
            defer-datasource-initialization: true
        h2:
            console:
                enabled: true
            path: /h2-console
            settings:
                trace: false
                web-allow-others: false
        
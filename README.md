# Instagram Clone

## 🚀프로젝트 목적

백엔드 개발자가 되기 위해 학습을 하였지만 아직 실력이 부족하다고 느끼고 있었고 배운 내용을 다시 복습하는 것 보다는 실제로 무언가를 만들어 보는게 저의 실력향상에 더 도움이 될 것이라 생각하여 이번 프로젝트를 진행하게 되었습니다.

어느정도 구현을 하고 보니 중복되는 코드가 많고 아키텍처 구조가 굉장히 난잡하여 이를 리팩토링 하며 유지관리에 용이한 코드와 깨끗한 아키텍쳐에 대해 고민을 하게 되었습니다.

이번 프로젝트를 통해 ERD 설계부터 EC2를 이용한 배포까지 쥬니어 백엔드 개발자가 되기위해 필요한 최소한의 능력을 갖출 수 있게 되었습니다.

새로운 기술을 배울 때 저에게 가장 효과적인 방법은 직접 그 기술을 사용하는 것이라고 느꼈습니다.

처음 이 프로젝트를 시작했을 때에는 게시판 만들기로 단순한 CRUD 만을 구현하였지만 이후에 지속적으로 학습을 하며 배운 기술들을 프로젝트에 적용시켜 

<br>

# 사용 기술

### 개발환경

- IDE : intelliJ IDEA
- JDK : java 8
- Project : Gradle
- SpringBoot 2.7.5
- AWS EC2
- AWS S3
- MariaDB (RDS)

### Dependency

- Spring Web
- Spring Data JPA
- JWT
- Lombok
- MariaDB

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

하지만 그 과정에서 엔드포인트가 늘어날수록 DTO 클래스가 많아져 관리하기 힘들다는 문제가 생겼습니다.

이를 해결하기 위해 각각의 주요 기능별로 DTO 클래스를 하나씩 만들어서 extends 와 static 클래스를 이용하여 기존에 30개가 넘는 클래스를 6개로 줄였습니다.



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

기존 아키텍처 구조는 Controller, Service, Entity 등 하나의 패키지에서 관리하였지만 기능을 추가 할수록 오히려 관리하기 힘들다는 느낌을 받았습니다.

이를 해결하기 위해 기존의 구조에서 기능별로 패키지를 나누어 관리하게 되었습니다.

<img width="100%" src="https://github.com/JinGyuGwak/instaCloneCoding/assets/134255553/480b4ff3-10a5-4c91-aad5-72fc2f7901a6">

<br>
<br>

# 현재 진행 및 예정 작업

## 5. Spring Rest Docs 와 Swagger

백엔드 개발자가 다른 사람들과 협업할 때 가장 중요한 문서 중 하나가 API 문서라고 생각합니다.

하지만 많은 사람들이 사용하고 있는 Swagger 은 실제 동작하는 코드에 추가로 코드를 작성하는 방식으로 비지니스 코드의 가독성을 떨어트린다고 생각하였습니다.

Spring Rest Docs는 Swagger 와는 다르게 API문서를 Controller 테스트 코드에 작성함으로 Swagger의 단점을 보완함과 동시에 강제적으로 Test 코드를 작성해야 한다는 단점처럼 보이는 장점을 가지고 있습니다. 

## 6. Service 코드 단위테스트 작성

## 7. Jenkins를 이용한 CI/CD 구축

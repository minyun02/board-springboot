# Spring Boot로 만든 커뮤니티 게시판

## **프로젝트 소개**

- 전 회사에서는 자체 프레임워크로 LMS 서비스를 개발하면서 Spring Boot와 조금 멀어진다는 생각이 들어 커뮤니티 게시판을 만들었습니다.
- CRUD 기능과 본문을 읽어서 해시태그로 전환하는 기능을 가지고 있습니다.
- 글에 댓글이나 좋아요가 눌리면 실시간으로 글 작성자에게 알람이 가는 기능이 있습니다.

---

## **사용 방법**

### 1. 로그인

- Username과 Password를 사용한 로그인을 제공합니다.
- 카카오 로그인 기능도 제공합니다.

![로그인화면](https://github.com/minyun02/board-springboot/assets/69966611/a7eb165f-b69a-4d80-8267-f2293c7ba36e)

### 2. 홈 (글 목록)

- 로그인 성공 시 글 목록 화면으로 이동합니다.
- 제목, 본문 내용, 해시 태그로 검색이 가능합니다.

![글목록화면](https://github.com/minyun02/board-springboot/assets/69966611/4910f485-9fcf-4ba1-a1b2-54576b5016bb)

### 3. 글 상세보기

- 상세 페이지에서 원하는 글을 볼 수 있습니다.
- 본문에 #로 시작하는 문자는 해시태그로 인식됩니다.
- 해당 글에 좋아요 버튼을 누를 수 있습니다. ‘좋아요’를 하면 작성자에게 알람이 전송됩니다.
- 해당 글에 댓글과 대댓글을 작성할 수 있습니다. 댓글이 등록되면 작성자에게 알람이 전송됩니다.

![글상세보기화면](https://github.com/minyun02/board-springboot/assets/69966611/fb6aae50-a625-4c94-ad4f-0a904dc89647)

### 4. 해시태그 목록

- 등록한 모든 해시태그를 볼 수 있습니다.
- 해시태그를 클릭하면 해당 해시태그를 가진 글 목록이 조회됩니다.

![해시태그목록화면](https://github.com/minyun02/board-springboot/assets/69966611/3d4e719a-750a-4ad7-b255-bb2b9c0880c3)

### 5. 내 글 목록

- 사용자가 작성한 게시글과 댓글을 볼 수 있습니다.

![내글목록화면](https://github.com/minyun02/board-springboot/assets/69966611/ca7dc86c-2cbf-4026-83e4-bd3b9cb502b1)

### 6. 알림 기능

- 글에 누군가 ‘좋아요’를 누르거나 댓글이 등록된 경우에 작성자에게 알림이 전송됩니다.
- Notifications라는 메뉴 우측 배지로 읽지 않은 알림 개수를 확인할 수 있습니다..
- 비동기식 방식으로 알림을 실시간으로 받을 수 있습니다.
- 알림 메뉴 안에서 댓글은 파란색, 좋아요는 빨간색 그리고 읽은 알람은 회색 처리했습니다.

![알림기능화면](https://github.com/minyun02/board-springboot/assets/69966611/fd56beea-28c2-48f4-b127-3c849f249659)

---

## **SKILL SET**

> 사용한 기술 스택은 아래와 같습니다.
> 
- Thymeleaf (template engine)
- Spring Boot (API Server)
- Spring Security (Security)
- Mysql (RDB)
- JPA & QueryDSL (ORM)
- OAuth2.0 (Login)
- SSE, Kafka (Alarm)
- AWS (Infra - EC2, S3, RDS)
- Nginx (Reverse Proxy Server)
- Github Action and CodeDeploy (CI/CD)

---

## **Spring Boot (API Server)**

> 버전
> 
- Spring Boot 3.1.3
- Java 17

> 디렉터리 구조
> 
- configuration
- controller
- domain
    - constant
    - entity
- dto
    - request
    - response
    - security
- exception
- repository
    - querydsl
- service

---

## **Spring Security (Security)**

- 로그인 시 username으로 사용자 정보를 확인하는 방식으로 구현했습니다.
- 인가된 사용자만 특정 API에 접근할 수 있습니다.
- 정적 파일 접근과 홈 화면, 로그인, 회원 가입에 대한 접근은 허용했습니다.
- 별도의 로그인 화면을 사용하고 모든 로그인 성공은 홈 화면으로 이동합니다.
- UserDetailsService로 username을 받아오고 재정의한 Principal에 정보를 담아서 넘겨줍니다.

---

## **JPA & QueryDSL (ORM)**

- Entity에는 @SQLDelete로 Soft Delete를 구현했습니다. 따라서, 데이터 조회 시에 추가되어야 하는 조건 처리를 했습니다.
- JPA : 반복적인 CRUD 작업을 대체해 데이터를 조회합니다.
- QueryDSL : Join 같은 JPA로 구현하기 다소 힘든 퀴리를 담당합니다.

> 구조는 아래와 같습니다.
> 
- PostEntity (Entity Class)
- PostEntityRepository (JPA Interface)
- PostRepositoryCustom (QueryDSL Interface)
- PostRepositoryCustomImpl (QueryDSL Implementation Class)

---

## **OAuth2.0 (Login)**

- OAuth provider(카카오)를 통해 간단하면서 더 나은 보안을 가진 회원가입, 로그인 기능을 제공했습니다.
- 인증 코드와 토큰 발급 과정을 거쳐서 카카오 사용자 정보를 받아옵니다.
- 받아온 사용자 정보의 username으로 DB 조회를 거쳐 중복 확인을 합니다.
- 동일한 사용자가 있는 경우에는 로그인, 없는 경우에는 등록을 시킵니다.
- 카카오 로그인 흐름

![oauthkakaologin](https://github.com/minyun02/board-springboot/assets/69966611/24416429-2526-466a-aac9-06f82ea7018d)

---

## **Kafka (Alarm)**

- 새로운 댓글이 등록되거나 누군가 좋아요를 누르면 해당 글의 작성자에게 알림이 전달되는 기능입니다.
- Server-Sent Events를 통해서 알림 기능을 실시간 처리했습니다.
- 알림의 발생과 소비는 kafka로 비동기식으로 구현했습니다.
- Kafka와 SSE를 활용한 알람 기능 흐름

![kafka](https://github.com/minyun02/board-springboot/assets/69966611/e31f40ad-207a-4cc9-9dd4-f4dd68254630)

---

## **Github Action and CodeDeploy (CI/DC)**

- Github Actions와 CodeDeploy를 사용해서 CI/CD를 구축했습니다.
- 로드 밸런서를 사용하지 않고 배포 스크립트를 통해 port를 변경하는 방식으로 무중단 배포를 구현했습니다.
- 무중단 배포 전체 구조

![cicd](https://github.com/minyun02/board-springboot/assets/69966611/140eaad2-907b-4417-8c17-495fb02b7f48)

---

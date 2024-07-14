# 📖 데브툰 백엔드 애플리케이션

<div align='center'>

  <kbd><img src = "https://github.com/user-attachments/assets/e5287461-7551-4a25-939f-1440522865d9"></kbd>

  <h3>개발자 유머짤 공유 플랫폼 구축 프로젝트 입니다.</h3>
  
  <a href='https://topaz-raincoat-203.notion.site/79f0935cded3430385c781e0f1e8fe9c?pvs=74'>📒 팀 노션</a> | 
  <a href='https://github.com/FreakPeople/freak-devtoon-front'>🎨 프론트엔드 레포지토리</a> |
  <a href='https://github.com/FreakPeople/freak-devtoon-back/pulls?page=3&q=is%3Apr+is%3Aclosed'>🎨 이슈 & PR 관리</a>
</div>

<br><br>

## 1. 기술 스택
- 백엔드
  - Java 21
  - SpringBoot 3.2.5
  - Spring Data JPA
  - Spring Security
  - JUnit5
  - Gradle
  - MySQL8.0
  - Docker
  - GitHub Actions
- 프론트
  - React

<br><br>

## 2. 프로젝트 구조

### 1. 아키텍처
<kbd><img src = "https://github.com/user-attachments/assets/6f86e761-48d1-4683-bead-c2571dd4af1b" style="border-radius: 10px;"></kbd>


```
기본경로 : main ──> java ──> yjh ──> devtoon

main...
├── devtoon
│    ├── auth
│    ├── bad_words_warning_count
│    ├── comment
│    ├── common
│    ├── cookie_wallet
│    ├── member
│         ├── application
│         ├── constant
│         ├── docs
│         ├── domain
│         ├── dto
│              ├── request
│              ├── response
│         ├── infrastructure
│         ├── presentation
│    ├── payment
│    ├── policy
│    ├── promotion
│    ├── webtoon
│    │      
│    ├── DeliveryApplication.java
│
test...
├── devtoon
     ├── auth
     ├── bad_words_warning_count
     ├── comment
     ├── cookie_wallet
     ├── member
     ├── payment
     ├── policy
     ├── promotion
     ├── webtoon
```
### main
- auth : 인증 도메인을 구현한다.
- bad_words_warning_count : 사용자 비속어 카운트 도메인을 구현한다.
- comment : 댓글 도메인을 구현한다.
- common : 모든 도메인에서 공통적으로 사용하는 기능(설정파일, Entity 공통필드, 예외처리)을 포함한다.
- cookie_wallet : 회원의 쿠키 지갑 도메인을 구현한다.
- member : 회원 도메인을 구현한다.
- payment : 결제 도메인을 구현한다.
- policy : 정책 도메인을 구현한다.
- promotion : 행사 도메인을 구현한다.
- webtoon : 웹툰 도메인을 구현한다.

### test
- 도메인별로 테스트가 정의되어 있다.
  - integration : 데이터베이스와 연동된 통합 테스트 작성.
  - domain : 도메인별 핵심 비즈니스 로직 단위 테스트 작성.

<br><br>

## 3. 개발 환경 구축
- back-end 와 front-end 서버를 로컬환경에서 실행시키고 테스트할 수 있습니다.
- 아래의 단계에 따라 로컬환경에서 순차적으로 실행하면 됩니다.
- docker를 컨테이너로 애플리케이션을 실행시키기 때문에 docker가 설치되어 있어야 합니다.

### 1. 프로젝트 클론
```
git clone https://github.com/FreakPeople/freak-devtoon-back.git
```

### 2. 도커 컴포즈 명령어 실행
- 터미널의 프로젝트 최상위 디렉토리에서 아래의 명령어를 실행합니다.
```
docker-compose up -d
```

### 3. 테스트 실행
- mac os 환경
```
./gradlew clean test
```
- window 환경
```
gradlew clean test
```

<br><br>

## 4. ERD 다이어그램
<img width="928" alt="erd" src="https://github.com/user-attachments/assets/c96d56e7-7ab6-4d67-922e-4734e82c5d3b" style="border-radius: 10px;">

<br><br>

## 5. 시퀀스 다이어그램
<div>
  <kbd>
    <img src="https://github.com/user-attachments/assets/93a3891d-8a3e-4804-976a-a6e587b9e66d" width="32%">
    <img src="https://github.com/user-attachments/assets/8f673036-5e57-4ad3-9d6c-cce4a23638a2" width="32%">
    <img src="https://github.com/user-attachments/assets/6177da44-a3a9-42b6-b8a5-12a512b24c9b" width="32%">
    <img src="https://github.com/user-attachments/assets/fc5a5cf7-95c5-4f21-9aa0-1bee8b29e5f7" width="32%">
    <img src="https://github.com/user-attachments/assets/d9a201e6-4b73-4b4b-b29f-e96b26261a5f" width="32%">
    <img src="https://github.com/user-attachments/assets/ffea7fbd-e884-45ea-aab4-e6640b4f627c" width="32%">
    <img src="https://github.com/user-attachments/assets/b61ada94-1972-4881-af65-af029dfc172c" width="32%">
    <img src="https://github.com/user-attachments/assets/989b8372-41ba-4dac-8ac2-8edb0f30cd00" width="32%">
    <img src="https://github.com/user-attachments/assets/e4b0677a-7092-41fd-b5cb-ea342e8ccd79" width="32%">
  </kbd>
</div>

<br><br>

## 6. API 명세서

### 인증 API
<details>
<summary>로그인</summary>

`POST /v1/auth/authenticate`
```가나다라
Request
{
  "email" : "string",
  "password" : "string": 
}

```

```
Response / 200 OK
{
  "statusMessage" : "string",
  "data" : {}
}
```
---
</details>

### 회원 API
<details>
<summary>로그인</summary>

`POST /v1/auth/authenticate`

```
Request
{
  "email" : "string",
  "password" : "string": 
}

```

```
Response / 200 OK
{
  "statusMessage" : "string",
  "data" : {}
}
```
---
</details>

### 웹툰 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 댓글 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 쿠키 지갑 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 비속어 경고 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 웹툰 결제 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 쿠키 결제 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 프로모션 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

### 정책 API
<details>
<summary>쿠키 증가</summary>

여기가 자세한 내용?

</details>

<br><br>

## 7. 화면 구성
|![회원가입](https://github.com/user-attachments/assets/37aeea37-3959-463f-b64f-743537c15658)|![웹툰조회](https://github.com/user-attachments/assets/8958f9ed-2f56-45d2-b6c8-c01d8662062b)|  
| :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: |
|                                       회원가입                                             |                                         웹툰조회                                           |

|![웹툰등록](https://github.com/user-attachments/assets/867c97bb-c382-4160-aee1-e394cd6236f0)|![웹툰등록완료](https://github.com/user-attachments/assets/693ad5da-7db7-4499-ac40-1bbfb9c09f69)|
| :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: |
|                                       웹툰등록                                             |                                        웹툰등록완료                                         |

|![댓글조회](https://github.com/user-attachments/assets/0395e25f-c754-4f4b-96a1-76cde47c09b4)|![댓글등록](https://github.com/user-attachments/assets/89bdc754-5804-4a22-9803-3835efdce28a)|
| :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: |
|                                       댓글조회                                             |                                         댓글등록                                           |

|![정책 및 프로모션 조회](https://github.com/user-attachments/assets/8d0c45e5-9cb6-44e7-93b9-196e139679f5)|![내정보조회](https://github.com/user-attachments/assets/c05d4a70-9451-42ac-9b11-de2d62f61042)|
| :--------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: |
|                                   정책 및 프로모션 조회                                       |                                        내정보조회                                          |

<br><br>

## 8. 스터디
<a href='https://lealea.tistory.com/327'>✏️ 프로젝트 기획부터 설계까지</a>  
<a href='https://lealea.tistory.com/328'>✏️ Git 활용하여 자신있게 프로젝트 협업하기</a>  
<a href='https://lealea.tistory.com/332'>✏️ 다양한 정책을 쉽게 등록하고 삭제하기</a>  
<a href='https://lealea.tistory.com/333'>✏️ 이제 너만 믿는다, 테스트 코드 작성하기</a>  
<a href='https://lealea.tistory.com/336'>✏️ 리팩토링 모음.zip</a>  
<a href='https://lealea.tistory.com/337'>✏️ 리팩토링: 프로모션 조회 설계 개선 및 성능 최적화 도전하기 - 설계 편</a>  
<a href='https://lealea.tistory.com/338'>✏️ 리팩토링: 프로모션 조회 설계 개선 및 성능 최적화 도전하기 - 성능 최적화 편</a>  
<a href='https://lealea.tistory.com/339'>✏️ 리팩토링: 쿠키 결제 로직 4단계로 개선하기 (feat. 원시값 포장)</a>  
<a href='https://wlgns2305.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%EB%8D%B0%EB%B8%8C%ED%88%B0'>✏️ 스프링 환경에서 비동기 프로그래밍 적용해보기</a>

<br><br>

## 9. 팀원
|                                   BackEnd                                    |                                    BackEnd                                    |
|:----------------------------------------------------------------------------:|:-----------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/78125105?v=4" width="100"> | <img src="https://avatars.githubusercontent.com/u/87998104?v=4" width="100"> |
|                         [황유정](https://github.com/youjungHwang)                          |                      [정지훈](https://github.com/Jeongjjuna)                      |

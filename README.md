# Campaign-Marketing


### 팀명 : ILoveGogi
<img width="832" alt="image" src="https://github.com/user-attachments/assets/76a30f13-7fa6-4995-93f0-44f4a2ba28fa">


## Detail Role <a name = "role"></a>
+ [최창규](https://github.com/kyle4293)
  - Back-end, Front-end, DevOps
  - GitHub Actions CI/CD 구축
  - AWS, Docker-Compose 환경 구축
  - 사용자 인증/인가, 소셜로그인, 마이페이지,캠페인,마켓 CRUD 구축
  - REST API 설계
  
<Br>

+ [김세환](https://github.com/sehwan24)
  - Back-end
  - Flutter 서버 구축
  - 캠페인 추천 기능 구현

<Br>

+ [최유빈](https://github.com/elenachoi26)
  - PM
  - 캠페인 추천 기능 구현

<Br>



### Technologies
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)



---

# [목차](#index) <a name = "index"></a>

- [개요](#outline)
- [아키텍처](#structure)
- [결과물](#outputs)
- [ERD](#erd)  
- [테스트 및 모니터링](#test)
- [왜 이 기술을 사용했는가?](#why)
- [리팩토링 & 성능 개선](#refactoring)

<br>

# 개요 <a name = "outline"></a>

<details>
   <summary> 본문 확인 (Click)</summary>
<br />
디지털 마케팅의 급부상과 함께, '체험단'의 개념은 소비자 경험을 통해 솔직하고 구체적인 리뷰를 생성하는 중요한 마케팅 전략으로 자리 잡고 있습니다.
본 프로젝트는 체험단 고객의 데이터를 기반으로 그들이 관심을 가질만한 점포를 상단에 노출시키는 추천 시스템을 통해 체험단 마케팅의 효과를 극대화하는 데 목적이 있습니다.

</details>


<br>

# 아키텍처  <a name = "structure"></a>
![voicefinder 아키텍처](https://github.com/user-attachments/assets/56e40f79-e574-44d4-a536-74367bf3567a)

본 프로젝트는 AWS 기반의 클라우드 인프라를 활용하여 구축된 웹 서비스입니다.
GitHub Actions, Docker-Compose, AWS EC2를 통해 CI/CD 파이프라인을 구축해 개발 프로세스를 자동화했습니다.

<br>

# 결과물  <a name = "outputs"></a>

<details>
   <summary> 본문 확인 (Click)</summary>
<br />

## VoiceFinder
### 메인페이지

<img width="357" alt="image" src="https://github.com/user-attachments/assets/edfa4c18-1b3f-4d4c-a80a-4c404efc7077">

<br>

### 로그인, 회원가입, 소셜로그인

<img width="368" alt="image" src="https://github.com/user-attachments/assets/e20d0140-5b77-4c35-9560-fb565b74f29c">
<br>
<img width="338" alt="image" src="https://github.com/user-attachments/assets/af367829-7800-4877-a0b8-d00bee124675">


<br>

### 캠페인 목록, 상세
<img width="340" alt="image" src="https://github.com/user-attachments/assets/b1854bd7-6399-4c35-bca9-16c3796023ea">

<br>

### 프로필, 프로필 수정

<img width="370" alt="image" src="https://github.com/user-attachments/assets/a8e4c290-afd2-4720-9733-715a92419424">

<br>

## 비즈 웹

### 메인 페이지
<img width="376" alt="image" src="https://github.com/user-attachments/assets/5cb7bce9-11a7-4f0d-8d85-17f8332ca3a9">

<br>

### 마켓, 캠페인 등록
<img width="376" alt="image" src="https://github.com/user-attachments/assets/a4b761a8-a9f2-4453-bc8b-cc196a2f01d5">

<br>

### 마켓 관리
<img width="376" alt="image" src="https://github.com/user-attachments/assets/d3059384-b236-46e4-8446-2e096566be2e">

<br>
</details>
<br>

# ERD <a name = "erd"></a>

![voicefinder-erd](https://github.com/user-attachments/assets/8fbc8d7f-17e9-4ed7-bb20-1229c841283f)
<br>

# 테스트 및 모니터링 <a name = "test"></a>

추후 작성 예정

<br>

# 왜 이 기술을 사용했는가? <a name = "why"></a>

<details>
   <summary> 본문 확인 (Click)</summary>
<br />

추후 보완 예정

## Refresh Token - Redis
Refresh Token을 구현하는 과정에서 구현 방식과 Token의 저장 위치에 관해 많은 고민을 했습니다.
Refresh Token을 클라이언트에 전송하지 않고 서버에만 저장하는 것이 더 안전하다고 생각했습니다. 
따라서 Acess Token은 클라이언트의 캐시에 저장하고 유효기간을 짧게 설정, 만료시 Redis에 저장되어 있는 Refresh Token을 이용해 유효하다면 토큰을 재발급하도록 구현했습니다. 

<br>

## 검색 기능 - Querydsl
Spring Data JPA 사용시 복잡한 로직의 경우 쿼리 문자열이 상당히 길어집니다.  
이러한 문제를 해결하기 위해 Querydsl을 도입해 동적인 쿼리 작성을 편리하게 할 수 있도록 했습니다.
또, 문자가 아닌 코드로 쿼리를 작성함으로써 컴파일 시점에 문법 오류를 쉽게 확인할 수 있었습니다.
특히 프로젝트에서 캠페인을 종료일이 얼마 안남은 순서대로 정렬하면서, 페이징과 검색 기능까지 수행하기 위해서는 Querydsl을 사용해야겠다는 생각이 들어서 해당 기술을 도입했습니다.
<br>

## GitHub Actions
CI/CD 파이프라인을 구축하기 위해 GitHub Actions를 사용했습니다. GitHub를 이용해 프로젝트를 진행하기 때문에 Main에 Push 하는 시점에서
파이프라인이 트리거 되도록 하여 코드의 통합과 배포 과정을 자동화했습니다.

</details>

<br>

# 리팩토링 & 성능 개선 <a name = "refactoring"></a>

<details>
   <summary> 본문 확인 (Click)</summary>
<br />

진행중
  
## 무중단 배포

## QueryDsl 성능 개선

## AOP

## 테스트 코드


</details>


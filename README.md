# 비파 키우기 — Backend

목포 먹거리 여행 웹게임 '비파 키우기'의 백엔드 API입니다. 목포의 음식점·카페를 방문하며 캐릭터를 키우는 게임으로, 장소 데이터는 TourAPI로 수집했습니다.

<img src="https://img.shields.io/badge/Java%2017-007396?style=flat-square&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Boot%203.1-6DB33F?style=flat-square&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white">
<img src="https://img.shields.io/badge/Kakao%20Login-FFCD00?style=flat-square&logo=kakao&logoColor=black">
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white">

## 프로젝트 개요

| 항목 | 내용 |
|---|---|
| 개발 기간 | 2025.07 ~ 2025.11 |
| 인원 | BE 1명 / FE 1명 / Unity 1명 |
| 담당 (정의찬) | Back-end 전담 |
| 수상 | 2025.11 교내 공학작품 경진대회 우수상 |
| 프론트엔드 | [bipa_frontend](https://github.com/justicechan1/bipa_frontend) |
| API 명세 | [bipa-api-docs](https://github.com/justicechan1/bipa-api-docs) |

## 주요 기능

- **카카오 로그인**: Kakao OAuth 인증 후 JWT 발급, 첫 로그인 시 닉네임·캐릭터 설정
- **장소 데이터 수집**: TourAPI로 목포 음식점·카페 정보를 수집해 DB 적재
- **장소 검색·선택**: 장소 검색, 방문 장소 선택과 도감(방문 기록) 등록
- **게임 진행**: 캐릭터 저장, 상점 구매

## 트러블슈팅

1. **CORS Preflight 차단**
   - 문제: Spring Security 필터가 브라우저의 CORS Preflight(OPTIONS) 요청을 인증 실패로 먼저 막아, 프론트에서 모든 API 호출이 실패함
   - 해결: Security 필터 체인에서 CORS 설정을 우선 적용하고 OPTIONS 요청을 허용

2. **형식이 섞인 TourAPI 응답**
   - 문제: TourAPI 장소 데이터의 필드 형식이 항목마다 달라 그대로 적재하면 게임 상점 연동이 깨짐
   - 해결: 2단계 파싱(응답 구조 검증 → 필드별 변환)으로 방어한 뒤 게임 상점 데이터와 연동

## 로컬 실행

```bash
docker compose up -d   # MySQL
./gradlew bootRun
```

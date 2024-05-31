# [📑 티켓 예약 시스템]
-	공연 예매하기

# 프로젝트 기능 및 설계

### 회원/로그인
   
1. 회원가입 기능
- 아이디는 unique 하며 중복 확인을 거친다.
- 일반 서비스 페이지로 가입 시 일반 User 권한을 갖는다.
- 관리자 페이지로 가입 시 Manager 권한을 갖는다.
2. 회원탈퇴 기능
- 회원 탈퇴 시 로그인이 불가하고, 서비스 이용이 불가해진다.
- 관리자 탈퇴 시 기본적인 정보가 다 지워진다.
3. 회원 정보 수정 기능
-	회원 정보를 추가하거나 수정할 수 있다.
4. 로그인/로그아웃 기능
-	아이디/패스워드가 일치할 경우 로그인할 수 있다.
-	로그인 세션 기능을 사용한다.

### 상품 조회(메인 화면)
1. 예매율이 높은 상품
- 일정 기간 사람들이 많이 예매한 공연을 메인에 띄운다. (DB의 예매율 계산 / [공연 정보 ](https://www.kopis.or.kr/por/cs/openapi/openApiList.do?menuId=MNU_00074&tabId=tab3_3) API 사용)

  -> DB의 조인 사용 & 애매율 산출 -> 높은 순으로 조회
2. 예매 오픈 임박 상품
- 일정 기간 후 예매 가능한 공연을 메인에 띄운다.
3. 관람 후기
-	최신/인기 공연에 대한 후기를 메인에 띄운다.
4. 공연명 검색 시 리스트로 반환한다.
5. 결과물이 많을 경우 paging 처리를 한다.

### 상품 카테고리별 조회(세부 화면)
상품이 카테고리별(장르별)로 보이는 걸 제외하고 기능은 메인화면과 같다.

### 예매
1. 관람일/회차 선택
-	해당 공연에 대한 예매 가능한 날짜/회차 정보를 반환한다.
-	잔여 좌석 등급, 자리수(재고) 정보를 반환한다.
2. 좌석 선택
-	해당 공연(회차)의 좌석 예매 가능한 자리를 모두 반환한다.
3. 가격/할인
-	선택한 자리의 정보를 서버에 요청한다.
4. 결제
-	결제 완료 시 해당 예매자 정보/가격/좌석 정보 DB에 저장한다.

### 예매 내역 확인
- 일정 기간 내, 로그인한 회원의 예매 내역 리스트를 반환한다.
- 예매한 공연을 클릭하면 상세 예매 내역을 반환한다.

### 예매 취소
-	예매 상세 내역 내에서 예매 내역을 취소한다.

### 관람후기
-	예매한 사람만이, 예매한 공연의 시점이 지난 후, 해당 공연에 대한 리뷰 작성, 수정, 삭제가 가능하다.

### 관리자 페이지(공연 등록 및 관리)
1. 매니저 권한으로 로그인/로그아웃한다.
2. 공연 개요를 DB에 등록한다.
3. 예매 가능한 좌석 범위를 지정하여 요청 시, DB에 등록한다.
- Place 테이블에서 해당 공연장에 대한 범위를 관리자 페이지로 반환하고,
해당 공연에서 사용할 좌석 등급/범위/가격을 요청받아서, seat 테이블에 추가한다.

# Tech Stack
![js](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![js](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![js](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![js](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![js](https://img.shields.io/badge/GIT-F05032?style=for-the-badge&logo=spring&logoColor=white)

# ERD 데이터 모델링
![image](https://github.com/aNN-algorithm/Ticketing/assets/149382038/4a650ba4-ef47-43ce-b2b8-fe519916cfbf)


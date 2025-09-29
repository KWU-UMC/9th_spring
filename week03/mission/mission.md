# 미션 앱 핵심 기능 API 명세서

## 1. 홈 화면 API

### 홈 화면 데이터 조회

**API Endpoint**: `GET /api/home`

**Request Header**: 
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Query String**: 
- `location` (String, 선택): 지역명 (예: "안암동")

**Path Variable**: 없음

**Request Body**: 없음

---

## 2. 마이페이지 리뷰 작성 API

### 가게 리뷰 작성

**API Endpoint**: `POST /api/users/reviews/{storeId}`

**Request Header**: 
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Query String**: 없음

**Path Variable**: 
- `storeId` (Long): 리뷰를 작성할 가게 ID

**Request Body**: 
```json
{
  "body": "맛있었어요!",
  "rating": 5,
  "images": "이미지 url"
}
```

---

## 3. 미션 목록 조회 API

### 미션 목록 조회 (진행중/완료)

**API Endpoint**: `GET /api/missions`

**Request Header**: 
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Query String**: 
- `status` (String, 필수): `IN_PROGRESS` 또는 `COMPLETED`

**Path Variable**: 없음

**Request Body**: 없음

---

## 4. 미션 성공 처리 API

### 미션 완료 처리

**API Endpoint**: `POST /api/missions/{missionId}/complete`

**Request Header**: 
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Query String**: 없음

**Path Variable**: 
- `missionId` (Long): 완료 처리할 미션 ID

**Request Body**: 없음

---

## 5. 회원 가입 API

### 회원 가입

**API Endpoint**: `POST /api/auth/signup`

**Request Header**: 
```
Content-Type: application/json
```

**Query String**: 없음

**Path Variable**: 없음

**Request Body**: 
```json
{
  "name": "사용자 이름",
  "gender": "사용자 성별",
  "birthDate": "사용자 생년월일",
  "address": "사용자 주소",
  "favoriteCategories": "선호 음식 ID"
}
```
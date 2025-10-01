# WEEK3 - 하지명

---

### 홈 화면 API 설계 
#### 1. API Endpoint : `GET /home`
#### 2. Response Body
- `userId`: 사용자 id
- `regionsUserPoints`: 사용자가 해당 지역에 대해 보유하고 있는 포인트 정보
- `missions`: 사용자에게 부여된 미션 목록
```json
{
  "userId": 1,
  "regionsUserPoints":[],
  "missions": []
}
```
#### 3. Request Header
사용자 인증과 요청 구분을 위한 헤더
```json
{
  "Authorization": "Bearer <ACCESS_TOKEN>",
  "Content-Type": "application/json"
}
```
#### 4. Query String
- `region`: 지역 정보
#### 5. Path variable
필요 없음 </br>
사용자 정보는 JWT 토큰에서 추출 </br>

---
### 마이페이지 리뷰 작성  
#### 1. API EndPoint: `POST /users/reviews/{storeId}`
#### 2. Request Body
- `score`: 평가 점수
- `content`: 리뷰 내용
- `images`: 이미지 url
```json
{
  "score": 5,
  "content": "정말 맛있었습니다!",
  "images": "이미지 url"
}
```
#### 3. Request Header
```json
{
  "Authorization": "Bearer <ACCESS_TOKEN>",
  "Content-Type": "application/json"
}
```
#### 4. Query String: 필요 없음
#### 5. Path Variable
- `storeId`: 리뷰를 작성할 해당 가게 id

---
### 미션 목록 조회 (진행중, 진행 완료) 
#### 1. API EndPoint: `GET /missions`
#### 2. Reqeust Body: 필요 없음
#### 3. Request Header
```json
{
  "Authorization": "Bearer <ACCESS_TOKEN>",
  "Content-Type": "application/json"
}
```
#### 4. Query String
- status: `COMPLETED` (수행완료), `PROGRESSING` (수행중)
#### 5. Path Variable: 필요 없음
#### 6. Response Body
```json
{
  "userId": 1,
  "totalCount": 25,
  "missions": [
    {
      "missionId": 1,
      "title": "종로구 맛집 탐방",
      "description": "김치찌개 맛집인 이 가게를 방문하세요",
      "status": "COMPLETED",
      "rewardPoint": 10
    }
  ]
}
```
---
### 미션 성공 처리 API
#### 1. API Endpoint: `POST /missions/{missionId}/complete`
#### 2. Request Body: 필요 없음
#### 3. Request Header
```json
{
  "Authorization": "Bearer <ACCESS_TOKEN>",
  "Content-Type": "application/json"
}
```
#### 4. Query String: 필요 없음
#### 5. Path Variable
- `missionId`: 완료 처리할 미션 id


---
### 회원 가입 API
#### 1. API Endpoint: `POST /auth/signup`
#### 2. Request Body
```json
{
  "userName": "사용자 이름",
  "gender": "사용자 성별",
  "birth": "사용자 생년월일",
  "address": "사용자 주소",
  "preferCategories": [
    "선호 음식 id"
  ]
}
```
#### 3. Request Header
```json
{
  "Content-Type" : "application/json"
}
```
#### 4. Query String: 필요 없음
#### 5. Path Variable: 필요 없음


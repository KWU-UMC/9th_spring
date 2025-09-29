# WEEK 3 - 💧나미/이나영

## 회원가입
### API Endpoint
    POST /api/users/signup
### Header
    Content-Type: application/json
### Query String
    필요 없음
### Request body
    {
        "name": "유저1",
        "gender": "여",
        "birthday": "2003-03-11",
        "address": "서울특별시 노원구 광운로 20",
        "detailAdddress" : "광운대학교",
        "userFoods" : 1
    }

## 홈 화면 조회
### API Endpoint
    GET /api/home
### Header
    Authorization : Bearer {accessToken}
### Query String
    /api/home?locationId=1&page=1&size=10   // 지역(안암동)에서 첫 페이지 10개 조회
    /api/home?locationId=2&page=2&size=5    // 지역(노원구)에서 두 번째 페이지 5개 조회
    /api/home?page=1&size=20                // 지역 필터 없이 전체에서 20개 조회
### Request body
    X

## 미션 목록 조회
### API Endpoint
    GET /api/missions/my?status=
### Header
    Authorization: Bearer <accessToken>
### Query String
    /api/missions/my?status=ONGOING     // 진행 중
    /api/missions/my?status=COMPLETED   // 진행 완료
### Request body
    X

## 미션 성공 처리
### API Endpoint
    PATCH /api/missions/{userMissionId}/complete
### Header
    Authorization: Bearer <accessToken>
### Query String
    필요 없음
### Request body
    {
        "isComplete": true
    }

## 마이 페이지 리뷰 작성
### API Endpoint
    POST /api/reviews
### Header
    Authorization: Bearer <accessToken>
    Content-Type: application/json
### Query String
    필요 없음
### Request body
    {
        "storeId": 11,
        "content": "김치찌개 너무 맛있었어요!",
        "star": 4.5,
        "photoUrl": [
            "https://s3.bucket/review/photo1.jpg"
        ]
    }
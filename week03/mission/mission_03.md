## 1. 홈 화면

- 요구사항 정의
    1. 지역 선택 정보
        - 지역이름, 해당 지역에서 완료한 미션 개수
    2. 선택된 지역에서 도전 가능한 미션 목록 (MY MISSION)
        - 미션 포인트, 미션 조건, 가게 이름, 가게 유형(미구현 ㅠㅠ), 미션 만료 일시(미션 기한 바탕으로 계산?)
- API Endpoint
    - `GET /home?regionId={region_id}`
- Request Header

    ```json
    Authorization: Bearer {accessToken}
    Accept: application/json
    ```

- Query String
    - region_id : 선택된 지역 ID
- Response
    - 200 OK

    ```json
    {
      "region": {
        "id": 3,
        "name": "안암동",
        "successed_missions": 7,
        "earned_points": 1000
      },
      "missions": [
        {
          "mission_id": 8,
          "store_id": 31,
          "store_name": "반이학생마라탕",
          "store_category": "중식당",
          "mission_point": 500,
          "mission_condition": "10,000원 이상 식사",
          "mission_deadline": "2025-10-09T23:59:59"
        }
      ]
    }
    ```


## 2. 마이 페이지 리뷰 작성

- 요구사항 정의
    1. 별점 입력
        - 가게 이름, 입력할 별점
    2. 리뷰 작성 페이지
        - 가게 이름, 입력한 별점, 리뷰 내용, 업로드한 사진(3개까지)
- API Endpoint
    - `POST /reviews`
- Request Body

    ```json
    {
      "store_id": 31,
      "rating": 5,
      "content": "맛있었습니다.",
      "photos": [
        "https://example.com/img1.jpg",
        "https://example.com/img2.jpg"
      ]
    }
    ```

- Request Header

    ```json
    Authorization: Bearer {accessToken}
    Content-Type: application/json
    Accept: application/json
    ```

- Response
    - 201 Created

    ```json
    {
      "review_id": 209,
      "store_id": 31,
      "rating": 5,
      "content": "맛있었습니다.",
      "created_at": "2025-10-02T12:34:56"
    }
    ```


## 3. 미션 목록 조회**(진행중, 진행 완료)**

- 요구사항 정의
    - 미션 포인트, 가게 이름, 미션 조건, 미션 상태, 리뷰 작성 완료 여부 조회 필요
- API Endpoint
    - `GET /missions?status={status}`
- Request Header

    ```json
    Authorization: Bearer {accessToken}
    Accept: application/json
    ```

- Query String
    - status : (선택) in_progress,success
- Response
    - 200 OK

    ```json
    {
      "missions": [
        {
          "user_mission_id": 831,
          "status": "IN_PROGRESS",
          "store_id": 31,
          "store_name": "반이학생마라탕",
          "mission_id": 8,
          "mission_point": 500,
          "mission_condition": "12,000원 이상 식사",
          "review_written": false,
          "updated_at": "2025-10-02T10:01:00"
        },
        {
          "user_mission_id": 209,
          "status": "SUCCESS",
          "store_id": 31,
          "store_name": "반이학생마라탕",
          "mission_id": 9,
          "mission_point": 500,
          "mission_condition": "10,000원 이상 식사",
          "review_written": true,
          "updated_at": "2025-09-30T09:00:00"
        }
      ],
    }
    ```


## 4. 미션 성공 누르기

- 요구사항 정의
    - 실제로는 미션 도전 시작 api → user_mission_id 생성 → 미션 성공 api 흐름으로 구현할 듯
- API Endpoint
    - `POST/user-missions/{userMissionId}/success`
- Request Header

  ```json
  Authorization: Bearer {accessToken}
  Accept: application/json
  ```

- Path Variable
    - userMissionId : 사용자 미션 ID
- Response
    - 200 OK

    ```json
    {
      "user_mission_id": 831,
      "status_before": "IN_PROGRESS",
      "status_after": "SUCCESS",
      "completed_at": "2025-10-02T12:40:00",
      "successed_missions": 7,
    }
    ```


## 5. 회원가입 (소셜 로그인 고려X)

- API Endpoint
    - `POST /users`
- Request Body

    ```json
    {
      "name": "최현진",
      "gender": "FEMALE",
      "birthdate": "2004-08-31",
      "address": "서울시 노원구 월계동",
      "preferred_food_ids": [1, 3, 5],
      "phone": "010-1234-5678",
      "email": "choehyeonjin831@gmail.com",
      "nickname": "현진"
    }
    ```

- Request Header

    ```json
    Content-Type: application/json
    Accept: application/json
    ```

- Response
    - 201 Created

    ```json
    {
      "id": 831
      "name": "최현진",
      "gender": "FEMALE",
      "birthdate": "2004-08-31",
      "address": "서울시 노원구 월계동",
      "preferred_food_ids": [1, 3, 5],
      "phone": "010-1234-5678",
      "email": "choehyeonjin831@gmail.com",
      "nickname": "현진",
      "point": 0,
      "created_at": "2025-10-02T12:00:00"
    }
    ```
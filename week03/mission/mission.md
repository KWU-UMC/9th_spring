- 회원가입

  Endpoint  : Post /api/users/signup

  Request Header : application/json

  Request Body

    ```json
    {
    "location_agreement": "True",
    "marketing_agreement": "True",
    "name":"홍길동",
    "gender":"M",
    "birdhDate":"2000-01-01",
    "address":"대구광역시 달서구"
    "preferenceFood":["한식","일식","중식"]
    }
    ```

- 홈화면

  Endpoint : Get /api/home

  Request Header

    ```json
    Authorization: Bearer <JWT_TOKEN>
    Content-Type: application/json
    ```

  query String

    ```json
    /api/home?locationId={locationId}&page={page}&limit={limit}
    ```

- 마이페이지 리뷰 작성

  Endpoint : Post /api/reviews

  Request Header

    ```json
    Authorization: Bearer <JWT_TOKEN>
    Content-Type: application/json
    ```

  Request Body

    ```json
    {
    "userMissionId":"1"
    "StoreId":"20",
    "rating":5,
    "content":"음식이 맛있어요"
    }
    ```

- 미션 목록 조회(진행중)

  Endpoint : Get /api/missions/in-progress

  Request Header

    ```json
    Authorization: Bearer <JWT_TOKEN>
    Accept: application/json
    ```

  Path Variable

    ```json
    /api/missions/in-progress?page={page}&limit={limit}
    ```

- 미션 목록 조회(완료)

  Endpoint : Post /api/missions/completed

  Request Header

    ```json
    Authorization: Bearer <JWT_TOKEN>
    Accept: application/json
    ```

  Path Variable

    ```json
    /api/missions/completed?page={page}&limit={limit}
    ```

- 미션 성공누르기

  Endpoint : Patch /api/missions/{missionId}/completed

  Request Header

    ```json
    Authorization: Bearer <JWT_TOKEN>
    Accept: application/json
    ```

  Path Variable : missionId
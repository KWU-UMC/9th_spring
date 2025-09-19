# WEEK1 - 하지명

---

## 미션
1. 0주차 때 직접 설계한 데이터베이스를 토대로 아래의 화면에 대한 쿼리를 작성

- 설계한 DB 사진
![UMC_mission_00.png](../../week00/mission/UMC_mission_00.png)

---

### 1. 리뷰 작성하는 쿼리
1. 사용자가 선택한 가게id를 통해 지역id 조회
2. review 테이블에 리뷰 추가
```SQL
SELECT region_id
FROM stores
WHERE store_id="사용자가 선택한 가게id"

INSERT INTO review (
    review_id,
    store_id,
    user_id,
    region_id,
    score,
    content,
    create_at
)
VALUES (
    리뷰id, 선택한가게id, 사용자id, 해당 가게 지역id, 리뷰점수, 리뷰내용, 작성시간(NOW())
);
```

### 2. 마이 페이지 화면 쿼리
사용자 정보와 사용자 포인트를 담아야 함  
Users테이블(사용자 정보 테이블)과 UserPoint테이블(사용자 포인트 테이블)을 JOIN해서 조회함
```SQL
SELECT U.*, UP.point
FROM Users as U
JOIN UserPoint as UP
    ON U.user_id=UP.user_id
WHERE U.user_id="사용자id"
```

### 3. 내가 진행 중, 진행 완료한 미션 모아서 보는 쿼리
UserMissions테이블(사용자가 할당받은 미션 정보 테이블)과 Missions테이블(미션 정보 테이블)을 join
```SQL
SELECT 
    UM.*,
    M.title,
    M.description,
FROM UserMissions as UM
JOIN Missions as M
    ON UM.mission_id=M.mission_id
WHERE UM.user_id ="사용자 id"
    AND UM.status IN ("진행중", "완료")
ORDER BY UM.assigned_at DESC
LIMIT 15 OFFSET (n - 1) * 15
```

### 4. 홈 화면 쿼리
Missions 테이블에서 특정 지역 id와 미션 상태가 "ACTIVE"인 Mission만 조회
```sql
SELECT 
    M.mission_id,
    M.store_id,
    M.region_id,
    M.title,
    M.description,
    M.start_date,
    M.end_date,
    M.status,
    S.store_id,
    S.address,
    S.category
FROM Missions as M
JOIN Stores as S
    ON M.store_id=S.store_id
    AND M.region_id=S.region_id
WHERE
    M.region_id="사용자가 선택한 지역 id"
    AND M.status ="active"
ORDER BY M.start_date DESC
LIMIT 15 OFFSET (n - 1) * 15
```

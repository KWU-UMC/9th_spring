# 1. 리뷰 작성하는 쿼리 (* 사진의 경우는 일단 배제)

```jsx
INSERT INTO review (member_id, store_id, score, body, created_at)
VALUES ('사용자 id', '가게 id', 5, '음 너무 맛있어요 포인트도 얻고 맛있는 맛집도 알게 된 것 같아 너무나도 행복한 식사였답니다. 다음에 또 올께요!!', NOW());
```

# 2. 마이 페이지 화면 쿼리
```jsx
    SELECT 
        m.name AS nickname,
        m.email,
        m.point
        CASE //인증과 미인증 표시하는 단계
            WHEN m.phone_number IS NULL THEN '미인증' 
            ELSE '인증됨' 
        END AS phone_status
    FROM member AS m
    WHERE m.id = {현재 로그인된 사용자 ID};
   ```
# 3. 내가 진행중, 진행 완료한 미션 모아서 보는 쿼리(페이징 포함)
```jsx
    SELECT
    mm.id AS member_mission_id,
    m.reward,
    s.name AS store_name,
    m.mission_spec,
    mm.status,
    FROM member_mission AS mm
    JOIN mission AS m ON mm.mission_id = m.id
    JOIN store AS s ON m.store_id = s.id
    WHERE mm.member_id = {로그인 사용자 ID 넣기}
    AND mm.status IN ('진행중', '성공')
    LIMIT 3 OFFSET 0;
```

# 3-1. 정렬 기준을 1순위는 포인트로 2순위는 최신순으로 하여 Cursor기반 페이지네이션을 구현해보세요!
```jsx
CONCAT(LPAD(m.reward, 10, '0'), LPAD(UNIX_TIMESTAMP(mm.created_at), 20, '0'))
```
- created_at은 DATETIME 타입이라 UNIX_TIMESTAMP()나 DATE_FORMAT() 같은 함수로 숫자/문자열로 변환해야 함.
```jsx  
  
SELECT
  mm.id AS member_mission_id,
  m.reward,
  s.name AS store_name,
  m.mission_spec,
  mm.status, 
  CONCAT(LPAD(m.reward, 10, '0'), LPAD(UNIX_TIMESTAMP(mm.created_at), 20, '0')) as cursor_value
  
FROM member_mission AS mm
  JOIN mission AS m ON mm.mission_id = m.id
  JOIN store AS s ON m.store_id = s.id
WHERE 
  mm.member_id = {로그인 사용자 ID 넣기}
  AND mm.status IN ('진행중', '성공')
  AND (
    CONCAT(LPAD(m.reward, 10, '0'), LPAD(UNIX_TIMESTAMP(mm.created_at), 20, '0'))
    < '{이전 페이지 마지막 cursor_value}'
    )
ORDER BY m.reward DESC, mm.created_at DESC
LIMIT 3;
```

# 4. 홈 화면 쿼리(현재 선택 된 지역에서 도전이 가능한 미션 목록, 페이징 포함)**

```jsx
    SELECT 
        r.name AS region_name, // 현재 선택된 지역 이름 상단 표시
        m.id AS mission_id,
        s.name AS store_name,
        m.mission_spec,
        m.reward,
        DATEDIFF(m.deadline, NOW()) AS D_day
    FROM mission AS m
    JOIN store AS s ON m.store_id = s.id
    JOIN region AS r ON s.region_id = r.id
    WHERE r.id = {선택된 지역 ID}  // 현재 선택된 지역 필터링
    ORDER BY m.deadline ASC  // 마감일 기준 정렬
    LIMIT 10 OFFSET 0;  -- // Offset 페이징 적용
```
# 시니어미션
https://velog.io/@seonseon/UMC9%EA%B8%B0-%EC%8B%A4%EC%A0%84-SQL
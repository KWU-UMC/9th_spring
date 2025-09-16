## 🔥 미션

---

1. 0주차 때 **직접 설계한** 데이터베이스를 토대로 아래의 화면에 대한 쿼리를 작성
- 설계한 DB 사진 (0주차 DB 수정 가능!)

![ERD(week1).png](ERD%28week1%29.png)
==>0주차 ERD에서 일부 수정하였습니다!
- 본문

리뷰 작성하는 쿼리,
* 사진의 경우는 일단 배제

```cpp
INSERT INTO Review(member_id,star,review_content,created_at,updated_at)
VALUES(닉네임1234,5,"너무 맛있어요",NOW(),NOW())
```



마이 페이지 화면 쿼리

```cpp
Select u.name,u.email,u.phone,u.point
From user u
WHERE u.member_id=현재 로그인한 유저 ID
```


내가 진행중, 진행 완료한 미션 모아서 보는 쿼리(페이징 포함)

```cpp
Select m.reward_point, m.reward_description, m.created_at,m.updated_at,s.name,
LPAD(m.mission_id,10,0) AS cursor_value
FROM user_mission um
left join Mission m on um.mission_id = m.mission_id
left join store s on m.store_id=s.store_id
WHERE um.member_id=현재 로그인한 유저 ID AND (LPAD(m.mission_id,10,'0')<'0000000042')
ORDER BY m.mission_id DESC
LIMIT 3;

```



홈 화면 쿼리
(현재 선택 된 지역에서 도전이 가능한 미션 목록, 페이징 포함)

```cpp
SELECT m.mission_id,
       m.title,
       m.reward_point,
       m.reward_description,
       m.deadline,
       s.name,
       l.name
       LPAD(m.mission_id,10,0) AS cursor_value
FROM MISSION m
Join store s in m.store_id = s.store_id
Join location l in s.location_id=l.location_id
left join user_mission um on um.mission_id = m.mission_id 
AND um.member_id=현재 로그인한 유저 ID
WHERE l.name="안암동" AND um.is_complete=false
ORDER BY AND (LPAD(m.mission_id,10,'0')<'0000000042')
LIMIT 2;
```
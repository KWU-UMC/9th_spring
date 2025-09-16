# WEEK 1 - 이나영🫧
## 실습 인증❕
### 리뷰 작성하는 쿼리
    insert into review (content, star, created_at, user_id,  store_id)
    values ('음 너무 맛있어요 포인트도 얻고 맛있는 맛집도 알게 된 것 같아 너무나도 행복한 식사였답니다. 다음에 또 올게요!!너무 맛있어요', 5.0, '2022-05-14', 1, 1);

### 마이 페이지 화면 쿼리
    select u.name, u.email,
    case
    when u.phone_num is null or u.phone_num = '' then '미인증'
    else '인증완료'
    end as phone_verified,
    u.point
    from user u
    where u.id = 1;

### 내가 진행 중, 진행 완료한 미션 모아서 보는 쿼리 (페이징 포함)
    select um.id as user_mission_id, m.id as mission_id, m.point,
    case when um.is_complete = 1 then '성공'
    else '진행 중'
    end as mission_status,
    s.name as store_name, m.conditional, m.created_at
    from user_mission um
    join mission m on um.mission_id = m.id
    join store s on m.store_id = s.id
    where um.user_id =1
    order by m.created_at desc
    limit 10 offset 0;

### 홈 화면 쿼리 (현재 선택된 지역에서 도전이 가능한 미션 목록, 페이징 포함)
    select l.name, m.id as mission_id, s.name as store_name, m. conditional, m.point,
    datediff(m.duration, curdate()) as d_day,
    case when um.id is null then '도전 가능'
    when um.is_complete = 0 then '진행 중'
    when um.is_complete = 1 then '완료'
    end as mission_status
    from mission m
    join store s on m.store_id = s.id
    join location l on s.location_id = l.id
    left join user_mission um on m.id = um.mission_id
    and um.user_id = 1
    where l.id = 2
    order by m.created_at desc
    limit 10 offset 0;
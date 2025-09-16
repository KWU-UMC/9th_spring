// mission
## 미션 1

    INSERT INTO review (review_content, rating, user_id, store_id, created_at)
    VALUES ('음 너무 맛있어요 포인트도 얻고 맛있는 맛집도 알게 된 것 같아 너무나도 행복한 식사였답니다. 다음에 또 올게요!!', 5, ?, ?, NOW());

## 미션 2
    SELECT name, email, point FROM user WHERE user_id = ?;

## 미션 3
---------
쿼리를 나눠서 작성했습니다.
----------

    진행완료 페이지 쿼리
    SELECT
        um.mission_id,
        um.is_complete,
        m.mission_content,
        m.point,
        s.store_name,
        '성공' as status
    FROM user_mission um
    JOIN mission m ON um.mission_id = m.mission_id
    JOIN store s ON m.store_id = s.store_id
    WHERE um.user_id = ?
        AND um.is_complete = TRUE
    ORDER BY um.completed_at DESC
    LIMIT ? OFFSET ?;

    진행중 페이지 쿼리
    SELECT
        um.mission_id,
        um.is_complete,
        m.mission_content,
        m.point,
        s.store_name,
        '진행중' as status
    FROM user_mission um
    JOIN mission m ON um.mission_id = m.mission_id
    JOIN store s ON m.store_id = s.store_id
    WHERE um.user_id = ?
        AND um.is_complete = FALSE
        AND m.deadline > NOW()
    ORDER BY m.deadline ASC
    LIMIT ? OFFSET ?;

## 미션 4
---------
쿼리를 나눠서 작성했습니다.
----------
    미션 목록 쿼리
    SELECT
        m.mission_id,
        m.mission_content,
        m.point as mission_point,
        m.deadline,
        s.store_name,
        s.store_type,
        l.local_name,
        DATEDIFF(m.deadline, NOW()) as days_remaining
    FROM mission m
    JOIN store s ON m.store_id = s.store_id
    JOIN local l ON s.local_id = l.local_id
    LEFT JOIN user_mission um ON m.mission_id = um.mission_id AND um.user_id = ?
    WHERE l.local_name = ?
        AND m.deadline > NOW()
        AND um.is_complete = FALSE
    ORDER BY m.deadline ASC
    LIMIT ? OFFSET ?;


    ? 지역 미션 완료 상태 확인 쿼리
    SELECT
        COUNT(CASE WHEN um.is_complete = TRUE THEN 1 END) as region_completed_count,
        COALESCE(rmb.bonus_point, 0) as bonus_point,
        COALESCE(rmb.is_claimed, FALSE) as bonus_claimed
    FROM local l
    LEFT JOIN store s ON l.local_id = s.local_id
    LEFT JOIN mission m ON s.store_id = m.store_id
    LEFT JOIN user_mission um ON m.mission_id = um.mission_id AND um.user_id = ?
    LEFT JOIN region_mission_bonus rmb ON l.local_id = rmb.local_id AND rmb.user_id = ?
    WHERE l.local_name = ?
    GROUP BY l.local_id, l.local_name, rmb.bonus_point, rmb.is_claimed;

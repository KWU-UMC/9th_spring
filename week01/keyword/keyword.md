- DB Join이란?

DB Join은 여러 테이블에 흩어져 있는 데이터를 하나로 합쳐서 조회할 때 사용하는 기능으로 각 테이블을 PK와 FK를 이용하여 두개 이상의 테이블의 행을 연결하는 역할을 한다.

- Join 종류들
    1. INNER JOIN : 두 테이블에서 조건에 맞는 데이터만 가져오는 것으로 on 절에 조인 조건을 반드시 넣어야 한다.

    ```sql
    SELECT m.mission_id,
           m.title,
           m.reward_point,
           m.reward_description,
           m.deadline,
           s.name,
           l.name,
           LPAD(m.mission_id,10,'0') AS cursor_value
    FROM MISSION m
    INNER JOIN store s ON m.store_id = s.store_id
    INNER JOIN location l ON s.location_id=l.location_id
    ```

    1. LEFT JOIN : 두 테이블에서 왼쪽 테이블은 전부 가져오고 오른쪽에 없으면 NULL 로 채운다.

    ```sql
    SELECT u.user_id, u.name, o.order_id
    FROM Users u
    LEFT JOIN Orders o
      ON u.user_id = o.user_id;
    ```

    1. RIGHT JOIN : 두 테이블에서 왼쪽 테이블은 전부 가져오고 오른쪽에 없으면 NULL 로 채운다.

    ```sql
    SELECT u.user_id, u.name, o.order_id
    FROM Users u
    RIGHT JOIN Orders o
      ON u.user_id = o.user_id;
    ```

    1. FULL OUTER JOIN : 양쪽 테이블 모두 포함한 테이블 전부를 가져오고 없으면 NULL로 채운다.

    ```sql
    SELECT u.user_id, u.name, o.order_id
    FROM Users u
    FULL OUTER JOIN Orders o
      ON u.user_id = o.user_id;
    ```

    1. CROSS JOIN : 조건 없이 두 테이블의 모든 행을 곱 집합으로 결합한다.

    ```sql
    SELECT u.user_id, o.order_id
    FROM Users u
    CROSS JOIN Orders o;
    ```

- 트랜잭션이란?

  데이터베이스에서 하나의 논리적 작업 단위로 SQL 문장이 모여 한번에 수행되어야 하는 작업을 뜻한다

  예를 들어 송금하는 과정에서 송금을 보내는 사람의 계좌에서는 잔액이 차감 되어야 하고 송금을 받는 사람의 계좌에서는 잔액이 증가하는 과정 두 개가 한번에 이루어져야 한다.

- Join on 과 where의 차이

  JOIN ON은 두테이블을 어떤 기준으로 연결할지 정하는 조건이고, WHERE은 그렇게 JOIN 된 결과에서 필요한 행만 걸러내는 조건이다. OUTER JOIN인 경우 JOIN ON에서 ON절에 조건을 주면 조건에 맞지 않더라도 테이블의 행은 유지 되는 반면 WHERE절에 조건을 주면 조건에 맞지 않는 행은 결과에서 빠져버리는 차이점이 있다.
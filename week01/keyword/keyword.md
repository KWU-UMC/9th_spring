# WEEK 1 - 이나영🫧
## 🔑 핵심 키워드 
### DB JOIN이란?
    - DB에서 두 개 이상의 테이블을 연결해 하나의 결과로 테이블을 만드는 것을 의미한다.
### JOIN 종류들
    - INNER JOIN
        - 조인하는 테이블의 on 절의 조건이 일치하는 결과만 출력
        - MySQL에서는 JOIN, INNER JOIN, CROSS JOIN이 모두 같은 의미
    - LEFT/RIGHT JOIN
        - 두 테이블이 합쳐질 때 왼쪽/오른쪽 기준 테이블의 것이 모두 출력되는 join
    - FULL OUTER JOIN
        - 두 테이블 전부 출력하는 것
    - SELF JOIN
        - 자기 자신을 조인한 것

### 트랜잭션이란?
    - DB의 상태를 변경시키는 작업의 단위
    - 트랜잭션의 특징 : ACID
        - Atomicity 원자성
          트랜잭션이 DB에 모두 반영되거나, 전혀 반형되지 않아야 함 
      - Consistenty 일관성
          트랜잭션의 작업 처리 결과는 항상 일관성 있어야 함
      - Isolation 독립성
          둘 이상의 트랜잭션이 동시에 병행 실행되고 있을 때, 어떤 트랜잭션도 다른 트랜잭션 연산에 끼어들 수 없음
      - Durability 지속성
          트랜잭션이 성공적으로 완료되었으면, 결과는 영구적으로 반영되어야 함
    - 트랜잭션 연산
      - COMMIT
      트랜잭션이 성공적으로 수행되었음을 선언하는 연산
      - ROLLBACK
      트랜잭션 수행이 실패했음을 선언하고, 작업을 취소하는 연산
### Join on과 where의 차이
      - Join on : **join 전**에 조건을 필터링함
      - where : **join 후**에 조건을 필터링함
      주의!
      inner join 하면 둘 다 차이 없음
      outer join 시 차이 있음...
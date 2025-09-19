# WEEK 1 - 하지명

---
## 핵심 키워드
- DB JOIN
- JOIN 종류
- 트랜잭션
- ON vs WHERE

---

## 1. DB Join이란?

둘 이상의 테이블을 연결해서 데이터를 검색하는 방법  
연결하려면 테이블들이 적어도 하나의 컬럼을 공유하고 있어야 한다.  
이 공유하고 있는 컬럼을 PK or FK 값으로 사용한다.

## 2. Join 종류들
### INNER JOIN(내부 조인)  
두 테이블을 조인할 때, 두 테이블에 모두 지정한 열의 데이터가 있어야 함  
두 테이블을 연결할 때 가장 많이 사용  
(~~약간 교집합같은 느낌~~)

```sql
SELET <열 목록>
FROM <첫 번째 테이블>
    INNER JOIN<두 번째 테이블>
    ON <조인 조건>
WHERE 검색 조건
```
    
### OUTER JOIN(외부 조인)   
내부 조인은 두 테이블에 모두 데이터가 있어야만 결과가 나오지만,
외부 조인은 한쪽에만 데이터가 있어도 결과가 나옴
    
```sql
SELET <열 목록>
FROM <첫 번재 테이블 (LEFT 테이블)>
    <LEFT|RIGHT|FULL> OUTER JOIN <두 번째 테이블 (RIGHT 테이블)>
    ON <조인 조건>
WHERE 검색 조건
```
   1. LEFT OUTER JOIN  : 왼쪽 테이블의 모든 값이 출력되는 조인
   2. RIGHT OUTER JOIN : 오른쪽 테이블의 모든 값이 출력되는 조인
   3. FULL OUTER JOIN : 왼쪽 외부 조인과 오른쪽 외부 조인이 합쳐진 것  
 
### CROSS JOIN(상호 조인)  
한 쪽 테이블의 모든 행과 다른 쪽 테이블의 모든 행을 조인시키는 기능  
상호 조인 결과의 전체 행 개수는 두 테이블의 각 행의 개수를 곱한 수 **(CARTESIAN PRODUCT 카티션 곱)**  
```sql
SELET *
FROM <첫 번째 테이블>
    CROSS JOIN <두 번째 테이블>
```

### SELF JOIN(자체 조인)  
자체 조인은 자기 자신과 조인하므로 1개의 테이블을 사용
별도의 문법이 있는 것이 아니고 1개로 조인하면 자체 조인이 됨  
```sql
SELET <열 목록>
FROM <테이블> 별칭A
    INNER JOIN <테이블> 별칭B
WHERE 검색조건
```



##  3.트랜잭션이란?
단일한 논리적인 작업 단위, 즉 더 이상 분할이 불가능한 업무처리 단위  
(한꺼번에 수행되어야 할 일련의 연산 모음집)  
SQL문을 실행할 때, 일부의 쿼리만 성공한다면, DB에 실제로 반영되는 일은 일어나지 않음.
하나의 SQL 쿼리문이 트랜스잭션이기 때문  

### 트랜잭션 로직
1. 트랜잭션을 시작한다.
2. 데이터를 읽거나 쓰는 등의 SQL문들을 포함해서 로직을 수행한다.
3. 일련의 과정들이 문제없이 동작했다면 트랜잭션을 _커밋_ 한다.
4. 중간에 문제가 발생했다면 트랜잭션을 _롤백_ 한다.

### 트랜잭션의 속성
1. 원자성  
트랜잭션이 데이터베이스에 모두 반영이 되거나, 아니면 전혀 반영되지 않아야 함  
    * 트랜잭션은 논리적으로 쪼개질 수 없는 작업 단위이기 때문에, 내부의 SQL문들이 모두 성공해야 함
    * 만약 중간에 SQL문이 실패하면 작업을 모두 취소해 아무 일 없었던 것처럼 롤백
2. 일관성  
트랜잭션의 작업 처리 결과가 항상 일관성있어야 함  
    * 트랜잭션은 DB 상태를 consistent 상태에서 또 다른 consistent 상태로 바꿔줘야 함
    * 트랜잭션이 진행되는 동안 데이터베이스가 변경되더라도 업데이트된 데이터베이스로 트랜잭션이 진행되는 것이 아니라,
   처음에 트랜잭션을 진행하기 위해 참조된 데이터베이스로 진행됨
3. 독립성  
어떤 하나의 트랜잭션이라도, 다른 트랜잭션의 연산에 끼어들 수 없음  
    * 여러 트랜잭션들이 동시에 실행될 때도 혼자 실행되는 것처럼 동작하게 만듦
    * DBMS는 여러 종류의 isolation level을 제공 (개발자는 어떤 level로 트랜잭션을 동작시킬지 설정할 수 있음)
4. 영구성  
트랜잭션이 성공적으로 완료됐을 경우, 그 결과가 영구적으로 반영되어야 함  
    * 커밋된 트랜잭션은 DB에 영구적으로 저장함. 즉, DB 시스템에 문제가 생겨도 커밋된 트랜잭션은 DB에 남아있음
    * 트랜잭션의 영구성은 DBMS가 보장함


### 4. Join on과 where의 차이
- **ON: JOIN을 하기 전에 필터링을 함**
  - ON 조건으로 필터링이 된 레코드 간의 JOIN이 이뤄짐
  - 컬럼명이 다르더라도 JOIN 조건으로 사용할 수 있음
  - WHERE절과 혼용해서 사용할 수 있음
  - 검색 조건을 사용할 수 있지만, WHERE을 쓰는 것을 권장함

- **WHERE: JOIN을 한 후에 필터링을 함**
    - JOIN을 한 결과에서 WHERE 조건절로 필터링이 이뤄짐

1. INNER JOIN에서의 ON과 WHERE  
INNER JOIN에서의 ON 조건과 WHERE 조건은 동일함  
`INNER JOIN + ON 조건절 + ON 조건절` = `INNER JOIN + ON 조건절 + WHERE 조건절`  
    ```sql
    SELETE * 
    FROM a INNER JOIN b ON a.key = b.key AND a.key2=b.key2
   
    SELECT *
    FROM a, b
    WHERE a.key = b.key AND a.key2 = b.key2

   SELECT *
    FROM a INNER JOIN b ON a.key = b.key
    WHERE a.key2 = b.key2
    ```
   
2. OUTER JOIN에서의 ON과 WHERE  
- TABLE 1  

    | col1 | col2  |
   |-----|-------|
   | 1   | one   |
   | 2   | two   |
    | 3   | three |

- TABLE 2
    
    |col1| col2  |
    |-----|-------|
    | 1|first|
    |2|second|

  - ON  
      ```sql
    SELECT t1.col1, t1.col2, t2.col1, t2.col2
      FROM table1 t1 LEFT OUTER JOIN table2 t2
      ON t1.col1 = t2.col1
      AND t1.col2 = 'first';
    ```
      **쿼리 실행 결과**
    - table1의 전체 row와 table2의 col2 컬럼 값이 first 인 row만 뽑은 row들을 OUTER JOIN
    - ON 조건은 JOIN을 하기 전 필터링

        | t1.col1 | t1.col2 | t2.col1 | t2.col2 |
        |---------|---------|---------|---------|
        | 1       | one     | 1       | first   |
        | 2       | two     | null    | null    |
        | 3       | three   | null    | null    |

  - WHERE
      ```sql
    SELECT t1.col1, t1.col2, t2.col1, t2.col2
      FROM table1 t1 LEFT OUTER JOIN table2 t2
      ON t1.col1 = t2.col1
      WHERE t2.col2 = 'first';
    ```
      **쿼리 실행 결과**  
      - table1의 전체 row와 table2의 전체 row를 OUTER JOIN한 후 table2의 col2 컬럼 값이 first인 row만 추출
      - WHERE 조건은 JOIN을 한 이후 필터링

        | t1.col1 | t2.col2 | t2.col1 | t2.col2 |
        |---------|---------|--------|---------|
|         | 1       | one     | 1      |first|
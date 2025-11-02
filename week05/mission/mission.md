# Week 5
### 깃허브 링크
브랜치 이름: Feat/Chapter5 </br>
[깃허브링크](https://github.com/hajimeong/UMC_Study_Week5/tree/Feat/Chapter5)

### 1. 리뷰 작성 쿼리
1. 사용자가 선택한 가게id를 통해 지역id 조회
```java
//리뷰 작정을 위한 쿼리 -> 사용자가 선택한 가게의 region을 조회
    @Query("SELECT s.regionId FROM Store s WHERE s.storeId=:storeId")
    Optional<Long> findRegionByStoreId(@Param("storeId") Long storeId);
```


### 2. 마이페이지 화면 쿼리
사용자 정보와 사용자 포인트를 담아야 함
```java
//마이페이지 화면 쿼리 작성
@Query("SELECT m FROM Member m WHERE m.id=:memberId")
Optional<Member> findMemberMyPage(@Param("memberId") Long memberId);
```

### 3. 진행 중, 진행 완료한 미션 조회 쿼리
UserMissions테이블(사용자가 할당받은 미션 정보 테이블)과 Missions테이블(미션 정보 테이블)을 join
```java
 //진행 중, 진행 완료한 미션을 모아서 보는 쿼리
    @Query("SELECT mm"+
            "FROM MemberMission mm"+
            "JOIN FETCH mm.mission m" +
            "WHERE mm.member.id = :memberId"+
            "AND (mm.completed = true OR mm.completed=false)"+
            "ORDER BY mm.createAt DESC")
    Page<MemberMission> findMemberMissionByMemberId(@Param("memberId") Long memberId, Pageable pageable);

```

### 4. 홈 화면 쿼리
Missions 테이블에서 특정 지역 id와 미션 상태가 "ACTIVE"인 Mission만 조회
```java
//홈 화면 쿼리  -> 이미 선택된 지역에서 도전이 가능한 미션 목록 조회
    @Query("""
        SELECT m
        FROM Mission m
        JOIN m.store s
        WHERE s.region.id = :regionId
          AND m.status = 'AVAILABLE'
          AND m.deadline >= CURRENT_DATE
        ORDER BY m.deadline ASC""")
    Page<Mission> findAvailableMissionsByRegion(@Param("regionId") Long regionId, Pageable pageable);

```
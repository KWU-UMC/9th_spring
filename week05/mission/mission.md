# WEEK 5 - 💧나미/이나영
## 깃허브 주소
https://github.com/na311ng/umc9th

## 리뷰 작성 

### 쿼리문 
    @Repository
    public interface ReviewRepository extends JpaRepository<Review, Long> {
    }
### api 명세서
    {
        "content": "너무 맛있어요!",
        "star": 5.0,
        "userId": 1,
        "storeId": 1
    }
## 마이페이지 조회
### 쿼리문
    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
    
        // 특정 사용자 정보 조회
        Optional<User> findById(Long id);
        
        // 이메일로 사용자 찾기 (추가 예시)
        Optional<User> findByEmail(String email);
    }
### api 명세서
    {
        "id": 1,
        "name": "홍길동",
        "email": "example@gmail.com",
        "point": 1200
    }
## 진행 중/진행 완료한 미션 모아서 보는 쿼리 (페이징 포함)
### 쿼리문
    @Query("""
        select new com.example.umc9th.domain.mission.dto.MemberMissionResponse(
            mm.id,
            m.id,
            s.name,
            m.conditional,
            m.point,
            m.duration,
            mm.isComplete
        )
        from MemberMission mm
        join mm.mission m
        join m.store s
        where mm.member.id = :memberId
        and (:status is null or
        (:status = 'ONGOING' and mm.isComplete = false) or
        (:status = 'COMPLETED' and mm.isComplete = true))
        order by m.createdAt desc
    """)
    Page<MemberMissionResponse> findeMemberMissionByMemeberId(
        @Param("memberId") Long memberId,
        Pageable pageable
    );
### api 명세서
    {
        "missions": [
            {
                "memberMissionId": 55,
                "missionId": 101,
                "storeName": "가게이름A",
                "conditional": "12,000원 이상의 식사를 하세요!",
                "point": 500,
                "duration": "2025-09-01",
                "isComplete": false
            },
            {
                "memberMissionId": 56,
                "missionId": 102,
                "storeName": "가게이름B",
                "conditional": "10,000원 이상 주문 시",
                "point": 300,
                "duration": "2025-09-03",
                "isComplete": true
            }
        ],
        "pageInfo": {
            "page": 0,
            "size": 5,
            "totalPages": 3,
            "totalElements": 15
        }
    }

## 지역별 미션 조회 
### 쿼리문
    @Query("""
        select new com.example.umc9th.domain.mission.dto.MissionHomeResponse(
            m.id,
            l.name,
            s.name,
            m.conditional,
            m.point,
            m.duration
        )
        from Mission m
        join m.store s
        join s.location l
        where l.id = :locationId
        order by m.createdAt desc
    """)
    Page<MissionHomeResponse> findMissionsByLocationId(
        @Param("locationId") Long locationId,
        Pageable pageable
    );
### api 명세서
    {
        "content": [
        {
            "missionId": 12,
            "locationName": "서울특별시 노원구 월계동",
            "storeName": "가게이름A",
            "conditional": "12,000원 이상 주문 시",
            "point": 500,
            "duration": "2025-09-07"
        },
        {
            "missionId": 13,
            "locationName": "서울특별시 노원구 월계동",
            "storeName": "가게이름B",
            "conditional": "10,000원 이상 주문 시",
            "point": 300,
            "duration": "2025-09-08"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10
    },
    "totalElements": 2,
    "totalPages": 1
    }

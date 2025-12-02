# Week 9 - 하지명

## 깃허브 링크
[9주차 미션 링크](https://github.com/hajimeong/UMC_Study_Mission/tree/Feat/Chapter9)

### Mission
1. 내가 작성한 리뷰 목록
```java
@GetMapping("/reviews/me")
    @Operation(summary = "내 리뷰 조회")
    public Page<ReviewDto> getReviews(
            @ValidPage @Parameter(description = "페이지번호 1부터")Integer page
    ){
        return reviewService.getMyReviews(1L,page);
    }
```
2. 특정 가게의 미션 목록
```java
@GetMapping("/stores/{storeId}/missions")
    @Operation(summary = "가게 미션 조회")
    public Page<MissionDto> getStoreMissions(
            @PathVariable Long storeId,
            @ValidPage Integer page
    ){
        return missionService.getStoreMissions(storeId, page);
    }
```
3. 내가 진행 중인 미션 목록
```java
@GetMapping("/missions/me")
    @Operation(summary="내 진행중인 미션 조회")
    public Page<MissionDto> getMyOngoingMissions(
            @ValidPage Integer page
    ){
        return missionService.getMyOngoingMissions(1L, page);
    }

```
4. 진행 중인 미션 진행 완료로 바꾸기
```java
@GetMapping("/missions/{missionId}/complete")
    @Operation(summary = "미션 완료 처리")
    public MissionDto completeMission(@PathVariable Long missionId){
        return missionService.completeMission(missionId);
    }
```
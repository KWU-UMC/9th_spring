# week 8 

### 깃허브 링크
[8주차](https://github.com/hajimeong/UMC_Study_Mission/tree/Feat/Chapter8)

### 미션
1. 특정 지역에 가게 추가하기 API
2. 가게에 리뷰 추가하기 API
3. 가게에 미션 추가하기 API
4. 가게의 미션을 도전 중인 미션에 추가(미션 도전하기) API

### 구현 사항
각 API에 필요한 dto, service, controller 모두 구현

## 1. 특정 지역에 가게 추가 API
### POST /regions/{regionId}/stores
```java
//특정 지역에 가게 추가 Controller
@RestController
@RequestMapping("/regions/{regionId}/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;


    @PostMapping
    public ResponseEntity<StoreDto> addStore(
            @PathVariable Long regionId,
            @RequestBody StoreDto storeDto
    ){
        StoreDto addStore=storeService.addStore(regionId,storeDto);
        return ResponseEntity.ok(addStore);
    }
}

```

## 2. 가게에 리뷰 추가 API
### POST /stores/{storeId}/reviews
```java
@PostMapping
    public ResponseEntity<ReviewDto> addReview(
            @PathVariable Long storeId,
            @RequestBody ReviewDto reviewDto
    ){
        ReviewDto addReview=reviewService.addReview(storeId, reviewDto);
        return ResponseEntity.ok(addReview);
    }
```

## 3. 가게에 미션 추가하기 API
### POST /stores/{storeId}/missions
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/missions")
public class MissionController {

    private final MissionService missionService;

    //특정 가게에 mission 추가 api
    @PostMapping
    public ResponseEntity<MissionDto> addMission(
            @PathVariable Long storeId,
            @RequestBody MissionDto missionDto
    ){
        MissionDto created=missionService.addMission(storeId,missionDto);
        return ResponseEntity.ok(created);
    }
}
```

## 4. 가게의 미션을 도전 중인 미션에 추가 
### POST /missions/{missionId}/challenge
```java
@PostMapping
    public ResponseEntity<MissionChallengeDto> challengeMission(
            @PathVariable Long missionId,
            @RequestBody MissionChallengeDto dto
    ) {
        MissionChallengeDto result = missionChallengeService.challengeMission(missionId, dto);
        return ResponseEntity.ok(result);
    }
```

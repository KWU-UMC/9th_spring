package com.example.Feat.Chapter4.domain.mission.entity;

import com.example.Feat.Chapter4.domain.mission.enums.MissionState;
import com.example.Feat.Chapter4.domain.store.entity.Store;
import com.example.Feat.Chapter4.domain.user.entity.User;
import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user_mission")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class UserMission extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Enumerated(STRING)
    @Column(name = "state", length = 20, nullable = false)
    private MissionState state;

    private LocalDateTime acceptedTime;
    private LocalDateTime completedTime;

    @Column(name = "review_written")
    private Boolean reviewWritten;

    @OneToOne(mappedBy = "userMission")
    private com.example.Feat.Chapter4.domain.review.entity.Review review;
}

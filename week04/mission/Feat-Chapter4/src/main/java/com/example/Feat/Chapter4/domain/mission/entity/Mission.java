package com.example.Feat.Chapter4.domain.mission.entity;

import com.example.Feat.Chapter4.domain.store.entity.Store;
import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "mission")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class Mission extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "reward")
    private Long reward;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "mission")
    private List<UserMission> userMissions = new ArrayList<>();
}
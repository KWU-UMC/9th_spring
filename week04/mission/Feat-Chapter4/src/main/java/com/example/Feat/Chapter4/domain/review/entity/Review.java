package com.example.Feat.Chapter4.domain.review.entity;

import com.example.Feat.Chapter4.domain.mission.entity.UserMission;
import com.example.Feat.Chapter4.domain.store.entity.Store;
import com.example.Feat.Chapter4.domain.user.entity.User;
import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY) @JoinColumn(name = "user_mission_id", nullable = false, unique = true)
    private UserMission userMission;

    // 조회 편의를 위해 유지(ERD에도 존재)
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @OneToMany(mappedBy = "review")
    private List<Reply> replies = new ArrayList<>();
}

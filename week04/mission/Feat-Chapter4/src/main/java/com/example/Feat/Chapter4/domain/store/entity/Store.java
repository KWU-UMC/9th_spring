package com.example.Feat.Chapter4.domain.store.entity;

import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class Store extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @Column(name = "owner_id")
    private Long ownerId; // 필요 시 User 연관으로 변경 가능

    @Column(name = "address", length = 20)
    private String address;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "store")
    private List<com.example.Feat.Chapter4.domain.mission.entity.Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<com.example.Feat.Chapter4.domain.mission.entity.UserMission> userMissions = new ArrayList<>();
}
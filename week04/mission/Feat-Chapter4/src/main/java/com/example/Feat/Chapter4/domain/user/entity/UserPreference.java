package com.example.Feat.Chapter4.domain.user.entity;

import com.example.Feat.Chapter4.domain.food.entity.FoodCategory;
import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "user_preference")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class UserPreference extends BaseTimeEntity {

    @EmbeddedId
    private UserPreferenceId id;

    @MapsId("userId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("foodId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodCategory food;
}

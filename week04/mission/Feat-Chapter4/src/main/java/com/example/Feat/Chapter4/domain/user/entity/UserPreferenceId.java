package com.example.Feat.Chapter4.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class UserPreferenceId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "food_id")
    private Long foodId;

    public UserPreferenceId(Long userId, Long foodId) {
        this.userId = userId;
        this.foodId = foodId;
    }
}
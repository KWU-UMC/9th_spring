package com.example.Feat.Chapter4.domain.user.entity;

import com.example.Feat.Chapter4.domain.user.enums.Gender;
import com.example.Feat.Chapter4.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "phone_number", length = 15, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "name", length = 10, nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "gender", length = 10, nullable = false)
    private Gender gender;

    @Column(name = "birthday", length = 20)
    private String birthday;

    @Column(name = "address", length = 60)
    private String address;

    @Column(name = "total_point")
    private Long totalPoint;

    @Column(name = "location_agreement")
    private Boolean locationAgreement;

    @Column(name = "alarm_agreement")
    private Boolean alarmAgreement;

    @OneToMany(mappedBy = "user")
    private List<UserPreference> preferences = new ArrayList<>();
}

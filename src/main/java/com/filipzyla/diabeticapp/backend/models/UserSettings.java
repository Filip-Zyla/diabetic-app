package com.filipzyla.diabeticapp.backend.models;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class UserSettings {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarUnits units;

    @Column(nullable = false, scale = 1)
    private Double sugarEmptyStomach;

    @Column(nullable = false, scale = 1)
    private Double sugarBeforeMeal;

    @Column(nullable = false, scale = 1)
    private Double sugarAfterMeal;
}

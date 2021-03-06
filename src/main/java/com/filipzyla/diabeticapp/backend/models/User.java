package com.filipzyla.diabeticapp.backend.models;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.utility.SugarDefaultSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarUnits units;

    @Column(nullable = false, scale = 1)
    private Integer hypoglycemia;

    @Column(nullable = false, scale = 1)
    private Integer hyperglycemia;

    @Column(nullable = false, scale = 1)
    private Integer hyperglycemiaAfterMeal;

    public User(String username, String pass, String email) {
        this.email = email;
        this.username = username;
        password = pass;
        hypoglycemia = SugarDefaultSettings.DEFAULT_HYPOGLYCEMIA;
        hyperglycemia = SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA;
        hyperglycemiaAfterMeal = SugarDefaultSettings.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL;
        units = SugarDefaultSettings.DEFAULT_UNITS;
    }
}
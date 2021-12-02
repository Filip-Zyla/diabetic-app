package com.filipzyla.diabeticapp.backend.models;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import com.filipzyla.diabeticapp.backend.utility.SugarDefaultBoundaries;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
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
    private Double hypoglycemia;

    @Column(nullable = false, scale = 1)
    private Double hyperglycemia;

    @Column(nullable = false, scale = 1)
    private Double hyperglycemiaAfterMeal;

    public User(String username, String pass, String email) {
        this.email = email;
        this.username = username;
        password = pass;
        hypoglycemia = SugarDefaultBoundaries.DEFAULT_HYPOGLYCEMIA;
        hyperglycemia = SugarDefaultBoundaries.DEFAULT_HYPERGLYCEMIA;
        hyperglycemiaAfterMeal = SugarDefaultBoundaries.DEFAULT_HYPERGLYCEMIA_AFTER_MEAL;
        units = SugarDefaultBoundaries.DEFAULT_UNITS;
    }
}
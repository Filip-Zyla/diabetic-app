package com.filipzyla.diabeticapp.backend.models;

import com.filipzyla.diabeticapp.backend.enums.SugarType;
import com.filipzyla.diabeticapp.backend.enums.SugarUnits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "sugar_unique", columnNames = {"id"})
})
public class Sugar {

    @Id
    @SequenceGenerator(name = "sugar_id_sequence", sequenceName = "sugar_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sugar_id_sequence")
    private Long id;

    @Column(nullable = false, scale = 1)
    private Integer sugar;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarUnits units;

    @Column(nullable = false)
    private LocalDateTime time;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Sugar(Integer sugar, SugarType type, LocalDateTime time, String note, User user) {
        this.sugar = sugar;
        this.type = type;
        units = SugarUnits.MILLI_GRAM;
        this.time = time;
        this.note = note;
        this.user = user;
    }
}
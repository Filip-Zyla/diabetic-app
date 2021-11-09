package com.filipzyla.diabeticapp.suger;

import com.filipzyla.diabeticapp.Enums.SugarType;
import com.filipzyla.diabeticapp.Enums.SugarUnits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity(name = "sugar")
@Table(name = "sugar", uniqueConstraints = {@UniqueConstraint(name = "sugar_unique", columnNames = "id")})
public class Sugar {

    @Id
    @SequenceGenerator(name = "sugar_id_sequence", sequenceName = "sugar_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sugar_id_sequence")
    private Long id;

    @Column(name = "sugar", nullable = false)
    private Double sugar;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private SugarType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "units", nullable = false)
    private SugarUnits units;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "note")
    private String note;

    public Sugar(Double sugar, SugarType type, SugarUnits units, LocalDateTime time, String note) {
        this.sugar = sugar;
        this.type = type;
        this.units = units;
        this.time = time;
        this.note = note;
    }
}

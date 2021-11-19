package com.filipzyla.diabeticapp.suger;

import com.filipzyla.diabeticapp.enums.SugarType;
import com.filipzyla.diabeticapp.enums.SugarUnits;
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
@Entity()
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "sugar_unique", columnNames = "id")
})
public class Sugar {

    @Id
    @SequenceGenerator(name = "sugar_id_sequence", sequenceName = "sugar_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sugar_id_sequence")
    private Long id;

    @Column(nullable = false)
    private Double sugar;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private SugarUnits units;

    @Column(nullable = false)
    private LocalDateTime time;

    public Sugar(Double sugar, SugarType type, SugarUnits units, LocalDateTime time) {
        this.sugar = sugar;
        this.type = type;
        this.units = units;
        this.time = time;
    }
}
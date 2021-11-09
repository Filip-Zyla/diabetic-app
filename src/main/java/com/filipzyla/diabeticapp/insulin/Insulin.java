package com.filipzyla.diabeticapp.insulin;

import com.filipzyla.diabeticapp.Enums.InsulinType;
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
@Entity(name = "insulin")
@Table(name = "insulin", uniqueConstraints = {@UniqueConstraint(name = "insulin_unique", columnNames = "id")})
public class Insulin {

    @Id
    @SequenceGenerator(name = "insulin_id_sequence", sequenceName = "insulin_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insulin_id_sequence")
    private Long id;

    @Column(name = "insulin", nullable = false)
    private Integer insulin;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private InsulinType type;


    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "note")
    private String note;

    public Insulin(Integer insulin, InsulinType type, LocalDateTime time, String note) {
        this.insulin = insulin;
        this.type = type;
        this.time = time;
        this.note = note;
    }
}

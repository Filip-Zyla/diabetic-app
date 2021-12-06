package com.filipzyla.diabeticapp.backend.models;

import com.filipzyla.diabeticapp.backend.enums.InsulinType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "insulin_unique", columnNames = "id")
})
public class Insulin {

    @Id
    @SequenceGenerator(name = "insulin_id_sequence", sequenceName = "insulin_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insulin_id_sequence")
    private Long id;

    @Column(nullable = false)
    private Integer insulin;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private InsulinType type;

    @Column(nullable = false)
    private LocalDateTime time;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Insulin(Integer insulin, InsulinType type, LocalDateTime time, String note, User user) {
        this.insulin = insulin;
        this.type = type;
        this.time = time;
        this.note = note;
        this.user = user;
    }
}
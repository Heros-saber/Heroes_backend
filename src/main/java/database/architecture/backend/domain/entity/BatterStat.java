package database.architecture.backend.domain.entity;

import database.architecture.backend.domain.listener.BatterStatListener;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(BatterStatListener.class)
public class BatterStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statId;

    @Column(nullable = false)
    private Double avg;

    @Column(nullable = false)
    private Double ops;

    @Column(nullable = false)
    private Double slg;

    @Column(nullable = false)
    private Double obp;

    @Column(nullable = false)
    private Double wrcPlus;

    @Column(nullable = false)
    private Double war;

    @Column(nullable = false)
    private Integer pa;

    @Column(nullable = false)
    private Integer ab;

    @Column(nullable = false)
    private Integer hit;

    @Column(nullable = false)
    private Integer h_double;

    @Column(nullable = false)
    private Integer triple;

    @Column(nullable = false)
    private Integer hr;

    @Column(nullable = false)
    private Integer sb;

    @Column(nullable = false)
    private Integer rbi;

    @Column(nullable = false)
    private Integer bb;

    @Column(nullable = false)
    private Integer so;

    @Column(nullable = false)
    private String year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;
}

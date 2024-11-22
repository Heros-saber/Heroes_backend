package database.architecture.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BatterStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statId;

    @Column(nullable = false)
    private double avg;

    @Column(nullable = false)
    private double ops;

    @Column(nullable = false)
    private double slg;

    @Column(nullable = false)
    private double obp;

    @Column(nullable = false)
    private double wrc_plus;

    @Column(nullable = false)
    private double war;

    @Column(nullable = false)
    private int pa;

    @Column(nullable = false)
    private int ab;

    @Column(nullable = false)
    private int hit;

    @Column(nullable = false)
    private int h_double;

    @Column(nullable = false)
    private int triple;

    @Column(nullable = false)
    private int hr;

    @Column(nullable = false)
    private int sb;

    @Column(nullable = false)
    private int rbi;

    @Column(nullable = false)
    private int bb;

    @Column(nullable = false)
    private int so;

    @Column(nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;
}

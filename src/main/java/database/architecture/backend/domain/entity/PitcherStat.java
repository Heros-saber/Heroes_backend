package database.architecture.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PitcherStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statId;

    @Column(nullable = false)
    private int games;

    @Column(nullable = false)
    private int win;

    @Column(nullable = false)
    private int lose;

    @Column(nullable = false)
    private int save;

    @Column(nullable = false)
    private int hold;

    @Column(nullable = false)
    private int tbf;

    @Column(nullable = false)
    private double ip;

    @Column(nullable = false)
    private double era;

    @Column(nullable = false)
    private int er;

    @Column(nullable = false)
    private int so;

    @Column(nullable = false)
    private int bb;

    @Column(nullable = false)
    private int hit;

    @Column(nullable = false)
    private int h_double;

    @Column(nullable = false)
    private int triple;

    @Column(nullable = false)
    private int hr;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private double war;

    @Column(nullable = false)
    private double whip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;

}

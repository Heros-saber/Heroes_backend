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
    private Integer statId;

    @Column(nullable = false)
    private Integer games;

    @Column(nullable = false)
    private Integer win;

    @Column(nullable = false)
    private Integer lose;

    @Column(nullable = false)
    private Integer save;

    @Column(nullable = false)
    private Integer hold;

    @Column(nullable = false)
    private Integer tbf;

    @Column(nullable = false)
    private Double ip;

    @Column(nullable = false)
    private Double era;

    @Column(nullable = false)
    private Integer er;

    @Column(nullable = false)
    private Integer so;

    @Column(nullable = false)
    private Integer bb;

    @Column(nullable = false)
    private Integer hit;

    @Column(nullable = false)
    private Integer h_double;

    @Column(nullable = false)
    private Integer triple;

    @Column(nullable = false)
    private Integer hr;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private Double war;

    @Column(nullable = false)
    private Double whip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;

}

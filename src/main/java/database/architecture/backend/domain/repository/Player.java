package database.architecture.backend.domain.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;

    @Column(nullable = false)
    private String playerName;

    @Column(nullable = false)
    private LocalDate playerBorn;

    @Column(nullable = false)
    private LocalDate playerDraft;

    @Column(nullable = false)
    private int playerPos;

    @Column(nullable = false)
    private boolean playerBattingSide;

    @Column(nullable = false)
    private boolean playerThrowSide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team playerTeam;

    @OneToMany(mappedBy = "pitcherRecord")
    private List<PitcherStat> pitcherStatList;

    @OneToMany(mappedBy = "batterRecord")
    private List<BatterStat> batterStatList;
}

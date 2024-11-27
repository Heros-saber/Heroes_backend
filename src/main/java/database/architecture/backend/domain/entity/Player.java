package database.architecture.backend.domain.entity;

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
    private Integer playerId;

    @Column(nullable = false)
    private String playerName;

    @Column(nullable = false)
    private LocalDate playerBorn;

    @Column(nullable = false)
    private String playerDraft;

    @Column(nullable = false)
    private Integer playerPos;

    @Column(nullable = false)
    private Boolean playerBattingSide;

    @Column(nullable = false)
    private Boolean playerThrowSide;

    private String playerImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @OneToMany(mappedBy = "player")
    private List<PitcherStat> pitcherStat;

    @OneToMany(mappedBy = "player")
    private List<BatterStat> batterStat;

    @OneToMany(mappedBy = "player")
    private List<PitcherZoneStat> pitcherZoneStat;

    @OneToMany(mappedBy = "player")
    private List<BatterZoneStat> batterZoneStat;
    public void updateTeam(Team playerTeam){
        this.team = playerTeam;
    }
}

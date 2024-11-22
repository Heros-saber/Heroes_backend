package database.architecture.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer day;

    private Integer heroesScore;

    private Integer oppoScore;

    private Boolean win;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    public void update(int year, int month, int day, int heroesScore, int oppoScore){
        this.year = year;
        this.month = month;
        this.day = day;
        this.heroesScore = heroesScore;
        this.oppoScore = oppoScore;

    }
}

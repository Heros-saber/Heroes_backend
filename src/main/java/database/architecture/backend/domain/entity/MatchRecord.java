package database.architecture.backend.domain.entity;

import database.architecture.backend.domain.crawling.dto.game.GameResultDTO;
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

    public void updateMatchRecord(GameResultDTO dto){
        int year = dto.getDate().getYear();
        int month = dto.getDate().getMonthValue();
        int day = dto.getDate().getDayOfMonth();
        int heroesScore = dto.getKiwoomScore();
        int opponentScore = dto.getOpponentScore();

        this.year = year;
        this.month = month;
        this.day = day;
        this.heroesScore = heroesScore;
        this.oppoScore = opponentScore;

        if(this.heroesScore == null && this.oppoScore == null)
            win = null;
        else if(this.heroesScore > opponentScore)
            win = true;
        else
            win = false;
    }
}

package database.architecture.backend.domain.crawling.dto.game;

import database.architecture.backend.domain.entity.MatchRecord;
import database.architecture.backend.domain.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GameResultDTO {
    private LocalDate date;
    private String opponentTeam;
    private Integer opponentScore;
    private Integer kiwoomScore;

    @Override
    public String toString() {
        return "GameData{" +
                "date='" + date + '\'' +
                ", opponentTeam='" + opponentTeam + '\'' +
                ", opponentScore=" + opponentScore +
                ", kiwoomScore=" + kiwoomScore +
                '}';
    }

    public MatchRecord toEntity(Team team){
        int year = date.getYear();
        int monthValue = date.getMonthValue();
        int day = date.getDayOfMonth();
        return MatchRecord.builder().year(year).month(monthValue).day(day).team(team).win((kiwoomScore > opponentScore))
                                    .oppoScore(opponentScore).heroesScore(kiwoomScore).build();
    }
}


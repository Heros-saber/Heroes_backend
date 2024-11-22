package database.architecture.backend.domain.crawling.dto;

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
    private int opponentScore;
    private int kiwoomScore;

    @Override
    public String toString() {
        return "GameData{" +
                "date='" + date + '\'' +
                ", opponentTeam='" + opponentTeam + '\'' +
                ", opponentScore=" + opponentScore +
                ", kiwoomScore=" + kiwoomScore +
                '}';
    }
}


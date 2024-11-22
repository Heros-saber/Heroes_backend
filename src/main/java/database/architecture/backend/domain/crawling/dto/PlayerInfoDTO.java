package database.architecture.backend.domain.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
public class PlayerInfoDTO {
    private String playerTeam;
    private LocalDate playerBorn;
    private String playerDraft;
    private String playerPos;
    private boolean playerBattingSide;
    private boolean playerThrowSide;
}

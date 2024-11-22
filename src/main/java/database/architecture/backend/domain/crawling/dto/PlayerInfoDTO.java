package database.architecture.backend.domain.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerInfoDTO {
    private String playerTeam;
    private String playerBorn;
    private String playerDraft;
    private String playerPos;
    private boolean playerBattingSide;
    private boolean playerThrowSide;
}

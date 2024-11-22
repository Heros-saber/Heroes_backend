package database.architecture.backend.domain.crawling.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TeamRankDTO {
    private int rank;
    private int win;
    private int lose;
    private int draw;
}

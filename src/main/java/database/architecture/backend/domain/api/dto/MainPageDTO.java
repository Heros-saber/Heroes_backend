package database.architecture.backend.domain.api.dto;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MainPageDTO {

    private int rank;
    private int win;
    private int lose;
    private String nextGame;
}

package database.architecture.backend.domain.crawling.dto.pitcher;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PitcherZoneDTO {
    private String circumstance;
    private String label;
    private List<Double> data;
}

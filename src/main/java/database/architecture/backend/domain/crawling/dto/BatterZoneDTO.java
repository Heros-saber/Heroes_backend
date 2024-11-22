package database.architecture.backend.domain.crawling.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatterZoneDTO {
    private String circumstance;
    private String label;
    private List<Double[]> data;
}

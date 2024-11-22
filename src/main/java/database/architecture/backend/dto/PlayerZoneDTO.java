package database.architecture.backend.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerZoneDTO {
    private String circum;
    private String label;
    private List<Double[]> data;
}

package database.architecture.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HeroesRecord {
    @Id
    @Column(nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Integer win;
    @Column(nullable = false)
    private Integer lose;
    @Column(nullable = false)
    private Integer draw;
    @Column(nullable = false)
    private Integer rank;
}

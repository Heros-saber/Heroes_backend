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
    private int year;
    @Column(nullable = false)
    private int win;
    @Column(nullable = false)
    private int lose;
    @Column(nullable = false)
    private int draw;
    @Column(nullable = false)
    private int rank;
}

package database.architecture.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String teamHometown;

    @Column(nullable = false)
    private LocalDate teamFounded;

    @OneToMany(mappedBy = "playerTeam")
    private List<Player> teamPlayers;

    @OneToMany(mappedBy = "team")
    private List<MatchRecord> teamMatch;
}
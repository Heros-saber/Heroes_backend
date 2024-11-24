package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerInfoRepository extends JpaRepository<Player, Long> {

    Player findPlayerByPlayerName(String playerName);
}

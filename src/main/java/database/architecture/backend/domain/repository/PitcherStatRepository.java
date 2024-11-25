package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.PitcherStat;
import database.architecture.backend.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PitcherStatRepository extends JpaRepository<PitcherStat, Long> {

    List<PitcherStat> findAllByPlayer(Player player);

    PitcherStat findPitcherStatByPlayerAndYear(Player player, String year);
}

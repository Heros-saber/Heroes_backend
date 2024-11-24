package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.PitcherZoneStat;
import database.architecture.backend.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PitcherZoneStatRepository extends JpaRepository<PitcherZoneStat, Long> {
    PitcherZoneStat findPitcherZoneStatByPlayerAndCircumstanceAndTag(Player player, String circum, String tag);
    void deletePitcherZoneStatByPlayerAndAndCircumstanceAndTag(Player player, String circum, String tag);

    List<PitcherZoneStat> findAllByPlayer(Player player);
}

package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.BatterZoneStat;
import database.architecture.backend.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatterZoneStatRepository extends JpaRepository<BatterZoneStat, Long> {
    BatterZoneStat findBatterZoneStatByPlayerAndCircumstanceAndTag(Player player,String circum, String tag);
    void deleteBatterZoneStatByPlayerAndAndCircumstanceAndTag(Player player, String circum, String tag);
}

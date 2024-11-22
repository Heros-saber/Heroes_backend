package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.PitcherZoneStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PitcherZoneStatRepository extends JpaRepository<PitcherZoneStat, Long> {
}

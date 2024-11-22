package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.BatterZoneStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatterZoneStatRepository extends JpaRepository<BatterZoneStat, Long> {
}

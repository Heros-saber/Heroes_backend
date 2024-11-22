package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.PitcherStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PitcherStatRepository extends JpaRepository<PitcherStat, Long> {
}

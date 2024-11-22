package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.BatterStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatterStatRepository extends JpaRepository<BatterStat, Long> {
}

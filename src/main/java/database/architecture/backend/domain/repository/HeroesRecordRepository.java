package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.HeroesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroesRecordRepository extends JpaRepository<HeroesRecord, Long> {
}

package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.HeroesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HeroesRecordRepository extends JpaRepository<HeroesRecord, Long> {
    HeroesRecord findHeroesRecordByYear(int year);
}

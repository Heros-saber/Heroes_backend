package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.MatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {
    List<MatchRecord> findAllByYearAndMonth(Integer year, Integer month);
}

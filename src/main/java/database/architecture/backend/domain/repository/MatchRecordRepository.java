package database.architecture.backend.domain.repository;

import database.architecture.backend.domain.entity.MatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {
    List<MatchRecord> findAllByYearAndMonth(Integer year, Integer month);
    List<MatchRecord> findAllByYearAndMonthAndDayAfterOrderByDay(int year, Integer month, int day);
    List<MatchRecord> findByYearAndMonthOrderByDayAsc(int year, int month);
}

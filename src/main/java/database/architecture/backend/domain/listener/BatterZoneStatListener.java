package database.architecture.backend.domain.listener;

import database.architecture.backend.domain.entity.BatterZoneStat;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BatterZoneStatListener { // 각 존에서 타율을 계산하여 1.0을 초과하면 예외가 발생

    @PrePersist
    @PreUpdate
    public void validateBattingAverage(BatterZoneStat zoneStat) {
        if ("타율".equals(zoneStat.getTag()) && calculateBattingAverage(zoneStat) > 1.0) {
            throw new IllegalArgumentException("Batting average cannot exceed 1.0 when tag is '타율'");
        }
    }

    private double calculateBattingAverage(BatterZoneStat zoneStat) { // 계산된 타율이 1.0을 초과하면 예외가 발생
        Double[] zones = {
                zoneStat.getZone1(), zoneStat.getZone2(), zoneStat.getZone3(), zoneStat.getZone4(), zoneStat.getZone5(),
                zoneStat.getZone6(), zoneStat.getZone7(), zoneStat.getZone8(), zoneStat.getZone9(), zoneStat.getZone10(),
                zoneStat.getZone11(), zoneStat.getZone12(), zoneStat.getZone13(), zoneStat.getZone14(), zoneStat.getZone15(),
                zoneStat.getZone16(), zoneStat.getZone17(), zoneStat.getZone18(), zoneStat.getZone19(), zoneStat.getZone20(),
                zoneStat.getZone21(), zoneStat.getZone22(), zoneStat.getZone23(), zoneStat.getZone24(), zoneStat.getZone25()
        };

        double sum = 0.0;
        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            }
        }

        return sum / 25;
    }
}
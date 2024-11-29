package database.architecture.backend.domain.listener;

import database.architecture.backend.domain.entity.PitcherZoneStat;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class PitcherZoneStatListener {

    @PrePersist
    @PreUpdate
    public void validateCircumstance(PitcherZoneStat zoneStat) {
        if (zoneStat.getCircumstance() == null || zoneStat.getCircumstance().isBlank()) {
            zoneStat.updateCircum("DEFAULT"); // 기본값 설정
        }

        // 구사율인 경우 sum이 100이 되도록 보정
        if ("구사율".equals(zoneStat.getTag())) {
            adjustZoneValues(zoneStat);
        }
    }

    @PostPersist
    public void logCreation(PitcherZoneStat zoneStat) {
        System.out.println("New PitcherZoneStat created: " + zoneStat.getZoneStatId());
    }

    @PostUpdate
    public void logUpdate(PitcherZoneStat zoneStat) {
        System.out.println("PitcherZoneStat updated: zoneAvg = " + zoneStat.zoneAvg());
    }

    private void adjustZoneValues(PitcherZoneStat zoneStat) {
        // 모든 zone 값을 배열로 가져오기
        Double[] zones = {
                zoneStat.getZone1(), zoneStat.getZone2(), zoneStat.getZone3(), zoneStat.getZone4(), zoneStat.getZone5(),
                zoneStat.getZone6(), zoneStat.getZone7(), zoneStat.getZone8(), zoneStat.getZone9(), zoneStat.getZone10(),
                zoneStat.getZone11(), zoneStat.getZone12(), zoneStat.getZone13(), zoneStat.getZone14(), zoneStat.getZone15(),
                zoneStat.getZone16(), zoneStat.getZone17(), zoneStat.getZone18(), zoneStat.getZone19(), zoneStat.getZone20(),
                zoneStat.getZone21(), zoneStat.getZone22(), zoneStat.getZone23(), zoneStat.getZone24(), zoneStat.getZone25()
        };

        // 현재 합계 계산
        double currentSum = 0.0;
        for (Double zone : zones) {
            if (zone != null) {
                currentSum += zone;
            }
        }

        // 보정 비율 계산
        double adjustmentFactor = currentSum > 0 ? 100.0 / currentSum : 1.0;

        // zone 값 보정
        for (int i = 0; i < zones.length; i++) {
            if (zones[i] != null) {
                zones[i] = Math.round(zones[i] * adjustmentFactor * 1000.0) / 1000.0;
            }
        }

        // 보정된 값을 다시 설정
        zoneStat.setZone1(zones[0]); zoneStat.setZone2(zones[1]); zoneStat.setZone3(zones[2]);
        zoneStat.setZone4(zones[3]); zoneStat.setZone5(zones[4]); zoneStat.setZone6(zones[5]);
        zoneStat.setZone7(zones[6]); zoneStat.setZone8(zones[7]); zoneStat.setZone9(zones[8]);
        zoneStat.setZone10(zones[9]); zoneStat.setZone11(zones[10]); zoneStat.setZone12(zones[11]);
        zoneStat.setZone13(zones[12]); zoneStat.setZone14(zones[13]); zoneStat.setZone15(zones[14]);
        zoneStat.setZone16(zones[15]); zoneStat.setZone17(zones[16]); zoneStat.setZone18(zones[17]);
        zoneStat.setZone19(zones[18]); zoneStat.setZone20(zones[19]); zoneStat.setZone21(zones[20]);
        zoneStat.setZone22(zones[21]); zoneStat.setZone23(zones[22]); zoneStat.setZone24(zones[23]);
        zoneStat.setZone25(zones[24]);
    }
}

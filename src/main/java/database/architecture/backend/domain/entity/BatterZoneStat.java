package database.architecture.backend.domain.entity;

import database.architecture.backend.domain.listener.BatterZoneStatListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(BatterZoneStatListener.class)
public class BatterZoneStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zoneStatId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String circumstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;

    private Double zone1;
    private Double zone2;
    private Double zone3;
    private Double zone4;
    private Double zone5;
    private Double zone6;
    private Double zone7;
    private Double zone8;
    private Double zone9;
    private Double zone10;
    private Double zone11;
    private Double zone12;
    private Double zone13;
    private Double zone14;
    private Double zone15;
    private Double zone16;
    private Double zone17;
    private Double zone18;
    private Double zone19;
    private Double zone20;
    private Double zone21;
    private Double zone22;
    private Double zone23;
    private Double zone24;
    private Double zone25;

    public void updateCircum(String circum){
        this.circumstance = circum;
    }

    public Double zoneAvg() {
        Double sum = 0.0;

        Double[] zones = {zone1, zone2, zone3, zone4, zone5, zone6, zone7, zone8, zone9, zone10,
                zone11, zone12, zone13, zone14, zone15, zone16, zone17, zone18, zone19, zone20,
                zone21, zone22, zone23, zone24, zone25};

        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            } else {
                sum += 0.0;
            }
        }

        return Double.parseDouble(String.format("%.3f", sum / 25));
    }

    public Double leftUpside(){
        Double sum = 0.0;

        Double[] zones = {zone1, zone2, zone3, zone6, zone7, zone8, zone11, zone12, zone13 };

        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            } else {
                sum += 0.0;
            }
        }

        return Double.parseDouble(String.format("%.3f", sum / 9));
    }

    public Double rightUpside(){
        Double sum = 0.0;

        Double[] zones = {zone3, zone4, zone5, zone8, zone9, zone10, zone13, zone14, zone15 };

        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            } else {
                sum += 0.0;
            }
        }

        return Double.parseDouble(String.format("%.3f", sum / 9));
    }

    public Double leftDownside(){
        Double sum = 0.0;

        Double[] zones = {zone11, zone12, zone13, zone16, zone17, zone18, zone21, zone22, zone23 };

        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            } else {
                sum += 0.0;
            }
        }

        return Double.parseDouble(String.format("%.3f", sum / 9));
    }

    public Double rightDownside(){
        Double sum = 0.0;

        Double[] zones = {zone13, zone14, zone15, zone18, zone19, zone20, zone23, zone24, zone25 };

        for (Double zone : zones) {
            if (zone != null) {
                sum += zone;
            } else {
                sum += 0.0;
            }
        }

        return Double.parseDouble(String.format("%.3f", sum / 9));
    }
}

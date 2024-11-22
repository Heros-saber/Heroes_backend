package database.architecture.backend.domain.repository;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BatterZoneStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int zoneStatId;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String circumstance;

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
}

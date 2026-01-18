package com.chamikara.spring_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transformers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transformer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String number;

    private String pole;

    private String region;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String baselineImage;

    private String baselineUploadDate;

    private String weather;

    private String location;

    @OneToMany(mappedBy = "transformer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Inspection> inspections = new ArrayList<>();

    @OneToMany(mappedBy = "transformer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}

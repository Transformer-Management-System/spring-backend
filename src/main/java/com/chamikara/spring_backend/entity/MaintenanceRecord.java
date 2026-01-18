package com.chamikara.spring_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformer_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Transformer transformer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Inspection inspection;

    @Column(nullable = false)
    private String recordTimestamp;

    private String engineerName;

    private String status; // OK / Needs Maintenance / Urgent Attention

    @Column(columnDefinition = "TEXT")
    private String readings; // JSON string of key-value pairs

    @Column(columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String annotatedImage; // snapshot of annotated image

    @Column(columnDefinition = "TEXT")
    private String anomalies; // JSON array of anomaly objects

    private String location; // snapshot of transformer location

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private String updatedAt;
}

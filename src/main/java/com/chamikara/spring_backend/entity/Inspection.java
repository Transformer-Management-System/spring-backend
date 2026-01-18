package com.chamikara.spring_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inspections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformer_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Transformer transformer;

    private String date;

    private String inspectedDate;

    private String inspector;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    private String status = "Pending";

    @Column(columnDefinition = "TEXT")
    private String maintenanceImage;

    private String maintenanceUploadDate;

    private String maintenanceWeather;

    @Column(columnDefinition = "TEXT")
    private String annotatedImage;

    @Column(columnDefinition = "TEXT")
    private String anomalies; // Stored as JSON string

    @Column(columnDefinition = "TEXT")
    private String progressStatus; // Stored as JSON string

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Annotation> annotations = new ArrayList<>();

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AnnotationLog> annotationLogs = new ArrayList<>();
}

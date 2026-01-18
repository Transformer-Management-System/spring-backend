package com.chamikara.spring_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annotation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnotationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Inspection inspection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformer_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Transformer transformer;

    private String imageId; // Reference to the annotated image

    @Column(nullable = false)
    private String actionType; // 'added', 'edited', 'deleted', 'ai_generated'

    @Column(columnDefinition = "TEXT", nullable = false)
    private String annotationData; // JSON string of the annotation state

    @Column(columnDefinition = "TEXT")
    private String aiPrediction; // Original AI prediction (JSON) if applicable

    @Column(columnDefinition = "TEXT")
    private String userAnnotation; // Final user-modified annotation (JSON)

    @Builder.Default
    private String userId = "Admin";

    @Column(nullable = false)
    private String timestamp;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

package com.chamikara.spring_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annotations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Inspection inspection;

    @Column(name = "annotation_id", unique = true, nullable = false)
    private String annotationId; // The unique ID from frontend (e.g., 'ai_1', 'user_123')

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double w;

    @Column(nullable = false)
    private Double h;

    private Double confidence;

    private String severity;

    private String classification;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private String source; // 'ai' or 'user'

    @Builder.Default
    private Boolean deleted = false;

    @Builder.Default
    private String userId = "Admin";

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private String updatedAt;
}

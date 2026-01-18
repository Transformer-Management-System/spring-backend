package com.chamikara.spring_backend.service;

import com.chamikara.spring_backend.dto.request.InspectionRequest;
import com.chamikara.spring_backend.dto.response.InspectionResponse;
import com.chamikara.spring_backend.entity.Inspection;
import com.chamikara.spring_backend.entity.Transformer;
import com.chamikara.spring_backend.exception.ResourceNotFoundException;
import com.chamikara.spring_backend.repository.InspectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InspectionService {
    
    private final InspectionRepository inspectionRepository;
    private final TransformerService transformerService;
    
    public List<InspectionResponse> getAllInspections() {
        log.debug("Fetching all inspections");
        return inspectionRepository.findAllWithTransformer().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public InspectionResponse getInspectionById(Long id) {
        log.debug("Fetching inspection with id: {}", id);
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection", "id", id));
        return mapToResponse(inspection);
    }
    
    public List<InspectionResponse> getInspectionsByTransformerId(Long transformerId) {
        log.debug("Fetching inspections for transformer: {}", transformerId);
        return inspectionRepository.findByTransformerIdOrderByDateDesc(transformerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public InspectionResponse createInspection(InspectionRequest request) {
        log.debug("Creating new inspection for transformer: {}", request.getTransformerId());
        
        Transformer transformer = transformerService.getTransformerEntity(request.getTransformerId());
        
        Inspection inspection = Inspection.builder()
                .transformer(transformer)
                .date(request.getDate())
                .inspectedDate(request.getInspectedDate())
                .inspector(request.getInspector())
                .notes(request.getNotes())
                .status(request.getStatus() != null ? request.getStatus() : "Pending")
                .maintenanceImage(request.getMaintenanceImage())
                .maintenanceUploadDate(request.getMaintenanceUploadDate())
                .maintenanceWeather(request.getMaintenanceWeather())
                .annotatedImage(request.getAnnotatedImage())
                .anomalies(request.getAnomalies())
                .progressStatus(request.getProgressStatus())
                .build();
        
        Inspection saved = inspectionRepository.save(inspection);
        log.info("Created inspection with id: {}", saved.getId());
        return mapToResponse(saved);
    }
    
    public InspectionResponse updateInspection(Long id, InspectionRequest request) {
        log.debug("Updating inspection with id: {}", id);
        
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection", "id", id));
        
        if (request.getTransformerId() != null && 
                !request.getTransformerId().equals(inspection.getTransformer().getId())) {
            Transformer transformer = transformerService.getTransformerEntity(request.getTransformerId());
            inspection.setTransformer(transformer);
        }
        
        if (request.getDate() != null) inspection.setDate(request.getDate());
        if (request.getInspectedDate() != null) inspection.setInspectedDate(request.getInspectedDate());
        if (request.getInspector() != null) inspection.setInspector(request.getInspector());
        if (request.getNotes() != null) inspection.setNotes(request.getNotes());
        if (request.getStatus() != null) inspection.setStatus(request.getStatus());
        if (request.getMaintenanceImage() != null) inspection.setMaintenanceImage(request.getMaintenanceImage());
        if (request.getMaintenanceUploadDate() != null) inspection.setMaintenanceUploadDate(request.getMaintenanceUploadDate());
        if (request.getMaintenanceWeather() != null) inspection.setMaintenanceWeather(request.getMaintenanceWeather());
        if (request.getAnnotatedImage() != null) inspection.setAnnotatedImage(request.getAnnotatedImage());
        if (request.getAnomalies() != null) inspection.setAnomalies(request.getAnomalies());
        if (request.getProgressStatus() != null) inspection.setProgressStatus(request.getProgressStatus());
        
        Inspection updated = inspectionRepository.save(inspection);
        log.info("Updated inspection with id: {}", id);
        return mapToResponse(updated);
    }
    
    public void deleteInspection(Long id) {
        log.debug("Deleting inspection with id: {}", id);
        
        if (!inspectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inspection", "id", id);
        }
        
        inspectionRepository.deleteById(id);
        log.info("Deleted inspection with id: {}", id);
    }
    
    public Inspection getInspectionEntity(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspection", "id", id));
    }
    
    private InspectionResponse mapToResponse(Inspection inspection) {
        return InspectionResponse.builder()
                .id(inspection.getId())
                .transformerId(inspection.getTransformer().getId())
                .transformerNumber(inspection.getTransformer().getNumber())
                .date(inspection.getDate())
                .inspectedDate(inspection.getInspectedDate())
                .inspector(inspection.getInspector())
                .notes(inspection.getNotes())
                .status(inspection.getStatus())
                .maintenanceImage(inspection.getMaintenanceImage())
                .maintenanceUploadDate(inspection.getMaintenanceUploadDate())
                .maintenanceWeather(inspection.getMaintenanceWeather())
                .annotatedImage(inspection.getAnnotatedImage())
                .anomalies(inspection.getAnomalies())
                .progressStatus(inspection.getProgressStatus())
                .build();
    }
}

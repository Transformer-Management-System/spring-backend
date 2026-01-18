# Thermal Inspection Automation System - Backend API

A Spring Boot backend service for the Thermal Inspection Automation System that manages transformers, inspections, annotations, and maintenance records.

## Technology Stack

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL 15
- **Build Tool**: Maven

## Prerequisites

- JDK 21 or later
- Docker and Docker Compose (for PostgreSQL)
- Maven (or use the included wrapper)

## Quick Start

### 1. Start the Database

```bash
docker-compose up -d
```

This starts a PostgreSQL container with:
- **Host**: localhost
- **Port**: 5432
- **Database**: transformer_db
- **Username**: admin
- **Password**: password123

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

The server will start at `http://localhost:8080`

## API Endpoints

### Health Check
- `GET /` - Service information
- `GET /health` - Health status
- `GET /api/info` - API documentation

### Transformers
- `GET /transformers` - List all transformers
- `GET /transformers/{id}` - Get transformer by ID
- `POST /transformers` - Create new transformer
- `PUT /transformers/{id}` - Update transformer
- `DELETE /transformers/{id}` - Delete transformer

### Inspections
- `GET /inspections` - List all inspections
- `GET /inspections/{id}` - Get inspection by ID
- `GET /inspections/transformer/{transformerId}` - Get inspections for a transformer
- `POST /inspections` - Schedule new inspection
- `PUT /inspections/{id}` - Update inspection (status, images, etc.)
- `DELETE /inspections/{id}` - Delete inspection

### Annotations
- `GET /annotations/{inspectionId}` - Get annotations for an inspection
- `POST /annotations/{inspectionId}` - Save annotations for an inspection

### Annotation Logs (Feedback Loop)
- `GET /annotation-logs` - List all annotation logs
- `GET /annotation-logs/inspection/{inspectionId}` - Get logs for an inspection
- `GET /annotation-logs/export/json` - Export logs as JSON
- `GET /annotation-logs/export/csv` - Export logs as CSV

### Maintenance Records
- `GET /records` - List all records (optional: ?transformer_id={id})
- `GET /records/{id}` - Get record by ID
- `GET /records/transformer/{transformerId}` - Get records for a transformer
- `POST /records` - Create new record
- `PUT /records/{id}` - Update record
- `DELETE /records/{id}` - Delete record
- `GET /records/export/pdf/{id}` - Export record as PDF

### Anomaly Detection (FastAPI Integration)
- `POST /anomaly-detection/detect` - Detect anomalies in images
- `GET /anomaly-detection/health` - Check FastAPI service health

## Request/Response Examples

### Create Transformer
```json
POST /transformers
{
  "number": "TR-001",
  "pole": "P-123",
  "region": "North",
  "type": "Distribution",
  "location": "123 Main St",
  "weather": "Sunny",
  "baselineImage": "data:image/png;base64,..."
}
```

### Schedule Inspection
```json
POST /inspections
{
  "transformerId": 1,
  "date": "2026-01-20",
  "inspector": "John Doe",
  "status": "Pending"
}
```

### Save Annotations
```json
POST /annotations/{inspectionId}
{
  "annotations": [
    {
      "annotationId": "ai_1",
      "x": 100,
      "y": 150,
      "w": 50,
      "h": 50,
      "confidence": 0.95,
      "severity": "High",
      "classification": "Hotspot",
      "source": "ai"
    }
  ],
  "annotatedImage": "data:image/png;base64,...",
  "userId": "Admin"
}
```

### Detect Anomalies
```json
POST /anomaly-detection/detect
{
  "transformerId": "TR-001",
  "baselineImage": "data:image/png;base64,...",
  "maintenanceImage": "data:image/png;base64,...",
  "sliderPercent": 0
}
```

## Project Structure

```
src/main/java/com/chamikara/spring_backend/
├── SpringBackendApplication.java     # Main application class
├── config/                           # Configuration classes
│   ├── CorsConfig.java              # CORS configuration
│   ├── JacksonConfig.java           # JSON serialization
│   └── WebClientConfig.java         # WebClient for FastAPI calls
├── controller/                       # REST controllers
│   ├── TransformerController.java
│   ├── InspectionController.java
│   ├── AnnotationController.java
│   ├── AnnotationLogController.java
│   ├── MaintenanceRecordController.java
│   ├── AnomalyDetectionController.java
│   └── HealthController.java
├── dto/                              # Data Transfer Objects
│   ├── request/                      # Request DTOs
│   └── response/                     # Response DTOs
├── entity/                           # JPA entities
│   ├── Transformer.java
│   ├── Inspection.java
│   ├── Annotation.java
│   ├── AnnotationLog.java
│   └── MaintenanceRecord.java
├── exception/                        # Custom exceptions
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── ServiceException.java
│   └── GlobalExceptionHandler.java
├── repository/                       # JPA repositories
│   ├── TransformerRepository.java
│   ├── InspectionRepository.java
│   ├── AnnotationRepository.java
│   ├── AnnotationLogRepository.java
│   └── MaintenanceRecordRepository.java
└── service/                          # Business logic services
    ├── TransformerService.java
    ├── InspectionService.java
    ├── AnnotationService.java
    ├── AnnotationLogService.java
    ├── MaintenanceRecordService.java
    └── AnomalyDetectionService.java
```

## Configuration

Key configuration properties in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/transformer_db
spring.datasource.username=admin
spring.datasource.password=password123

# FastAPI Service
fastapi.service.url=http://localhost:8000
fastapi.service.timeout=60000

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=100MB
```

## Testing

Run tests with:
```bash
./mvnw test
```

Tests use an H2 in-memory database configured in `application-test.properties`.

## Building for Production

```bash
./mvnw clean package -DskipTests
java -jar target/spring-backend-0.0.1-SNAPSHOT.jar
```

## FastAPI Microservice Integration

The backend integrates with a FastAPI microservice for AI-based anomaly detection:

- **Endpoint**: `/api/v1/detect`
- **Method**: POST (multipart/form-data)
- **Parameters**:
  - `baseline`: Baseline image file
  - `maintenance`: Maintenance image file
  - `transformer_id`: Transformer identifier
  - `slider_percent`: Optional threshold adjustment

Make sure the FastAPI service is running at the URL configured in `fastapi.service.url`.

## License

This project is part of EN3350 - Software Design Competition at University of Moratuwa.

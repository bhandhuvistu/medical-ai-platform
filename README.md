# Medical AI Platform (Local Testable)

Generated on 2026-03-14.

## Modules
- core-contracts: Shared DTOs and Kafka events
- common-services: JPA entities, storage, AI & extraction services
- api-app: REST API (sync + async submit)
- worker-app: Kafka consumer for async processing
- infra-local: Docker Compose (Kafka, MinIO, Postgres, Ollama)

## Quick Start
1. Start infra:
   docker compose -f infra-local/docker-compose.yml up -d
2. Pull model:
   ollama pull llama3.1:8b
3. Build:
   mvn -T1C clean package
4. Run API:
   java -jar api-app/target/api-app-0.1.0.jar
5. Run Worker:
   java -jar worker-app/target/worker-app-0.1.0.jar

## Test Sync
curl -X POST http://localhost:8080/api/reports/upload   -F "files=@your-report.pdf"

## Test Async
curl -X POST http://localhost:8080/api/reports/submit   -F "files=@your-report.pdf"

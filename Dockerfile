FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY api-app/pom.xml api-app/
COPY common-services/pom.xml common-services/
COPY core-contracts/pom.xml core-contracts/
COPY worker-app/pom.xml worker-app/
RUN mvn -B dependency:resolve -DexcludeScope=test 2>&1 | head -50 || true
COPY . .
RUN mvn clean package -DskipTests -pl api-app -am
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/api-app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

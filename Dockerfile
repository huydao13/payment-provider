FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml trước để cache layer dependency — chỉ tải lại Maven deps
# khi pom.xml đổi, không phải mỗi lần code đổi.
COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src src
RUN mvn -q clean package -DskipTests

# ── Stage 2: run ─────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

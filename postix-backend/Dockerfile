# ─── Stage 1: build with Maven ─────────────────────────────────────────────────

FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /build

# 1) Copy only the pom (and wrapper) so Maven deps can cache
COPY pom.xml ./pom.xml
COPY .mvn/ .mvn/

# 2) Pre-fetch all dependencies
RUN mvn dependency:go-offline -B

# 3) Copy your sources and compile
COPY src/        src/
RUN mvn package -DskipTests -B

# ─── Stage 2: runtime image ───────────────────────────────────────────────────
# ─── Stage 2: runtime image ───────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copy whatever jar got built (Spring Boot “fat” jar)
COPY --from=build /build/target/*.jar ./postix-backend.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java \
-Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
-jar postix-backend.jar"]



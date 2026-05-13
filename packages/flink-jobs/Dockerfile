FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY . .
RUN ./gradlew shadowJar --no-daemon

FROM apache/flink:1.20.0-scala_2.12-java17

WORKDIR /opt/flink/lib
COPY --from=builder /app/build/libs/flink-jobs-*-all.jar flink-jobs.jar
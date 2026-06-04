# ==========================================
# Stage 1: Build Shadow/Fat JAR
# ==========================================
FROM gradle:8-jdk17-alpine AS builder
WORKDIR /app

# 1. 빌드 캐시 최적화: 설정 파일 및 래퍼 선복사
# COPY gradlew .
# COPY gradle gradle
COPY *.gradle.kts .
COPY slim-kafka-print-job/*.gradle.kts slim-kafka-print-job/

# 소스 없이 의존성만 먼저 다운로드하여 캐싱 레이어 생성
RUN gradle :slim-kafka-print-job:dependencies --no-daemon || true

# 2. 소스 코드 복사 및 빌드 (불필요한 테스트는 컴파일 속도를 위해 제외 권장)
COPY slim-kafka-print-job/src slim-kafka-print-job/src
RUN gradle :slim-kafka-print-job:shadowJar -x test --no-daemon

# ==========================================
# Stage 2: Flink Runtime Setup
# ==========================================
FROM flink:1.20.4-java17

# Flink 내부 기본 유저(flink:flink) 권한으로 파일 전송 및 소유권 변경
# Flink 공식 이미지는 /opt/flink/usrlib 디렉토리를 기본 지원하거나 내부 처리가 되어 있습니다.
WORKDIR /opt/flink
COPY --from=builder --chown=flink:flink /app/slim-kafka-print-job/build/libs/slim-kafka-print-job-*-all.jar /opt/flink/usrlib/app.jar

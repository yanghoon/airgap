#!/usr/bin/env bash
set -e

# 1. 스크립트 자신(build.sh)의 위치를 기준으로 절대 경로 계산
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 2. settings.gradle.kts가 위치한 Root 경로 산출 (jobs/docker 의 두 단계 위)
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

# 3. Skaffold가 호출된 현재 작업 디렉토리(Job 폴더) 기준으로 Job 이름 추출
JOB_DIR="$(pwd)"
JOB_NAME="$(basename "$JOB_DIR")"

echo "========================================"
echo "🚀 Building Flink Job: $JOB_NAME"
echo "📂 Project Root: $ROOT_DIR"
echo "📂 Working Dir : $JOB_DIR"
echo "📂 Image Tag   : $IMAGE"
echo "========================================"

# 4. 상대 경로(../../gradlew) 대신 산출된 절대 경로를 이용해 Gradle 빌드 실행
#    (-p 옵션으로 프로젝트 루트를 명시하여 더욱 견고하게 동작)
"$ROOT_DIR/gradlew" -p "$ROOT_DIR" ":jobs:$JOB_NAME:clean" ":jobs:$JOB_NAME:shadowJar"

# 5. Docker 빌드 시에도 Dockerfile 위치를 절대 경로로 지정
docker build --tag="$IMAGE" -f "$SCRIPT_DIR/Dockerfile.common" . --no-cache

echo "Build Success : $IMAGE"

# 
ENV_FILE="$SCRIPT_DIR/.env"
if [ -f "$ENV_FILE" ]; then
    # sed의 구분자로 '/' 대신 '|'를 사용하여 이미지 경로에 포함될 수 있는 '/' 문자와 충돌 방지
    # macOS와 Linux 모두 호환되도록 -i.bak 옵션 사용 후 백업 파일 삭제
    sed -i.bak "s|^FLINK_IMAGE=.*|FLINK_IMAGE=$IMAGE|" "$ENV_FILE"
    rm -f "${ENV_FILE}.bak"
    
    echo "✅ Updated .env: FLINK_IMAGE=$IMAGE"
else
    echo "⚠️ .env file not found at $ENV_FILE. Skipping update."
fi

# 6. Skaffold가 push를 요구할 경우(push: true) push 수행
if [ "${PUSH_IMAGE:-false}" = "true" ]; then
    echo "Pushing image: $IMAGE"
    docker push "$IMAGE"
fi
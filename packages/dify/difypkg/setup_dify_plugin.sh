#!/bin/bash

# ==============================================================================
# Dify Plugin Installation & Configuration Script
# ==============================================================================
# [가이드]
# 1. 설치 방법: 
#    - Dify 콘솔에서 어드민 API 토큰(ADMIN_API_TOKEN)을 발급받습니다.
#    - 본 스크립트 실행 권한을 줍니다. (chmod +x setup_dify_plugin.sh)
#    - 아래 명령어로 실행하여 difypkg를 업로드하고 설치합니다.
# 2. 설정 방법:
#    - 스크립트 실행 시 사내 모델 API URL과 Key를 인자로 넘기면 자동으로 설정됩니다.
# 
# [실행 예시]
# ./setup_dify_plugin.sh <ADMIN_API_TOKEN> <PLUGIN_PATH> <INTERNAL_API_BASE> <API_KEY>
# ./setup_dify_plugin.sh "app-xxx..." "openai_api_compatible.difypkg" "http://internal-llm.company.com/v1" "sk-dummy"
# ==============================================================================

DIFY_HOST="http://dify.local:8080"
ADMIN_API_TOKEN=${1:-""}
PLUGIN_PATH=${2:-"openai_api_compatible.difypkg"}
INTERNAL_API_BASE=${3:-"http://internal-llm.company.com/v1"}
INTERNAL_API_KEY=${4:-"dummy-key"}

# 도움말 출력
show_help() {
    echo "사용법: $0 <ADMIN_API_TOKEN> [PLUGIN_PATH] [INTERNAL_API_BASE] [INTERNAL_API_KEY]"
    echo "예시: $0 \"app-xxx\" \"openai_api_compatible.difypkg\" \"http://internal.llm/v1\" \"sk-123\""
}

# 기본 유효성 검사
validate_inputs() {
    if [ -z "$ADMIN_API_TOKEN" ]; then
        echo "❌ Error: ADMIN_API_TOKEN이 누락되었습니다."
        show_help
        exit 1
    fi

    if [ ! -f "$PLUGIN_PATH" ]; then
        echo "❌ Error: 플러그인 패키지 파일 '$PLUGIN_PATH' 을 찾을 수 없습니다."
        exit 1
    fi

    echo "=========================================="
    echo "Dify Host: $DIFY_HOST"
    echo "Plugin Path: $PLUGIN_PATH"
    echo "API Base: $INTERNAL_API_BASE"
    echo "=========================================="
}

# 플러그인 업로드 함수
upload_plugin() {
    echo -e "\n⏳ 1. 플러그인 패키지 업로드 중..."
    local upload_resp
    upload_resp=$(curl -s -X POST "$DIFY_HOST/console/api/workspaces/current/plugin/upload/pkg" \
        -H "Authorization: Bearer $ADMIN_API_TOKEN" \
        -F "pkg=@$PLUGIN_PATH")

    # unique_identifier 추출
    UNIQUE_ID=$(echo "$upload_resp" | grep -o '"unique_identifier":"[^"]*' | cut -d'"' -f4)

    if [ -z "$UNIQUE_ID" ] || [ "$UNIQUE_ID" == "null" ]; then
        echo "❌ Error: 플러그인 업로드 실패 또는 파싱 실패"
        echo "응답: $upload_resp"
        exit 1
    fi

    echo "✅ 업로드 성공! Unique ID: $UNIQUE_ID"
}

# 플러그인 설치 함수
install_plugin() {
    echo -e "\n⏳ 2. 플러그인 설치 중..."
    local install_resp
    install_resp=$(curl -s -X POST "$DIFY_HOST/console/api/workspaces/current/plugin/install/pkg" \
        -H "Authorization: Bearer $ADMIN_API_TOKEN" \
        -H "Content-Type: application/json" \
        -d "{\"unique_identifier\": \"$UNIQUE_ID\"}")

    if echo "$install_resp" | grep -q '"result":"success"'; then
        echo "✅ 설치 성공!"
    else
        echo "⚠️ 설치 실패 또는 예외 응답: $install_resp"
    fi
}

# 사내 모델용 기본 자격증명 설정 함수
configure_model() {
    echo -e "\n⏳ 3. 모델 프로바이더(사내 모델) 자격 증명 설정 중..."
    local cred_resp
    cred_resp=$(curl -s -X POST "$DIFY_HOST/console/api/workspaces/current/model-providers/openai_api_compatible/credentials" \
        -H "Authorization: Bearer $ADMIN_API_TOKEN" \
        -H "Content-Type: application/json" \
        -d "{
            \"credentials\": {
                \"api_key\": \"$INTERNAL_API_KEY\",
                \"api_base\": \"$INTERNAL_API_BASE\"
            }
        }")

    echo "✅ 설정 완료! 응답: $cred_resp"
}

# 메인 실행부
main() {
    validate_inputs
    upload_plugin
    install_plugin
    configure_model
    echo -e "\n🎉 모든 작업이 완료되었습니다."
}

# 스크립트 실행
main

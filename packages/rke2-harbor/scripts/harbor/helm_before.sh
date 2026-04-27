# .tmp-XXXX 형식의 유니크한 임시 폴더 생성
# TMP_DIR=$(mktemp -d ./.tmp-XXXX)
TMP_DIR=.tmp

echo "Working in $TMP_DIR"
mkdir -p "$TMP_DIR"
# rm -rf $TMP_DIR && echo "Cleaned up existing $TMP_DIR"

# 사용자 설정 변수
REPO_URL="https://helm.goharbor.io"
CHART_NAME="harbor"
CHART_VERSION="1.3.2"
CHART_FILE="${TMP_DIR}/${CHART_NAME}-${CHART_VERSION}.tgz"
CHART_MANIFEST="${TMP_DIR}/rke2-helm-${CHART_NAME}.yaml"

echo "### [1/2] Fetching download URL from ${REPO_URL}/index.yaml..."

# 1. index.yaml 다운로드 및 yq 파싱
# -s: Silent 모드
# -L: Redirect 추적
DOWNLOAD_URL=$(curl -sL "${REPO_URL}/index.yaml" | \
    zarf tools yq ".entries.\"${CHART_NAME}\"[] | select(.version == \"${CHART_VERSION}\") | .urls[0]")

# URL이 정상적으로 추출되었는지 확인
if [ -z "$DOWNLOAD_URL" ] || [ "$DOWNLOAD_URL" == "null" ]; then
    echo "Error: Could not find chart ${CHART_NAME}:${CHART_VERSION} in index.yaml"
    exit 1
fi

echo "### Target URL: ${DOWNLOAD_URL}"
echo "### [2/2] Downloading .tgz file..."

# 2. 추출된 URL로 최종 다운로드 (URL이 상대 경로일 경우를 대비해 처리 필요)
# 많은 Helm Repo가 절대 경로를 사용하지만, 상대 경로일 경우 REPO_URL을 앞에 붙여야 함
if [[ $DOWNLOAD_URL != http* ]]; then
    DOWNLOAD_URL="${REPO_URL}/${DOWNLOAD_URL}"
fi

curl -L -o "$CHART_FILE" "$DOWNLOAD_URL"

echo "### Done: $(ls -lh $CHART_FILE)"


# ./zarf tools yq eval ".spec.chartContent = load_str(\"$CHART_FILE\")" rke2-helm-${CHART_NAME}.yaml > "$CHART_MANIFEST"
cp rke2-helm-${CHART_NAME}.yaml "$CHART_MANIFEST"
base64 -i $CHART_FILE | \
  zarf tools yq -i '.spec.chartContent  = load_str("/dev/stdin")' "$CHART_MANIFEST"
cat values-${CHART_NAME}.yaml | \
  zarf tools yq -i '.spec.valuesContent = load_str("/dev/stdin")' "$CHART_MANIFEST"

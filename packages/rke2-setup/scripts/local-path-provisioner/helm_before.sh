# .tmp-XXXX 형식의 유니크한 임시 폴더 생성
# TMP_DIR=$(mktemp -d ./.tmp-XXXX)
TMP_DIR=.tmp
echo "Working in $TMP_DIR"
rm -rf $TMP_DIR && echo "Cleaned up existing $TMP_DIR"

# Git 클론 및 패키징
git clone --depth 1 https://github.com/rancher/local-path-provisioner.git "$TMP_DIR/source"
# ./zarf tools helm package "$TMP_DIR/source/deploy/chart/local-path-provisioner" -d "$TMP_DIR"
tar -czf "$TMP_DIR/local-path-provisioner.tgz" -C "$TMP_DIR/source/deploy/chart/local-path-provisioner" .

# 생성된 .tgz 파일을 Base64로 인코딩하여 Zarf 변수로 저장
CHART_FILE=$(ls $TMP_DIR/local-path-provisioner.tgz)
# Zarf 0.7x의 set-variable 문법 활용
# echo "$(base64 -w 0 < $CHART_FILE)"
# ./zarf tools yq eval ".spec.chartContent = load_str(\"$CHART_FILE\")" rke2-helm-local-path-provisioner.yaml > "$TMP_DIR/rke2-helm-local-path-provisioner.yaml"
cp rke2-helm-local-path-provisioner.yaml "$TMP_DIR/rke2-helm-local-path-provisioner.yaml"
base64 $CHART_FILE | \
  zarf tools yq -i '.spec.chartContent  = load_str("/dev/stdin")' "$TMP_DIR/rke2-helm-local-path-provisioner.yaml"
cat values-local-path-provisioner.yaml | \
  zarf tools yq -i '.spec.valuesContent = load_str("/dev/stdin")' "$TMP_DIR/rke2-helm-local-path-provisioner.yaml"
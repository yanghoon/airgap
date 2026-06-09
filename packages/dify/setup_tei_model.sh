#!/bin/bash
set -e

echo "=== 1. 다운로드 대상 모델: intfloat/multilingual-e5-small ==="
mkdir -p models

# huggingface-cli 설치 확인 (없으면 설치)
if ! command -v huggingface-cli &> /dev/null; then
    echo "huggingface-cli가 없습니다. 설치를 진행합니다..."
    curl -LsSf https://hf.co/cli/install.sh | bash
    export PATH="$HOME/.local/bin:$PATH"
fi

echo "모델 다운로드 시작..."
huggingface-cli download intfloat/multilingual-e5-small --local-dir models/intfloat/multilingual-e5-small

echo "=== 2. PVC에 데이터를 복사하기 위한 임시 Pod 생성 ==="
cat <<EOF | kubectl apply -n dify -f -
apiVersion: v1
kind: Pod
metadata:
  name: tei-model-copier
  namespace: dify
spec:
  containers:
  - name: copier
    image: busybox
    command: ["sleep", "infinity"]
    volumeMounts:
    - name: model-data
      mountPath: /data
  volumes:
  - name: model-data
    persistentVolumeClaim:
      claimName: tei-model-pvc
EOF

echo "임시 Pod가 실행될 때까지 대기합니다..."
kubectl wait --for=condition=ready pod/tei-model-copier -n dify --timeout=60s

echo "=== 3. 모델 파일 복사 중 (Host -> PVC) ==="
kubectl cp models dify/tei-model-copier:/data/
echo "복사가 완료되었습니다. 임시 Pod를 삭제합니다."
kubectl delete pod tei-model-copier -n dify

echo "=== 4. TEI Deployment 재시작 (모델 인식) ==="
kubectl rollout restart deployment text-embeddings-inference -n dify
echo "TEI Pod가 정상적으로 실행될 때까지 대기합니다..."
kubectl rollout status deployment text-embeddings-inference -n dify

echo "모든 구성이 완료되었습니다!"

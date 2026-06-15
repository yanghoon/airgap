#!/bin/sh
# TEST_NAME=Exact_Count_Delay_Test
# TOTAL_COUNT="100" # 100건 * 0.2초 = 이론상 최소 20초 소요
# KAFKA_BROKER=my-cluster.kafka.local:443
# KAFKA_BROKER=my-cluster.kafka.local:9092
# KAFKA_TOPIC=test-topic
# KAFKA_OPTS="-X ssl.endpoint.identification.algorithm=none -X enable.ssl.certificate.verification=0"
# KAFKA_OPTS="$KAFKA_OPTS -d all"

# set -x

# awk를 사용하여 이론상 예상 소요 시간 계산
EXPECTED_TIME=$(awk -v count="${TOTAL_COUNT:?}" 'BEGIN { print count * 0.1 }')

echo "=========================================="
echo "테스트 시작: $TEST_NAME"
echo "▶ 목표 전송 건수: $TOTAL_COUNT 건"
echo "▶ 이론상 소요 시간: 최소 $EXPECTED_TIME 초 (sleep 0.1 기준)"
echo "=========================================="

# 시작 시간 측정 (나노초 단위)
START_TIME=$(date +%s.%N)

# 파이프라인을 통한 정량 로그 전송
kcat -b $KAFKA_BROKER $KAFKA_OPTS -L
{
  for i in $(seq 1 $TOTAL_COUNT); do
      TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
      echo "$TIMESTAMP | $TEST_NAME | SEQ=$(printf "%06d" $i)"
      sleep 0.1
  done
} | kcat -b $KAFKA_BROKER -t $KAFKA_TOPIC $KAFKA_OPTS -P

# kcat 종료 코드 및 종료 시간 측정
KCAT_EXIT=$?
END_TIME=$(date +%s.%N)

if [ $KCAT_EXIT -eq 0 ]; then
    echo "=========================================="
    echo "Kafka 전송 및 확인(ACK) 완료"
    echo "=========================================="
    
    # 실제 소요 시간 및 최종 건수 출력 (EPS 제외)
    awk -v t1="$START_TIME" -v t2="$END_TIME" -v count="$TOTAL_COUNT" 'BEGIN {
        elapsed = t2 - t1;
        printf "▶ 최종 전송 성공 건수: %d 건\n", count;
        printf "▶ 실제 총 소요 시간  : %.3f 초\n", elapsed;
    }'
    echo "=========================================="
    exit 0
else
    echo "Kafka 전송 중 에러가 발생했습니다."
    exit 1
fi

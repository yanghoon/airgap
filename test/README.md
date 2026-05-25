# Test

## Kafka

```bash
vi manifests/logging-job-kcat.yaml
# kind: ConfigMap
# metadata:
#   name: kafka-test-config
# data:
#   TEST_NAME: Exact_Count_Delay_Test
#   TOTAL_COUNT: "2000"  # 100건 * 0.2초 = 이론상 최소 20초 소요
#   KAFKA_BROKER: kafka-service.namespace.svc.cluster.local:9092
#   KAFKA_TOPIC: test-topic
```

```bash
kubectl replace --force manifess/logging-job-kcat.yaml
```

# Kafka Operator

## Install

```bash
zarf package deploy /opt/airgap/packages/zarf-package-kafka-operator-v0.0.1.tar.zst
```

## Test

### `kafkactl`

```bash
# curl -OL | sudo tar -xzf - -o /usr/bin/kafkactl

kafkactl config add my-cluster --broker localhost:9094  # kubectl port-forward
kafkactl get brokers
kafkactl get topics
```

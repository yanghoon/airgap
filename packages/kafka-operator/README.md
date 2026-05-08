# Kafka Operator

## Install

```bash
zarf package deploy /opt/airgap/packages/zarf-package-kafka-operator-v0.0.1.tar.zst
```

## Test

### `kafkactl`

```bash
# Install kafkactl
curl -OL https://github.com/deviceinsight/kafkactl | sudo tar -xzf - -o /usr/bin/kafkactl

# Configure
cat ~/.config/kafkactl/config.yml <EOF
contexts:
  my-cluster:
    brokers:
      - my-cluster.kafka.local:443
    tls:
      enabled: true
      insecure: true
      # ca, cert, certkey
EOF

# Conection Test
kafkactl config get-contexts
kafkactl get brokers

# Message Test
kafkactl get topics
kafkactl describe topic test
kafkactl consume test --offset 0=0  # --offset partition=offset
```

# Flink

## Flink Jobs

**Local**

```bash
# Local Test
gradle run
```

**Build**

```bash
# Fat Jar
gradle :jobs:$(basename $PWD):shadowJar

# Container Image
skaffold build
# ../docker/build.sh
# + gradle :jobs:$(basename $PWD):shadowJar
# + docker build --tag $(basenmae $PWD):latest -f ../docker/Dockerfile.common

# Test Container Image
# +vi ./docker/.env
(cd ../docker && docker compose up)
```

**Deploy**

```bash
skaffold run
```

## Flink Starters

```kotlin
dependencies {
    implementation(project(":starters:flink-core"))
    implementation(project(":starters:flink-table"))
    implementation(project(":starters:flink-logging"))

    shadowImplementation(project(":starters:flink-kafka"))
    shadowImplementation(project(":jobs:common"))
}
```

## Development

### VS Code

```json
// source : launch.json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "KafkaPrintJob",
            "request": "launch",
            "mainClass": "io.slim.flink.KafkaPrintJob",
            "projectName": "slim-kafka-print-job",
            "env": {
                "FLINK_CONF_DIR": "${workspaceFolder}/packages/flink-jobs"
            },
            "vmArgs": "--add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED"
        }
    ]
}
```
# Prompt
Create flink job

# Spec
- flink job
    - flink version 1.20 LTS
    - simple source to generate counter each 5s
    - trmastorm to combine data with current datetime
    - sink to stdout
- build
    - using build.gradle.kts
    - using skaffold
    - use `git describe` as image tag
- deploy
    - create `FlinkDeployment` in `manifests/flink-deploy.yaml`
    - deploy on kubernetes

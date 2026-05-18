# Debugging

## Zarf

### Zarf Source

```bash
# TODO: Install Golang
```

```bash
git clone https://github.com/zarf-dev/zarf && cd zarf
go run main.go COMMAND [flags]
```

### Zarf Command

```bash
zarf version

zarf tools helm version
zarf tools helm template rancher rancher/rancher --version 2.13.3 -f values.yaml --kube-version 1.34.4

zarf dev inspect definition
zarf dev find-images --kube-version 1.34.4
zarf dev find-images --kube-version 1.34.4 --set-values "postDelete.enabled=false"

zarf package create
zarf package deploy /opt/airgap/packages/zarf-package-rancher-amd64-2.13.3.tar.zst

zarf package list
zarf package inspect definition

zarf connect list
zarf connect rancher --local-port 8443
```

```bash
curl -u "zarf-pull:$(zarf tools get-creds registry-readonly)" localhost:51351/v2/_catalog
curl -u "zarf-pull:$(zarf tools get-creds registry-readonly)" localhost:51351/v2/bitnami/postgresql/tags/list
```

### Zarf Command with Registry

```bash
# ZARF_REGISTRY
# ZARF_REGISTRY_AUTH_PUSH
zarf tools registry login harbor.local --interactive #--insecure

# zarf tools registry catalog  # zarf-docker-registry
# zarf tools registry catalog harbor.local --insecure  # Unauthorized 401

# Prepare: create harbor project (ex. strmizi for query.io/strimzi/kafka-bridge:latest)
zarf package mirror-resources zarf-package-xxx.tar.zst \
  --images \
  --registry-url harbor.local --registry-push-username admin --registry-push-password xxxx
#   --insecure-skip-tls-verify
```

## Skaffold

### Docker Build

```bash
skaffold build  --dry-run
skaffold render --digest-source=local
```

```bash
skaffold config set default-repo harbor.local/flink-jobs
skaffold config set insecure-registries harbor.local
skaffold config list
```

### Podman Build

```toml
# ~/.config/containers/registries.conf
unqualified-search-registries = ["docker.io"]

[[registry]]
location = "harbor.local"
insecure = true
```

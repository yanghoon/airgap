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

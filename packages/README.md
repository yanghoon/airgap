
## Local

```bash
# Linux
zarf init

# Mac
kind create cluster --config kind-config.yaml
    # kind get clusters
    # kind get kubeconfig

zarf init --confirm
# https://github.com/kubernetes/ingress-nginx/tree/main/deploy/static/provider/kind
zarf tools kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

```bash
# cd packages/xxx
# zarf package create
# zarf package deploy zarf-package-xxx.tar.zst

# zarf package deploy zarf-package-rke2-setup-amd64-v0.0.1.tar.zst --components rke2-helmconfig-ingress-nginx --confirm
zarf package deploy zarf-package-dify-amd64-1.14.1.tar.zst --confirm
```

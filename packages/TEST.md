# Zarf Test

## Zarf Init (k3s)

```bash
zarf version
zarf init  # k3s, git-server (components)
           # ~/.zarf-cache/zarf-init-amd64-v0.73.1.tar.zst (package-location)
zarf destroy --confirm

zart tools kubectl get node
zarf tools monitor  # k9s

# /etc/rancher/k3s/k3s.yaml
# /var/lib/rancher/k3s/**
```

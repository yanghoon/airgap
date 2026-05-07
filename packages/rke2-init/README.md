# RKE2

## Install

**Variables**

- Registry Domain : harbor.local
- Storage Class : standard

```bash
zarf package /opt/airgap/packages/zarf-package-rke2-init-v0.0.1.tar.zst    # rke2 binary and scripts
zarf package /opt/airgap/packages/zarf-package-rke2-setup-v0.0.1.tar.zst   # rke2 airgap components (local-path-provisioner, ...)
zarf package /opt/airgap/packages/zarf-package-rke2-harbor-v0.0.1.tar.zst  # harbor
```

## Configure

### In-Cluster Registry

0. ~~**Insecure TLS**~~

```bash
sudo cat /etc/rancher/rke2/registries.yaml <EOF
configs:
  harbor.local: # Change to your domain name
    tls:
      insecure-skip-verify: true
EOF
```

1. `zarf init`
```bash
# Prepare: create harbor project zarf-dev (for ghcr.io/zarf-dev/zarf/agent:v0.xx.x)
zarf init \
  --registry-url harbor.local --registry-push-usernmae admin --registry-push-password xxxx
#   --insecure-skip-tls-verify
```

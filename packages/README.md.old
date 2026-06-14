# RKE2 Airgap Zarf Init Package

This package provides RKE2 deployment for airgapped environments using Zarf package manager.

## Overview

The package includes three deployment scenarios:

1. **init-rke2-server**: First RKE2 server (cluster initialization)
2. **init-rke2-server-join**: Additional RKE2 servers (join existing cluster)
3. **init-rke2-agent**: RKE2 agents (worker nodes)

## Prerequisites

- Zarf CLI installed
- RKE2 binaries and images downloaded
- SSH access to target nodes
- Private registry (optional, for airgap)

## Package Structure

```
packages/
├── init-rke2-server/
│   └── zarf.yaml              # Package definition
└── files/
    ├── rke2.linux-amd64.tar.gz
    ├── rke2.linux-arm64.tar.gz
    ├── rke2-images.linux-amd64.tar.zst
    ├── rke2-images.linux-arm64.tar.zst
    ├── ansible/
    │   ├── playbooks/install.yml
    │   ├── inventory/
    │   │   ├── hosts-init.yml        # First server inventory
    │   │   ├── hosts-server-join.yml # Additional server
    │   │   └── hosts-agent.yml      # Agent inventory
    │   └── roles/rke2/tasks/main.yml
    └── config/
        ├── server-config-example.yaml
        └── registries-example.yaml
```

## Usage

### 1. Create Package

```bash
# Package the RKE2 binaries and images
zarf package create packages/init-rke2-server \
  --variables RKE2_VERSION=v1.35.1+rke2r1 \
  --set SYSTEM_DEFAULT_REGISTRY=registry.example.com:5000
```

### 2. Deploy First Server

```bash
# Deploy to first server node
zarf package deploy init-rke2-server.tar.zst \
  --components=rke2-binaries,rke2-images,rke2-install-playbook \
  --set RKE2_VERSION=v1.35.1+rke2r1 \
  --set CLUSTER_CIDR=10.42.0.0/16 \
  --set SERVICE_CIDR=10.43.0.0/16
```

Or use Ansible directly:

```bash
# Run install playbook
ansible-playbook -i files/ansible/inventory/hosts-init.yml \
  files/ansible/playbooks/install.yml \
  --extra-vars "RKE2_VERSION=v1.35.1+rke2r1"
```

### 3. Get Join Token

After first server starts, get the join token:

```bash
# On first server
cat /var/lib/rancher/rke2/server/db/etcd/node-token
```

### 4. Join Additional Servers

```bash
# Use server-join inventory with token
ansible-playbook -i files/ansible/inventory/hosts-server-join.yml \
  files/ansible/playbooks/install.yml \
  --extra-vars "RKE2_VERSION=v1.35.1+rke2r1"
```

### 5. Join Agents

```bash
# Use agent inventory with token
ansible-playbook -i files/ansible/inventory/hosts-agent.yml \
  files/ansible/playbooks/install.yml \
  --extra-vars "RKE2_VERSION=v1.35.1+rke2r1"
```

## Configuration

### Private Registry

Edit `config/server-config-example.yaml`:

```yaml
system-default-registry: "registry.example.com:5000"
cluster-cidr: "10.42.0.0/16"
service-cidr: "10.43.0.0/16"
```

### Registrar Configuration

Edit `config/registries-example.yaml`:

```yaml
mirrors:
  docker.io:
    endpoint:
      - "https://registry.example.com:5000"
configs:
  "registry.example.com:5000":
    tls:
      insecure_skip_verify: true
```

## Variables

| Variable | Description | Default |
|----------|-------------|---------|
| RKE2_VERSION | RKE2 version | v1.35.1+rke2r1 |
| ARCH | Target architecture | amd64 |
| SYSTEM_DEFAULT_REGISTRY | Private registry URL | - |
| RKE2_CONFIG_YAML | RKE2 config YAML | - |
| INSTALL_DIR | Installation directory | /usr/local |
| DATA_DIR | RKE2 data directory | /var/lib/rancher/rke2 |
| SERVER_URL | First server URL | https://127.0.0.1:9345 |
| JOIN_TOKEN | Cluster join token | - |
| CLUSTER_CIDR | Pod network CIDR | 10.42.0.0/16 |
| SERVICE_CIDR | Service network CIDR | 10.43.0.0/16 |

## Airgap Image Loading

### Method 1: Local Tarball

```bash
# Place images in /var/lib/rancher/rke2/agent/images/
sudo cp rke2-images.linux-amd64.tar.zst /var/lib/rancher/rke2/agent/images/
```

### Method 2: Private Registry

```bash
# Load and push images to private registry
docker load -i rke2-images.linux-amd64.tar.zst
docker tag rancher/rke2-runtime:v1.35.1-rke2r1 registry.example.com:5000/rancher/rke2-runtime:v1.35.1-rke2r1
docker push registry.example.com:5000/rancher/rke2-runtime:v1.35.1-rke2r1
```

## Security

- Tokens should be handled securely
- Use TLS for registry communication
- Set file permissions (0600) for kubeconfig and certs

## References

- [RKE2 Documentation](https://docs.rke2.io)
- [Zarf Documentation](https://docs.zarf.dev)
- [RKE2 Releases](https://github.com/rancher/rke2/releases)
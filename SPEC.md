# RKE2 Airgap Zarf Init Package Specification

## Overview

This document describes the Zarf init package for deploying RKE2 in airgapped environments. The package provides three deployment scenarios:

1. **init-rke2-server**: First RKE2 server node (cluster initialization)
2. **init-rke2-server-join**: Additional RKE2 server nodes (join to existing cluster)
3. **init-rke2-agent**: RKE2 agent nodes (worker nodes)

## Use Case

Zarf packages are used to package all required artifacts for airgapped RKE2 deployment:
- RKE2 binaries (tarball)
- RKE2 container images
- Optional: Helm charts, manifests
- Configuration files

Each package variant includes:
- RKE2 install tarball
- Required container images
- Ansible playbooks for deployment

## Package Components

### 1. init-rke2-server

**Purpose**: Deploy first RKE2 server node to initialize new cluster

**Included**:
- `rke2.linux-${ARCH}.tar.gz` - RKE2 binary and systemd units
- `rke2-images.linux-${ARCH}.tar.zst` - Container images
- `rke2-images-cilium.linux-${ARCH}.tar.zst` (optional) - Cilium CNI images
- Ansible playbook to configure and start rke2-server

**Variables**:
- `rke2_version`: RKE2 version (e.g., "v1.35.1+rke2r1")
- `rke2_config`: RKE2 configuration YAML
- `system_default_registry`: Private registry URL (optional)
- `install_dir`: Installation directory (/usr/local or /opt/rke2)

### 2. init-rke2-server-join

**Purpose**: Join additional server nodes to existing cluster

**Included**:
- `rke2.linux-${ARCH}.tar.gz` - RKE2 binary
- `rke2-images.linux-${ARCH}.tar.zst` - Container images
- Token/join information from first server
- Ansible playbook to join as server

**Variables**:
- `rke2_version`: RKE2 version
- `server_join_url`: First server URL (https://server0:9345)
- `join_token`: Server join token
- `rke2_config`: RKE2 configuration

### 3. init-rke2-agent

**Purpose**: Deploy agent nodes to join cluster as workers

**Included**:
- `rke2.linux-${ARCH}.tar.gz` - RKE2 binary
- `rke2-images.linux-${ARCH}.tar.zst` - Container images
- Ansible playbook to join as agent

**Variables**:
- `rke2_version`: RKE2 version
- `server_join_url`: Any server URL
- `join_token`: Agent join token
- `rke2_config`: RKE2 configuration

## Package Structure

```
packages/init-rke2-server/
├── zarf.yaml                 # Package definition
├── files/
│   ├── rke2.linux-amd64.tar.gz
│   ├── rke2.linux-arm64.tar.gz
│   ├── rke2-images.linux-amd64.tar.zst
│   ├── rke2-images.linux-arm64.tar.zst
│   └── inventory/
│       └── hosts.yml         # Sample inventory
└── README.md                 # Usage documentation
```

## Zarf Package Definition (zarf.yaml)

```yaml
kind: ZarfPackageConfig
metadata:
  name: init-rke2-server
  description: RKE2 airgap init package for server deployment
  version: 0.1.0

variables:
  - name: RKE2_VERSION
    default: "v1.35.1+rke2r1"
  - name: SYSTEM_DEFAULT_REGISTRY
    description: Private registry URL for airgap
  - name: RKE2_CONFIG
    description: RKE2 configuration YAML
  - name: INSTALL_DIR
    default: "/usr/local"

components:
  - name: rke2-server-binaries
    required: true
    files:
      - source: files/rke2.linux-amd64.tar.gz
        destination: rke2.linux-amd64.tar.gz
      - source: files/rke2.linux-arm64.tar.gz
        destination: rke2.linux-arm64.tar.gz

  - name: rke2-server-images
    required: true
    images:
      - rancher/rke2-runtime:v1.35.1-rke2r1
      - rancher/rke2:v1.35.1-rke2r1

  - name: rke2-server-config
    required: true
    manifests:
      - name: rke2-server-install
        path: files/ansible/playbooks

  - name: rke2-server-join-config
    required: false
    description: Server join configuration
    manifests:
      - name: rke2-server-join
        path: files/ansible/playbooks
```

## Deployment Flow

### Server (First Node)

1. Load images from tarball or private registry
2. Extract RKE2 binary to `/usr/local` or `/opt/rke2`
3. Configure registries.yaml (if using private registry)
4. Generate config.yaml with server settings
5. Enable and start rke2-server service
6. Wait for cluster initialization (etcd, control-plane)
7. Save join tokens for server/agent nodes

### Server (Join)

1. Load images from tarball or private registry
2. Extract RKE2 binary
3. Configure config.yaml with server-url and token
4. Enable and start rke2-server service
5. Wait for node to join cluster

### Agent

1. Load images from tarball or private registry
2. Extract RKE2 binary
3. Configure config.yaml with server-url and agent token
4. Enable and start rke2-agent service
5. Wait for node to join cluster

## Private Registry Configuration

When using a private registry, add to RKE2 config:

```yaml
system-default-registry: "registry.example.com:5000"
tls:
  sans:
    - "registry.example.com"
```

## Airgap Image Loading

### Method 1: Private Registry

1. Push images to private registry:
```bash
docker load -i rke2-images.linux-amd64.tar.zst
docker tag rancher/rke2-runtime:v1.35.1-rke2r1 registry.example.com:5000/rancher/rke2-runtime:v1.35.1-rke2r1
docker push registry.example.com:5000/rancher/rke2-runtime:v1.35.1-rke2r1
```

2. Configure registries.yaml:
```yaml
mirrors:
  docker.io:
    endpoint:
      - "https://registry.example.com:5000"
```

### Method 2: Local Tarball

Place tarball in `/var/lib/rancher/rke2/agent/images/`

## Security Considerations

- Tokens should be handled securely (use Zarf secrets)
- TLS certificates for registry communication
- SELinux policies for RHEL-based systems
- File permissions on kubeconfig and certs (0600)

## References

- [RKE2 Airgap Install](https://docs.rke2.io/install/airgap)
- [Zarf Init Package](https://docs.zarf.dev/ref/init-package/)
- [RKE2 Releases](https://github.com/rancher/rke2/releases)
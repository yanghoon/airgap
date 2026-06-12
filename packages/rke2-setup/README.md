# RKE2 Setup

Initial Base Components for Workload (Storage, Ingress, ...)

## Usage

```bash
# Packaging
zarf package create

# Install
zarf package deploy
```

### Rancher Local Path Provisioner Helm Chart

- for Dynamic Storage Provisioning
- RKE2 HelmChart Resource (Helm Chart as Base64 Encoded, Default Values)
- `images_before.sh` : create tar images using `zarf tools registry pull --format=tarball`
- `helm_before.sh` : update helm chart as base64 format into yaml

### Nginx Ingress Controller Helm Chart Config

- Custom Config for RKE2 Nginx Ingress Controller Helm Chart
- RKE2 HelmChartConfig Resource

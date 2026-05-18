# Dify Docker for Air-Gapped Environment

Setup Dify with bundled official plugins for air-gapped/offline deployment.

## Prerequisites

- curl
- docker & docker-compose

## Setup

### 1. Download Dify Docker

```bash
# Download and extract only docker directory from master branch
curl -sL https://github.com/langgenius/dify/archive/refs/heads/master.tar.gz | tar -xz --strip-components=1 dify-main/docker

# Copy .env.example to .env
cp docker/.env.example docker/.env
```

### 2. Package Official Plugins (Optional)

To bundle official plugins for offline installation:

```bash
./difypkg.sh models/openai_api_compatible
```

```bash
# https://docs.dify.ai/en/self-host/configuration/environments#plugin-daemon-storage-configuration
cp ./plugins/ ./docker/volumes/plugin_daemon/
```

This will:
1. Download `langgenius/dify-official-plugins` archive
2. Extract plugin directory into `plugins/`
3. Create `<plugin-name>.difypkg` file

### 3. Configure docker-compose.yaml

Add the following environment variables to `docker/docker-compose.yaml`:

```yaml
services:
  api:
    environment:
      - FORCE_VERIFYING_SIGNATURE=false
      - ENFORCE_LANGGENIUS_PLUGIN_SIGNATURES=false
      - PLUGIN_MAX_PACKAGE_SIZE=524288000

  nginx:
    environment:
      - NGINX_CLIENT_MAX_BODY_SIZE=500M
```

## Usage

```bash
cd docker
docker-compose up -d
```

Access Dify at `http://localhost:80`
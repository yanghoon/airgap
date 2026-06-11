# Dify

## Dify Docker Compose

```bash
docker compose up
```

```bash
mkdir -p .tmp
curl -L https://github.com/langgenius/dify/archive/refs/heads/main.zip -o .tmp/dify.zip

mkdir -p temp_dify
unzip -q .tmp/dify.zip -d temp_dify
mv temp_dify/dify-main/docker ./dify

rm -rf temp_dify
rm .tmp/dify.zip
```

### Configure

```bash
mkdir -p dify/volumes/db/data/pgdata
mkdir -p dify/volumes/redis/data
mkdir -p dify/volumes/certbot/conf/live
mkdir -p dify/volumes/certbot/www
```

```bash
cp dify/.env.example dify/.env
vi dify/.env
# - for Plugin
# FORCE_VERIFYING_SIGNATURE=false
#
# - for Port
# EXPOSE_NGINX_PORT=7080
# EXPOSE_NGINX_SSL_PORT=7433
# CONSOLE_API_URL=http://localhost:7080
# CONSOLE_WEB_URL=http://localhost:7080
# SERVICE_API_URL=http://localhost:7080
# APP_API_URL=http://localhost:7080
# APP_WEB_URL=http://localhost:7080
# ENDPOINT_URL_TEMPLATE=http://localhost:7080/e/{hook_id}
# NEXT_PUBLIC_SOCKET_URL=ws://localhost:7080
```

## Dify Plugin Offline Packager

### Setup

```bash
mkdir -p packager
mkdir -p packager/plugins

curl -L https://github.com/kurokobo/dify-plugin-offline-packager/archive/refs/heads/main.tar.gz | tar -xf - -C packager --strip-components=1
curl -L https://github.com/langgenius/dify-official-plugins/archive/refs/heads/main.tar.gz | tar -xf - -C packager/plugins --strip-components=1
```

**UV**

```bash
# Mac
brew install uv
# brew install ca-certificates
```

```bash
# Linux
sudo apt install -y python3-pip  # suto apt --fix-broken install

curl -LsSf https://astral.sh/uv/install.sh | sh
vi ~/.config/uv/uv.toml
# [
#     ...
#     "github.com"
# ]
```

### Packaging

**Offline Packager**

```bash
# 
cd packager
uv run scripts/packager.py --marketplace langgenius/openai_api_compatible:0.0.53

# 
cd packager
uv run scripts/packager.py --local dummy.difypkg  # for dify-plugin
bin/dify-plugin-* plugin package plugins/models/openai_api_compatible -o difypkg/openai_api_compatible.difypkg
uv run scripts/packager.py --local difypkg/openai_api_compatible.difypkg
```

**plugins/models/openai_api_compatible**

```bash
vi plugins/models/openai_api_compatible/pyproject.toml
# dependencies = [
#     ...
#     "gevent>=25.5.1",
#     "zope-interface>=8.3",
#     "h11>=0.16.0"
# ]
```

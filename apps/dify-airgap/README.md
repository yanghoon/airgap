# Dify

## Dify Docker Compose

```bash
curl -L https://github.com/langgenius/dify/archive/refs/heads/main.zip -o dify.zip

mkdir -p temp_dify
unzip -q dify.zip -d temp_dify
mv temp_dify/dify-main/docker ./dify

rm -rf temp_dify
rm dify.zip
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
curl -L https://github.com/langgenius/dify-plugin-offline-packager/archive/refs/haeds/main.tar.gz -o packager.tar.gz

mkdir -p packager
tar -xzf packager.tar.gz -C packager
rm packager.tar.gz
```

```bash
curl -L https://github.com/langgenius/dify-official-plugins/archive/refs/haeds/main.tar.gz -o plugins.tar.gz

mkdir -p plugins
tar -xzf plugins.tar.gz -C plugins
rm plugins.tar.gz
```

### Packaging

```bash
sudo apt install -y python3-pip  # suto apt --fix-broken install

curl -LsSf https://astral.sh/uv/install.sh | sh
vi ~/.config/uv/uv.toml
# [
#     ...
#     "github.com"
# ]
```

```bash
packager/bin/dify-plugin-cli-* plugin package \
    plugins/models/openai_api_compatible
uv run packager/scripts/packager.py \
    --local openai_api_compatible.difypkg
```

* **models/openai_api_compatible**

```bash
vi plugins/models/openai_api_compatible/pyproject.toml
# dependencies = [
#     ...
#     "gevent>=25.5.1",
#     "zope-interface>=8.3",
#     "h11>=0.16.0"
# ]
```

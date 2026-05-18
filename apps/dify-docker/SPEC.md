# Dify Docker for Air-Gapped Environment - Specification

## Project Overview

Setup Dify with bundled official plugins for air-gapped/offline deployment.

## Features

### 1. Dify Docker Checkout
1. Copy and run shell commands in `README.md`
1. Download archive zip of main branch from dify repo
2. Extract only `docker` directory into `dify-docker`
3. Copy `.env.example` to `.env`
4. Remove downloaded archive file

### 2. Dify Official Plugin Package
1. Run `difypkg.sh <plugin-path>`. Example `./difypkg.sh models/openai_api_compatible`
2. Download archive zip of main branch from `langgenius/dify-official-plugins` repo
3. Extract argumented directories into `plugins/` directory
4. Create `<plugin-path>.difypkg` file

### 3. Configure docker-compose.yaml
1. Add environment variables for local plugin loading:
   - `FORCE_VERIFYING_SIGNATURE=false`
   - `ENFORCE_LANGGENIUS_PLUGIN_SIGNATURES=false`
   - `PLUGIN_MAX_PACKAGE_SIZE=524288000`
   - `NGINX_CLIENT_MAX_BODY_SIZE=500M`

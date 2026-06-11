#!/bin/bash

set -e

# http_proxy=

echo
mkdir -p .tmp
pw=$(echo 'admin' | base64)
curl -X POST "http://dify.local/console/api/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@email.com","password":"YWRtaW4K","language":"ko-KR","remember_me":false}' \
  -c .tmp/cookies.txt -v

# https://github.com/langgenius/dify/blob/main/api/controllers/console/workspace/plugin.py#L306-L321
echo
csrf=$(awk '$6 == "csrf_token" {print $7}' .tmp/cookies.txt)
curl -X POST "http://dify.local/console/api/workspaces/current/plugin/upload/pkg" \
  -H "X-Csrf-Token: $csrf" \
  -F "pkg=@openai_api_compatible-offline.difypkg" \
  -b .tmp/cookies.txt -v \
  -o .tmp/res.json

echo
csrf=$(awk '$6 == "csrf_token" {print $7}' .tmp/cookies.txt)
plugin_id=$(j  -r '.unique_identifier' .tmp/res.json)
curl -X POST "http://dify.local/console/api/workspaces/current/plugin/install/pkg" \
  -H "X-Csrf-Token: $csrf" \
  -H "Content-Type: application/json" \
  -d "{\"plugin_unique_identifiers\":[\"$plugin_id\"]}" \
  -b .tmp/cookies.txt -v

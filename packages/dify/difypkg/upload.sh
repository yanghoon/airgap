#!/bin/bash

set -e

# http_proxy=

echo
curl -X POST "http://dify.local/console/api/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@email.com","password":"admin","language":"ko-KR","remember_me":false}' \
  -c cookies.txt -i

# https://github.com/langgenius/dify/blob/main/api/controllers/console/workspace/plugin.py#L306-L321
echo
csrf=$(awk '$6 == "csrf_token" {print $7}' cookies.txt)
curl -X POST "http://dify.local/console/api/workspaces/current/plugin/upload/pkg" \
  -H "X-Csrf-Token: $csrf" \
  -F "pkg=@openai_api_compatible-offline.difypkg" \
  -b cookies.txt -i

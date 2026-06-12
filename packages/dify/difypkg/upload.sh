#!/bin/bash

set -e

# http_proxy=

# vars
pkg="${1:?Require plugin file. (ex. 'openai_api_compatible-offline.difypkg')}"
tmp_path=.tmp
cookie="$tmp_path/cookies.txt"
res="$tmp_path/res.json"

mkdir -p .tmp

echo
pw=$(base64 <<< $(read -s -p "password: "))
curl -X POST "http://dify.local/console/api/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@email.com","password":"YWRtaW4K","language":"ko-KR","remember_me":false}' \
  -c "$cookie" -v

# https://github.com/langgenius/dify/blob/main/api/controllers/console/workspace/plugin.py#L306-L321
echo
csrf=$(awk '$6 == "csrf_token" {print $7}' $cookie)
curl -X POST "http://dify.local/console/api/workspaces/current/plugin/upload/pkg" \
  -H "X-Csrf-Token: $csrf" \
  -F "pkg=@$pkg" \
  -b "$cookie" -v \
  -o "$res"

echo
csrf=$(awk '$6 == "csrf_token" {print $7}' $cookie)
plugin_id=$(j  -r '.unique_identifier' $res)
curl -X POST "http://dify.local/console/api/workspaces/current/plugin/install/pkg" \
  -H "X-Csrf-Token: $csrf" \
  -H "Content-Type: application/json" \
  -d "{\"plugin_unique_identifiers\":[\"$plugin_id\"]}" \
  -b "$cookie" -v

#!/bin/bash

curl -X GET http://bifrost.local:8080/v1/models \
  --resolve bifrost.local:8080:127.0.0.1 \
  -H "Content-Type: application/json" \
  -i

#!/bin/bash

curl -X POST http://localhost:4000/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "custom/auto",
    "messages": [
      {"role": "user", "content": "Hello!"}
    ]
  }' -i

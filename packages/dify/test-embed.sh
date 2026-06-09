#!/bin/bash

curl http://localhost:4000/v1/embeddings \
    -X POST \
    -H "Content-Type: application/json" \
    -d '{
      "model": "embedding/multilingual-e5-small",
      "input": "테스트 문장입니다."
    }' -i
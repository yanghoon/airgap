#!/bin/bash

#   1. Namespace( default ) 생성:
curl -X POST http://iceberg-rest.local/v1/namespaces -H "Content-Type: application/json" -d '{"namespace": ["default"]}'                                        
                                                                                                                                                                    
# 2. Table( iceberg ) 생성:
curl -X POST http://iceberg-rest.local/v1/namespaces/default/tables \
  -H "Content-Type: application/json" \
  -d '{"name": "iceberg", "schema": {"type": "struct", "fields": [{"id": 1, "name": "log_line", "type": "string", "required": false}, {"id": 2, "name": "insert_datetime", "type": "timestamp", "required": false}]}, "properties": {"format-version": "2"}}'

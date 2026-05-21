# Bifrost

## Usage

```bash
docker compose up -d
```

**UI**

1. Open http://localhost:4000/
2. Model > Model Providers
3. Add New Provider > OpenRouter > Add new key
<!--
4. Add New Provider > Custom Provider > Save
    a. Base Format : OpenAI
    b. Base Url : https://your-llm-service:port/vi/
    c. Allowed Request Types: Text Completion(+Stream), Chat Completion(+Stream)
4-1. Add new key
    a. API Key : your-api-key
    b. 
-->

**CLI**

```bash
npx -y @maximhq/bifrost-cli
```

```bash
bifrost
# Configuration
#   - Base URL : http://localhost:4000
#   - Harness : Opencode
#   - Model : openrouter/openrouter/auto
#   - Vietual Key : no
# 
# Edit Session
#   - Ctrl + b
#   - e : edit session
#   - m : model
```

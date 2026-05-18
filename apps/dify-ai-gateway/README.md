# Bifrost AI Gateway - 설정 가이드

Bifrost는 OpenAI-compatible API를 제공하는 고성능 LLM 게이트웨이입니다. OpenRouter 및 커스텀 사내 모델을 단일 엔드포인트로 통합할 수 있습니다.

## 파일 구조

```
dify-ai-gateway/
├── compose.yaml          # Docker Compose 설정
├── config.json           # Bifrost 프로바이더 설정
├── .env                  # 환경변수 (API 키)
├── .env.example          # 환경변수 템플릿
└── bifrost-data/         # 데이터 저장소 (자동 생성)
```

## 빠른 시작

### 1. 환경변수 설정

```bash
# .env.example을 .env로 복사
cp .env.example .env

# .env 파일을 편집하여 API 키 입력
# OPENROUTER_API_KEY=sk-or-v1-your-key-here
```

### 2. 컨테이너 실행

```bash
docker compose up -d

# 상태 확인
docker compose ps

# 로그 확인
docker compose logs -f
```

### 3. Health Check

```bash
curl http://localhost:8080/health
# {"status":"ok","components":{"db_pings":"ok"}}
```

## API 호출 방법

### 기본 형태

```
model 형식: {provider}/{model-id}
예: openrouter/deepseek/deepseek-v4-flash:free
```

### curl 예시

```bash
# OpenRouter 모델 호출
curl -X POST http://localhost:8080/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "openrouter/deepseek/deepseek-v4-flash:free",
    "messages": [{"role": "user", "content": "Hello!"}],
    "max_tokens": 100
  }'

# 응답 예시
{
  "id": "gen-xxxxxx",
  "choices": [{
    "message": {"role": "assistant", "content": "Hi!"}
  }],
  "usage": {...}
}
```

## 사용 가능한 모델 확인

```bash
# OpenRouter API로 직접 확인
curl -s https://openrouter.ai/api/v1/models \
  -H "Authorization: Bearer YOUR_API_KEY" | \
  python3 -c "import json,sys; [print(m['id']) for m in json.load(sys.stdin)['data'][:20]]"
```

## 주요 설정 설명

### config.json 구조

| 필드 | 설명 |
|------|------|
| `providers.{name}.keys` | API 키 및 모델 설정 |
| `providers.{name}.network_config.base_url` | 엔드포인트 URL |
| `providers.{name}.network_config.extra_headers` | 커스텀 헤더 (인증 등) |
| `providers.{name}.custom_provider_config` | 프로바이더별 세부 설정 |

### 커스텀 헤더 예시 (company-llm)

```json
"extra_headers": {
  "Authorization": "Bearer ${COMPANY_LLM_API_KEY}",
  "X-Company-Auth": "${COMPANY_AUTH_TOKEN}",
  "X-Company-Team": "${COMPANY_TEAM_ID}"
}
```

## 관리 UI

브라우저에서 http://localhost:8080 에 접속하여 웹 UI를 통해 프로바이더, 라우팅 규칙, 가상 키 등을 관리할 수 있습니다.

## 프로바이더 추가/수정

### Web UI 사용
1. http://localhost:8080 접속
2. Model Providers → Add New Provider

### config.json 직접 편집
1. `config.json` 수정
2. `docker compose restart bifrost`로 재시작

## 라우팅 규칙 생성 (선택사항)

특정 모델명을 내부 모델로 매핑할 수 있습니다:

```bash
curl -X POST http://localhost:8080/api/governance/routing-rules \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-model-route",
    "scope": "global",
    "cel_expression": "model == \"my-model\"",
    "priority": 0,
    "targets": [{
      "provider": "openrouter",
      "model": "deepseek/deepseek-v4-flash:free",
      "weight": 1.0
    }]
  }'
```

이후 `model: "my-model"`로 요청 가능

## 문제 해결

### API 호출 시 404 에러
- 모델 ID가 정확한지 확인 (OpenRouter 모델 포맷 확인)
- `openrouter/{provider}/{model}` 형식 사용

### Virtual Key 관련 에러
- 요청 시 Authorization 헤더에 API 키가 아닌 Bifrost VK 사용
- VK는 웹 UI에서 생성 가능

### 설정 파일 적용 안됨
```bash
docker compose restart bifrost
```

## Links

- [Bifrost 문서](https://docs.getbifrost.ai/)
- [GitHub](https://github.com/maximhq/bifrost)
- [OpenRouter](https://openrouter.ai/)
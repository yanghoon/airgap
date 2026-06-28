# 🚀 Dify ↔ Langfuse 회귀 테스트

## 1. 환경 설정
```bash
pip install requests langfuse
```

## 2. API 키 및 환경 변수 설정 (run_eval.py 상단)

```python
LANGFUSE_PUBLIC_KEY = "pk-lf-..."
LANGFUSE_SECRET_KEY = "sk-lf-..."
LANGFUSE_HOST = "[http://langfuse.local](http://langfuse.local)"

DIFY_API_URL = "[http://dify.local/v1/chat-messages](http://dify.local/v1/chat-messages)"
DIFY_API_KEY = "app-..."

DATASET_NAME = "your_dataset"
RUN_NAME = "your_experiment"
```

## 3. 실행 방법

```bash
python dify_testing_v2.py
```

## 4. 결과 확인 방법

1. Langfuse 대시보드(`http://langfuse.local`)에 접속합니다.
2. `Datasets` > `your_dataset` > `Runs` > `your_experiment` 접속.
3. 결과(평균 점수, 통과 여부, 오답 사유)를 확인합니다.

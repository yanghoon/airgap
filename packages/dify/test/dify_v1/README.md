# Dify Agent Evaluation with Langfuse

이 프로젝트는 로컬에 구축된 Dify 에이전트를 Langfuse의 데이터셋을 활용하여 자동으로 테스트하고, Langfuse의 빌트인 정확성(Exactness) 평가기를 통해 결과를 채점하기 위한 Python 평가 스크립트(`run_eval.py`)입니다.

## 🌟 주요 기능
- **Langfuse Dataset 연동**: Langfuse에 저장된 특정 데이터셋(`dataset/without-rag-and-schema`) 항목을 자동으로 불러옵니다.
- **Dify Agent 자동 호출**: 데이터셋의 쿼리(User Message)를 Dify Chat Message API로 전송하고 응답을 받습니다.
- **Trace 및 Evals 트리거**: 실행 결과를 Langfuse의 `Traces` 와 `Generations`에 기록하여 Langfuse UI에서 설정한 Managed Evaluators(LLM-as-a-judge)가 자동으로 정확성을 채점할 수 있도록 연동합니다.

---

## 🛠️ 사전 준비 (Prerequisites)

1. **Python 3.x**
2. **Langfuse 및 Dify 로컬 환경**
   - Langfuse: `http://langfuse.local`
   - Dify: `http://dify.local`
3. **API Keys**
   - **Langfuse**: Secret Key (`sk-lf-...`) 및 Public Key (`pk-lf-...`)
   - **Dify**: App API Key (`app-...`)

---

## 🚀 설치 및 사용법 (Usage)

### 1. 가상 환경 구성 및 패키지 설치
스크립트 실행을 위해 파이썬 가상환경을 만들고 필요한 라이브러리(`requests`)를 설치합니다.

```bash
# 가상 환경 생성
python3 -m venv venv

# 가상 환경 활성화 (Mac/Linux)
source venv/bin/activate

# 필수 패키지 설치
pip install requests
```

### 2. 스크립트 실행
가상 환경이 활성화된 상태에서 아래 명령어를 통해 스크립트를 실행합니다.

```bash
python run_eval.py
```

### 3. 실행 결과 예시
스크립트가 실행되면 데이터셋의 각 항목을 순회하며 Dify에 질문을 던지고, 결과를 Langfuse에 기록합니다.

```text
==================================================
🚀 Starting Dify Agent Evaluation
Dataset : dataset/without-rag-and-schema
Run Name: Dify-Eval-Run-1782639144
==================================================
✅ Dataset fetched successfully. Total items: 1

[Item ID: f039eeda-60a0-4d28-ad72-cd9f86c8019a]
💬 Query : 안녕 이름이 뭐야?
🤖 Answer: 안녕! 나는 구글에서 훈련된 파이리야. 😊
✅ Trace & Generation created in Langfuse.
==================================================
🎉 Evaluation Script completed! (1/1 success)
```

---

## 📊 결과 확인 방법 (Langfuse UI)

스크립트 실행이 완료된 후, Langfuse 대시보드(`http://langfuse.local`)에서 평가 결과를 확인할 수 있습니다.

1. **Trace 기록 확인**
   - 메뉴 좌측의 **[Traces]** 탭으로 이동합니다.
   - `Dify-Eval-Run-...` 이름으로 생성된 최신 Trace를 클릭합니다.
   - Trace 내부에서 Dify로 전송된 Input과 반환된 Output(Generation)을 상세히 확인할 수 있습니다.

2. **빌트인 평가기(Evals) 채점 결과 확인**
   - 스크립트는 생성된 Trace에 `dataset-eval` 이라는 태그를 자동으로 부여합니다.
   - Langfuse UI (`Evals` -> `Config`)에서 해당 태그를 트리거로 사용하도록 Exactness(정확성) 평가기를 설정해 둔 경우, 평가기가 백그라운드에서 자동으로 채점을 수행합니다.
   - Trace 상세 페이지의 **[Scores]** 탭을 클릭하면 LLM(예: openrouter)이 평가한 정확성 점수를 확인할 수 있습니다.

---

## ⚠️ 알려진 사항 (Known Limitations)
- 구버전의 로컬 Langfuse 환경에서는 Dataset Run API(`POST /api/public/dataset-run-items`)를 지원하지 않아 404 에러가 발생할 수 있습니다. 
- 본 스크립트는 이러한 API 호환성 문제를 방지하기 위해 SDK 대신 **REST API를 직접 호출**하며, 에러를 안전하게 건너뛰고 Trace 기록을 최우선으로 저장하도록 설계되었습니다. (Trace 목록 및 Evals 태그 기능은 정상적으로 동작합니다.)

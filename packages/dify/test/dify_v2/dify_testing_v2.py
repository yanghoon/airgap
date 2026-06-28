import requests
from langfuse import Langfuse

# ==========================================
# 1. 환경 변수 및 설정
# ==========================================
LANGFUSE_PUBLIC_KEY = "pk-lf-..."
LANGFUSE_SECRET_KEY = "sk-lf-..."
LANGFUSE_HOST = "http://langfuse.local"

DIFY_API_URL = "http://dify.local/v1/chat-messages"
DIFY_API_KEY = "app-..."

DATASET_NAME = "사내_Git_VOC_데이터셋"
RUN_NAME = "회귀테스트_v1.0"

# ==========================================
# 2. 개별 동작 함수 분리 (단일 책임)
# ==========================================

def extract_question(item) -> str:
    """
    [역할] 데이터셋 아이템의 복잡한 JSON 구조에서 사용자 질문 텍스트만 빼냅니다.
    """
    try:
        return item.input["message"][1]["text"]
    except (KeyError, TypeError, IndexError):
        return str(item.input)

def call_dify_agent(query_text: str) -> str:
    """
    [역할] Dify 에이전트에게 질문을 던지고, 생성된 Trace ID(message_id)를 반환합니다.
    """
    payload = {
        "inputs": {},
        "query": query_text,
        "response_mode": "blocking",
        "user": "langfuse-experiment-runner"
    }
    headers = {
        "Authorization": f"Bearer {DIFY_API_KEY}",
        "Content-Type": "application/json"
    }

    response = requests.post(DIFY_API_URL, json=payload, headers=headers)
    response.raise_for_status()  # HTTP 에러 발생 시 예외(Exception) 처리

    result = response.json()
    trace_id = result.get("message_id")
    
    return trace_id

def link_trace_to_dataset(item, trace_id: str):
    """
    [역할] Dify의 실행 결과(Trace ID)를 Langfuse의 특정 문제(Dataset Item)에 연결합니다.
    """
    item.link(
        trace_id=trace_id,
        run_name=RUN_NAME
    )

# ==========================================
# 3. 메인 오케스트레이터 (실행 흐름)
# ==========================================

def main():
    print(f"🚀 [{RUN_NAME}] 파이프라인을 시작합니다...\n")

    # 1. Langfuse 클라이언트 셋업 및 시험지(Dataset) 로드
    langfuse = Langfuse(
        public_key=LANGFUSE_PUBLIC_KEY,
        secret_key=LANGFUSE_SECRET_KEY,
        host=LANGFUSE_HOST
    )
    dataset = langfuse.get_dataset(DATASET_NAME)
    total_items = len(dataset.items)
    
    print(f"📋 총 {total_items}개의 테스트 시나리오를 찾았습니다.\n")

    # 2. 문제 풀이 및 자동 채점 루프
    for index, item in enumerate(dataset.items, start=1):
        
        # [단계 A] 문제 추출
        query_text = extract_question(item)
        print(f"[{index}/{total_items}] 질문: {query_text}")

        try:
            # [단계 B] 에이전트 호출 (문제 풀기)
            trace_id = call_dify_agent(query_text)
            print(f"  ✅ 에이전트 답변 완료 (Trace ID: {trace_id})")

            # [단계 C] 답안지 제출 및 채점 트리거
            link_trace_to_dataset(item, trace_id)
            print(f"  🔗 Langfuse 매칭 완료")

        except Exception as e:
            # 에러가 나도 다음 문제로 넘어갈 수 있도록 예외 처리
            print(f"  ❌ 실행 중 오류 발생: {e}")
            continue

    print(f"\n🎉 파이프라인 종료! Langfuse 대시보드의 '{DATASET_NAME} -> Runs' 에서 결과를 확인하세요.")

# 파이썬 실행 진입점
if __name__ == "__main__":
    main()

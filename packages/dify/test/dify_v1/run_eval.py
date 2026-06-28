import os
import requests
from requests.auth import HTTPBasicAuth
import urllib.parse
import uuid
from datetime import datetime, timezone

# 1. 환경 변수 및 설정
LANGFUSE_HOST = "http://langfuse.local"
LANGFUSE_PK = "pk-lf-167618d1-62c8-406d-b859-bd371e94f2d6"
LANGFUSE_SK = "sk-lf-1c6cadee-8a5a-4b18-b457-9ceee5437418"
auth = HTTPBasicAuth(LANGFUSE_PK, LANGFUSE_SK)

DATASET_NAME = "dataset/without-rag-and-schema"
DIFY_URL = "http://dify.local/v1/chat-messages"
DIFY_API_KEY = "app-6KpsqR5IUjxFjD7VBcUozCzh"
RUN_NAME = f"Dify-Eval-Run-{int(datetime.now(timezone.utc).timestamp())}"

print("="*50)
print(f"🚀 Starting Dify Agent Evaluation")
print(f"Dataset : {DATASET_NAME}")
print(f"Run Name: {RUN_NAME}")
print("="*50)

# 2. Dataset 가져오기
encoded_name = urllib.parse.quote(DATASET_NAME, safe='')
items_url = f"{LANGFUSE_HOST}/api/public/dataset-items?datasetName={encoded_name}"
resp = requests.get(items_url, auth=auth)
resp.raise_for_status()
dataset_items = resp.json().get('data', [])
print(f"✅ Dataset fetched successfully. Total items: {len(dataset_items)}")

# 3. 데이터셋 순회하며 Dify API 호출 및 결과 기록
success_count = 0
for item in dataset_items:
    item_id = item.get("id")
    input_data = item.get("input", {})
    expected_output = item.get("expectedOutput", "")
    
    # user 메시지 추출
    messages = input_data.get("message", [])
    user_msg = next((msg["text"] for msg in reversed(messages) if msg.get("role") == "user"), "")
    
    if not user_msg:
        continue

    print(f"\n[Item ID: {item_id}]")
    print(f"💬 Query : {user_msg}")

    # Dify 호출
    dify_headers = {
        "Authorization": f"Bearer {DIFY_API_KEY}",
        "Content-Type": "application/json"
    }
    payload = {
        "inputs": {},
        "query": user_msg,
        "response_mode": "blocking",
        "user": "langfuse-eval-tester"
    }
    
    try:
        dify_resp = requests.post(DIFY_URL, headers=dify_headers, json=payload)
        dify_resp.raise_for_status()
        dify_result = dify_resp.json()
        answer = dify_result.get("answer", "")
        print(f"🤖 Answer: {answer}")
        
        # 4. Langfuse API 호출 (Trace, Generation 생성)
        trace_id = str(uuid.uuid4())
        generation_id = str(uuid.uuid4())
        
        # Trace 생성 (평가 트리거를 위한 태그 추가)
        trace_payload = {
            "id": trace_id,
            "name": RUN_NAME,
            "tags": ["dataset-eval"]
        }
        requests.post(f"{LANGFUSE_HOST}/api/public/traces", auth=auth, json=trace_payload).raise_for_status()
        
        # Generation 생성 (trace에 속함)
        generation_payload = {
            "id": generation_id,
            "traceId": trace_id,
            "name": "dify-agent-generation",
            "startTime": datetime.now(timezone.utc).isoformat(),
            "model": "dify-app",
            "input": user_msg,
            "output": answer
        }
        requests.post(f"{LANGFUSE_HOST}/api/public/generations", auth=auth, json=generation_payload).raise_for_status()
        print("✅ Trace & Generation created in Langfuse.")
        
        # Langfuse 서버가 Generation을 비동기(Kafka)로 DB에 반영할 때까지 약간의 대기가 필요할 수 있습니다.
        import time
        time.sleep(2)
        
        # Dataset Run Item 생성 (Dataset Item과 Generation 연결)
        run_item_payload = {
            "datasetItemId": item_id,
            "observationId": generation_id,
            "runName": RUN_NAME
        }
        
        # 재시도 로직 추가 (최대 3회)
        max_retries = 3
        linked = False
        for attempt in range(max_retries):
            run_resp = requests.post(f"{LANGFUSE_HOST}/api/public/dataset-run-items", auth=auth, json=run_item_payload)
            if run_resp.status_code == 200:
                print("✅ Successfully linked to Dataset Run.")
                linked = True
                break
            elif run_resp.status_code == 404:
                print(f"⏳ Observation not yet found by Langfuse DB, retrying ({attempt+1}/{max_retries})...")
                time.sleep(2)
            else:
                run_resp.raise_for_status()
                
        if not linked:
             print("⚠️ Failed to link to Dataset Run after retries.")
            
        success_count += 1
        
    except Exception as e:
        print(f"❌ Error during evaluation: {e}")

print("="*50)
print(f"🎉 Evaluation Script completed! ({success_count}/{len(dataset_items)} success)")
print(f"👉 Check Langfuse UI (Traces tagged with 'dataset-eval').")

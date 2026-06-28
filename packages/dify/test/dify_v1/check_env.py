import urllib.request
import urllib.error
import urllib.parse
import json
import base64

# 1. Langfuse API Check
LANGFUSE_HOST = "http://langfuse.local"
LANGFUSE_SK = "sk-lf-1c6cadee-8a5a-4b18-b457-9ceee5437418"
LANGFUSE_PK = "pk-lf-167618d1-62c8-406d-b859-bd371e94f2d6"
DATASET_NAME = "dataset/without-rag-and-schema"

def make_request(url, headers=None):
    if headers is None:
        headers = {}
    req = urllib.request.Request(url, headers=headers)
    try:
        with urllib.request.urlopen(req) as response:
            return response.getcode(), json.loads(response.read().decode('utf-8'))
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode('utf-8')
    except Exception as e:
        return 0, str(e)

print("--- Checking Langfuse API ---")
try:
    auth_str = f"{LANGFUSE_PK}:{LANGFUSE_SK}"
    auth_encoded = base64.b64encode(auth_str.encode('utf-8')).decode('ascii')
    headers = {
        "Authorization": f"Basic {auth_encoded}",
        "Content-Type": "application/json"
    }

    encoded_name = urllib.parse.quote(DATASET_NAME, safe='')
    dataset_url = f"{LANGFUSE_HOST}/api/public/datasets/{encoded_name}"
    
    status, data = make_request(dataset_url, headers)
    
    if status == 200:
        print(f"✅ Dataset '{DATASET_NAME}' found.")
        
        # Fetch items
        items_url = f"{LANGFUSE_HOST}/api/public/dataset-items?datasetName={encoded_name}"
        item_status, item_data = make_request(items_url, headers)
        if item_status == 200:
            items = item_data.get('data', [])
            print(f"✅ Found {len(items)} items in the dataset.")
            if len(items) > 0:
                print(f"   Example Item Keys: {list(items[0].keys())}")
                if 'expected_output' in items[0]:
                    print(f"   ✅ 'expected_output' field exists for Exactness eval.")
                else:
                    print(f"   ⚠️ 'expected_output' field is missing in the dataset items. Exactness evaluator usually needs expected output.")
        else:
            print(f"❌ Failed to fetch dataset items. Status: {item_status}, {item_data}")
    else:
        print(f"❌ Dataset '{DATASET_NAME}' NOT found or error. Status: {status}, {data}")
except Exception as e:
    print(f"❌ Error connecting to Langfuse: {e}")


# 2. Dify API Check
DIFY_URL = "http://dify.local/v1"
DIFY_API_KEY = "app-6KpsqR5IUjxFjD7VBcUozCzh"

print("\n--- Checking Dify API ---")
try:
    dify_headers = {
        "Authorization": f"Bearer {DIFY_API_KEY}",
        "Content-Type": "application/json"
    }
    # Using the /parameters endpoint to check if the app key is valid
    dify_status, dify_data = make_request(f"{DIFY_URL}/parameters", dify_headers)
    if dify_status == 200:
        print("✅ Dify App API connected successfully. App parameters retrieved.")
    else:
        print(f"❌ Failed to connect to Dify API. Status: {dify_status}, {dify_data}")
except Exception as e:
    print(f"❌ Error connecting to Dify: {e}")

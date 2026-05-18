import os
import sys
import zipfile
import subprocess
import requests

def pack_offline():
    """1. 외부 패키지(Wheels)를 다운로드하고 오프라인 플래그를 삽입한 뒤 .difypkg로 압릭합니다."""
    print("[*] Downloading dependencies and packaging...")
    os.makedirs("wheels", exist_ok=True)
    
    if os.path.exists("requirements.txt"):
        subprocess.run(["pip", "download", "-r", "requirements.txt", "-d", "wheels"], check=True)
        with open("requirements.txt", "r+") as f:
            content = f.read()
            f.seek(0, 0)
            f.write("--no-index\n--find-links wheels\n" + content)
            
    elif os.path.exists("pyproject.toml"):
        subprocess.run(["pip", "download", ".", "-d", "wheels"], check=True)
        with open("pyproject.toml", "a") as f:
            f.write("\n[tool.uv]\nno-index = true\nfind-links = [\"wheels\"]\n")
    
    pkg_name = f"{os.path.basename(os.getcwd())}_offline.difypkg"
    with zipfile.ZipFile(pkg_name, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, _, files in os.walk("."):
            if ".git" in root or pkg_name in files: continue
            for file in files:
                fp = os.path.join(root, file)
                zipf.write(fp, os.path.relpath(fp, "."))
                
    print(f"=========> [Success] Generated offline package: {pkg_name}")
    return pkg_name

def upload_to_dify(dify_url, token, pkg_path):
    """2. Dify API를 통해 에어갭 환경 내부의 Dify 서버에 패키지를 업로드/업데이트합니다."""
    print(f"[*] Uploading {pkg_path} to {dify_url}...")
    # Dify 버전에 따라 /plugins/upload 또는 /plugins/install-local 사용
    url = f"{dify_url.rstrip('/')}/console/api/plugins/upload"
    headers = {"Authorization": f"Bearer {token}"}
    
    with open(pkg_path, "rb") as f:
        files = {"file": (os.path.basename(pkg_path), f, "application/zip")}
        res = requests.post(url, headers=headers, files=files)
        
    if res.status_code in [200, 201]:
        print("[Success] Plugin successfully updated in Dify!")
    else:
        print(f"[Error] Failed to upload: {res.status_code} - {res.text}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage:\n  python dify_airgap.py pack\n  python dify_airgap.py upload <DIFY_URL> <CONSOLE_TOKEN> <PKG_PATH>")
        sys.exit(1)
        
    mode = sys.argv[1]
    if mode == "pack":
        pack_offline()
    elif mode == "upload":
        upload_to_dify(sys.argv[2], sys.argv[3], sys.argv[4])

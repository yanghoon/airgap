
```bash
# 1. 빈 디렉토리 생성 및 이동
mkdir dify-docker && cd dify-docker

# 2. Git 저장소 초기화
git init

# 3. 원격(Dify 공식 레포지토리) 주소 등록
git remote add -f origin https://github.com/langgenius/dify.git

# 4. Sparse-checkout 기능 활성화
git sparse-checkout init --cone

# 5. 다운로드할 특정 폴더(docker) 지정
git sparse-checkout set docker

# 6. main 브랜치로부터 해당 폴더만 Pull
git pull origin main
```
# Opentelemetry Collector

### Debug - `kubectl sniff` and Wireshark
```bash
# Mac: curl -Lo ksniff.zip https://github.com/eldadru/ksniff/releases/latest/download/ksniff-darwin-amd64.zip
curl -Lo ksniff.zip https://github.com/eldadru/ksniff/releases/latest/download/ksniff-linux-amd64.zip

# 2. 압축 풀기
unzip ksniff.zip

# 3. 플러그인 파일을 환경 변수 PATH가 잡힌 시스템 경로(/usr/local/bin)로 이동
sudo mv kubectl-sniff /usr/local/bin/

# 4. 실행 권한 부여
sudo chmod +x /usr/local/bin/kubectl-sniff

# 5. 설치 확인
kubectl sniff --help
```

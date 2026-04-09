# Restic 설치/복구 스크립트 SPEC

## 환경
- 대상 OS는 RHEL8, Ubuntu22
- 모든 출력은 영문으로
- 로스크리븥 가독성을 위해 기능별로 함수화
- restic v0.18.1
- restric 설치 위치는 /usr/local/bin (sudo 지원)
- restic env 파일 위치는 ~/.restic_env 경로에 600 권한으로 관리
- base64 파일 위치는 ./bootstrap/restic_${VERSION}.base64

## Shell Sub-Commands
- `archive`
  - 스크립트에 고정된 버전 출력. 사용자 입력이 없으면 기본값 사용
  - 스크립트와 동일 경로에 다운로드. 다운로드된 파일이 압축되어 있을 경우, 압축 해제하고 tgz 파일로 재압축. base64단독 파일로 저장.
  - base64 파일은 archive 실행 시점에 상관 없이 동일 버전은 동일한 파일이 생성되어야 함
  - Download URL : https://github.com/restic/restic/releases/download/v${VERESION}/restic_${VERSION}_linux_amd64.bz2
- `install`
  - base64로 저장된 restic binary의 설치, base64 파일은 유지
  - 설치된 restic binary를 실행해 버전 출력
  - 설치 위치를 PATH에 지정하는 가이드 출력 (설치 경로 변경으로 불필요)
- `config`
  - 사용자 직접 입력 방식으로 S3 restic repo 환경 변수 파일 생성
  - 파일이 존재하는 경우, 기존 값을 출력해주고 사용자 입력이 없는 경우 기존 값 사용. 파일이 없는 경우에만 샘플값 출력
  - `RESTIC_REPOSITORY`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_DEFAULT_REGION`, `NO_PROXY` (기본 값은 repo의 host)
- ~~`config-load` : 현재 터미널에 env 환경변수 로드 (wapper script 없이 불가능. source 방식으로 직접 입력)~~

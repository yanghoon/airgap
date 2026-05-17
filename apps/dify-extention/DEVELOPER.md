# 🛠️ 엔지니어용 익스텐션 개발 가이드 (Developer Guide)

본 문서는 **Dify Agent Sidepanel Extension**의 내부 아키텍처 설계 구조, 코드 스타일에 대한 컨벤션, 그리고 다중 실행 컨텍스트(Multi-context) 디버깅 전략을 다룹니다.

---

## 1. 아키텍처 상세 (Architectural Deep Dive)

Manifest V3(MV3) 환경의 핵심 제약인 **비정기적 서비스 워커 수명 주기**와 **엄격한 콘텐츠 보안 정책(CSP)**을 해결하기 위해 본 프로젝트는 메시징 패킷 전달 방식이 아닌 **반응형 스토리지 구독(Reactive Storage Subscriptions)** 모델을 채택했습니다.

### 데이터 흐름 및 리스너 제어 메커니즘
1. **Sidepanel (`main.js`)** 오픈 시 `wxt/storage` 인프라를 통해 `local:is_sidepanel_active` 값을 `true`로 갱신합니다. 언로드(`unload`) 시 즉시 `false`로 돌려놓습니다.
2. **Content Script (`content.js`)**는 페이지가 로드될 때 상시 실행되지만, 백그라운드 연산을 하지 않습니다. 오직 스토리지를 구독(`sidepanelState.watch`)하고 있다가, 상태 변화에 따라 DOM 리스너를 동적으로 `add` / `remove` 합니다.

이 구조 덕분에 사용자가 익스텐션을 쓰지 않을 때 호스트 웹페이지의 CPU 및 메모리 자원을 0%에 가깝게 유지할 수 있습니다.

---

## 2. 개발 환경 및 주요 명령어

WXT 프레임워크 기반의 빌드 파이프라인 엔진을 사용합니다.

```bash
# 1. 의존성 패키지 설치
npm install

# 2. 로컬 개발 서버 구동 (임시 크롬 세션 자동 오픈 및 핫 리로드 활성화)
npm run dev

# 3. 프로덕션 빌드 (압축 해제된 확장 프로그램 빌드 결과물 생성)
npm run build

# 4. 내부 배포용 프로덕션 ZIP 패키징 (.output 디렉토리에 생성)
npm run zip

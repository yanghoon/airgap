# [Tech Spec] Dify Agent Sidepanel 통신 및 상태 관리 시스템 설계

## 1. Context (배경)
사내 업무 생산성 향상을 위해 임베딩된 Dify Agent와의 상호작용이 잦아짐에 따라 웹 화면의 특정 텍스트 요소를 신속하게 정제 및 획득할 수 있는 크롬 확장 프로그램의 필요성이 대두됨. 
특히, Chrome Manifest V3(MV3) 환경의 보안 정책(CSP) 강화와 백그라운드 서비스 워커의 비정기적 라이프사이클(Lifecycle) 제약 조건을 충족하면서도, 브라우저 성능에 부하를 주지 않는 가벼운 이벤트 핸들링 설계가 요구됨.

## 2. Goals & Non-Goals
### Goals
* **Disk-First 구조 지향**: 비정기적으로 정지되는 MV3 환경에서 상태 유실을 원천 차단하기 위해 단일 진실 공급원(Single Source of Truth)으로 `chrome.storage`를 활용.
* **이벤트 리스너 동적 제어**: 사용자가 Sidepanel을 열어 스캔 활성화가 된 상태에서만 호스트 웹페이지에 DOM 감지 리스너(`mouseover`, `click`)를 등록함으로써 웹 브라우징 퍼포먼스 낭비를 제어함.
* **보안성 확보**: MV3의 인라인 스크립트 금지 정책에 맞추어 스크립트 결합 아키텍처 구축.

### Non-Goals
* 외부 도메인(Dify) iframe 자체의 내부 비즈니스 로직 제어 및 스타일 직접 변경.
* 사내 전사 인프라 시스템(MDM 등)을 연계한 강제 배포 인프라 구축 기술 지원.

## 3. Architectural Design
### 핵심 아키텍처 모델: Reactive Storage Subscriptions
수동으로 메시징 패킷(`sendMessage`)을 구성해 상태를 주고받는 대신 `wxt/storage` 인프라를 중심으로 둔 상태 구독 모델을 채택함.

```text
[ Sidepanel UI (main.js) ] 
       │ 
       ▼ (setValue)
┌──────────────────────────────┐
│  wxt/storage 싱글톤          │ ◄── [ Single Source of Truth ]
└──────────────────────────────┘
       │
       ▼ (watch event 변경 감지)
[ Content Script (content.js) ]
       │
       ├─► (isActive: true)  ──► Add Event Listeners (DOM 하이라이팅 개시)
       └─► (isActive: false) ──► Remove Event Listeners & Style Cleanup

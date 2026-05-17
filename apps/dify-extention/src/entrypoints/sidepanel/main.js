// import { sidepanelState } from '@/utils/storage.js'

function initSidepanelLifecycle() {
  console.log('[Sidepanel] 패널 로드 완료. 백그라운드 포트 연결을 시도합니다.');

  // 1. 백그라운드와 통신할 포트를 개설합니다. (이름을 백그라운드와 일치시킵니다)
  const port = chrome.runtime.connect({ name: 'dify-sidepanel-channel' });

  // 2. [선택 사항] 이전 세션에서 저장해 둔 스토리지 상태가 있다면 가져와서 로깅/UI 복구
  // storage.getItem<{ isOpen: boolean; updatedAt: number }>('local:panel_status')
  //   .then((status) => {
  //     if (status) {
  //       const lastTime = new Date(status.updatedAt).toLocaleString();
  //       console.log(`[Sidepanel] 직전 패널 상태 데이터 복구 -> 마지막 업데이트: ${lastTime}`);
  //     }
  //   });

  // 3. 백그라운드가 예상치 못하게 죽거나 끊겼을 때의 에러 핸들링 (방어 코드)
  port.onDisconnect.addListener(() => {
    if (chrome.runtime.lastError) {
      console.warn('[Sidepanel] 백그라운드와 연결이 끊겼습니다:', chrome.runtime.lastError.message);
    }
  });
}

initSidepanelLifecycle()

// /**
//  * Sidepanel 수명 주기 진입점
//  */
// function initSidepanel() {
//   // console.log('[Dify Ext] 사이드패널 초기화 시작')
//   sidepanelState.setValue(true)
//   window.addEventListener('unload', releaseSidepanel)
// }

// /**
//  * Sidepanel 닫힘 또는 컨텍스트 유실 시 상태 해제
//  */
// function releaseSidepanel() {
//   // console.log('[Dify Ext] 사이드패널 해제 실행')
//   sidepanelState.setValue(false)
// }

// initSidepanel()

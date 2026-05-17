import { defineBackground } from "#imports"
import { sidepanelState } from '@/utils/storage.js'

export default defineBackground(() => {
  // 아이콘 클릭 시 사이드패널이 열리도록 행동 정의
  chrome.sidePanel
    .setPanelBehavior({ openPanelOnActionClick: true })
    .catch((error) => console.error(error))
  
  chrome.runtime.onConnect.addListener((port) => {
    
    // 포트 이름이 사이드 패널에서 정의한 식별자와 일치하는지 확인
    if (port.name === 'dify-sidepanel-channel') {
      console.log('[Background] 사이드 패널이 열렸습니다. (포트 연결 성공)');
      
      // 1. 열림 상태를 스토리지에 업데이트
      sidepanelState.setValue(true)
      // storage.setItem('local:panel_status', {
      //   isOpen: true,
      //   updatedAt: Date.now()
      // }).catch(err => console.error('스토리지 저장 실패:', err));

      // 2. 포트 연결이 끊어질 때(사이드 패널이 닫힐 때)의 이벤트 리스너 등록
      port.onDisconnect.addListener(() => {
        console.log('[Background] 사이드 패널이 닫혔습니다. (포트 연결 해제)');
        
        // 닫힘 상태와 최종 시간을 스토리지에 업데이트
        sidepanelState.setValue(false)
        // storage.setItem('local:panel_status', {
        //   isOpen: false,
        //   updatedAt: Date.now()
        // }).catch(err => console.error('스토리지 저장 실패:', err));
      });
    }
  });
  
 })

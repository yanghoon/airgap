// import { defineContentScript } from 'wxt/sandbox'
import { defineContentScript } from '#imports'
import { sidepanelState } from '@/utils/storage.js'
import { UI_CONFIG } from '@/utils/constants.js'

// ==========================================
// 1. 이벤트 핸들러 선언 (함수 평탄화 구조)
// ==========================================

const handleMouseOver = (e) => {
  if (e.target) e.target.style.outline = UI_CONFIG.HIGHLIGHT_OUTLINE
}

const handleMouseOut = (e) => {
  if (e.target) e.target.style.outline = ''
}

const handleClick = async (e) => {
  e.preventDefault()
  e.stopPropagation()
  
  const target = e.target
  if (!target) return

  const extractedText = target.innerText.trim()
  await processAndCopyText(extractedText)
}

// ==========================================
// 2. 비즈니스 로직 처리
// ==========================================

const processAndCopyText = async (text) => {
  if (!text) return
  try {
    // 텍스트 정제 (연속된 공백 제거 및 개행 문자 트리밍)
    const sanitizedText = text.replace(/\s+/g, ' ').trim()
    await navigator.clipboard.writeText(sanitizedText)
    console.log('[Dify Ext] 텍스트 정제 및 클립보드 복사 완료')
  } catch (error) {
    console.error('[Dify Ext] 클립보드 접근 실패:', error)
  }
}

// ==========================================
// 3. DOM 리스너 오케스트레이션
// ==========================================

const startScanning = () => {
  document.addEventListener('mouseover', handleMouseOver)
  document.addEventListener('mouseout', handleMouseOut)
  document.addEventListener('click', handleClick, { capture: true })
}

const stopScanning = () => {
  document.removeEventListener('mouseover', handleMouseOver)
  document.removeEventListener('mouseout', handleMouseOut)
  document.removeEventListener('click', handleClick, { capture: true })
  clearRemainingStyles()
}

const clearRemainingStyles = () => {
  document.querySelectorAll(UI_CONFIG.CLEANUP_SELECTOR).forEach((el) => {
    el.style.outline = ''
  })
}

// ==========================================
// 4. 메인 제어 흐름 (한 화면 내 가독성 확보)
// ==========================================
export default defineContentScript({
  matches: ['<all_urls>'],
  main() {
    // [제어 흐름 1] 스토리지 관찰을 통해 사이드패널 개폐 상태 동적 구독
    sidepanelState.watch((isActive) => {
      debugger
      isActive ? startScanning() : stopScanning()
    })

    // [제어 흐름 2] 초기 페이지 컨텍스트 렌더링 시 시점 동기화
    sidepanelState.getValue().then((isActive) => {
      debugger
      if (isActive) startScanning()
    })
  },
})

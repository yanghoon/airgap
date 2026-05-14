// 1. 아이콘 클릭 시 사이드 패널이 열리도록 설정
chrome.sidePanel
  .setPanelBehavior({ openPanelOnActionClick: true })
  .catch((error) => console.error(error));

// (선택 사항) 설치 시점에 초기화 로직을 넣고 싶다면 아래처럼 작성도 가능합니다.
chrome.runtime.onInstalled.addListener(() => {
  console.log('Dify Side Panel Extension Installed');
});
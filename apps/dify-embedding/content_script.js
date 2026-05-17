// let lastElement = null;

// // 1. Hover 시 강조 효과
// document.addEventListener('mouseover', (e) => {
//   if (lastElement) {
//     lastElement.style.outline = ''; // 이전 강조 제거
//   }
//   lastElement = e.target;
//   lastElement.style.outline = '2px dashed #1C64F2'; // 파란색 점선 테두리
//   lastElement.style.cursor = 'copy'; // 커서 모양 변경

// });

// // 2. 클릭 시 텍스트 추출 및 전송
// document.addEventListener('click', (e) => {
//   e.preventDefault(); // 페이지 이동이나 폼 제출 방지
//   e.stopPropagation();

//   const selectedText = e.target.innerText || e.target.value;

//   // 확장 프로그램 내부(Background/SidePanel)로 메시지 전송
//   chrome.runtime.sendMessage({
//     type: 'ELEMENT_CLICKED',
//     text: selectedText
//   });

//   // 복사되었다는 시각적 피드백
//   const originalBackground = e.target.style.backgroundColor;
//   e.target.style.backgroundColor = '#d1e7ff';
//   setTimeout(() => {
//     e.target.style.backgroundColor = originalBackground;
//   }, 200);
// });

// content_script.js
document.addEventListener('mouseover', (e) => {
  // 인라인 스타일로 강제 주입
  e.target.style.setProperty('outline', '3px solid #1C64F2', 'important');
  e.target.style.setProperty('outline-offset', '-3px', 'important');
  
  e.target.addEventListener('mouseout', () => {
    e.target.style.removeProperty('outline');
    e.target.style.removeProperty('outline-offset');
  }, { once: true });
  console.log("선택된 텍스트:", e.target.innerText);
});

document.addEventListener('click', (e) => {
  e.preventDefault();
  e.stopPropagation();
  const text = e.target.innerText;
  console.log("선택된 텍스트:", text);
  chrome.runtime.sendMessage({ type: 'ELEMENT_CLICKED', text: text });
}, true); // true를 주어 캡처링 단계에서 가로챔
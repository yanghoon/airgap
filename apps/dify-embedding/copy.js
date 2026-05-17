  chrome.runtime.onMessage.addListener((message) => {
    if (message.type === 'ELEMENT_CLICKED') {
      console.log("복사된 텍스트:", message.text);
      // 여기서 Dify iframe에 텍스트를 전달하거나 알림을 띄울 수 있습니다.
      alert("에이전트에게 전달할 컨텍스트가 복사되었습니다.");
    }
  });
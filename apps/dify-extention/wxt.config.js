import { defineConfig } from 'wxt'

export default defineConfig({
  srcDir: 'src',
  manifest: {
    // name: "Dify Agent Sidepanel",
    // description: "사이드패널을 통해 화면 요소를 추출하고 Dify Agent와 상호작용합니다.",
    permissions: [
      "sidePanel",
      "storage",
      "tabs"
    ],
    host_permissions: [
      "<all_urls>"
    ],
    // side_panel: {
    //   default_path: "entrypoints/sidepanel/index.html"
    // }
    // browserRunner: {
    //   chromiumArgs: [
    //     // 1. 프로필 데이터를 임시 폴더가 아닌 지정된 프로젝트 내부 폴더에 저장 (세팅 유지 핵심)
    //     '--user-data-dir=./.wxt/chrome-profile',
        
    //     // 2. [보너스] 새 탭이 열릴 때마다 개발자 도구(DevTools)를 자동으로 열어주는 옵션
    //     '--auto-open-devtools-for-tabs'
    //   ],
    // }
    logger: {
      debug: true
    },
  }
})

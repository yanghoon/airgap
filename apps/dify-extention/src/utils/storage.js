// import { storage } from 'wxt/storage';
import { storage } from '#imports'
import { STORAGE_KEYS } from './constants.js'

// Sidepanel의 활성화 상태를 관리하는 Reactive Storage 아이템
export const sidepanelState = storage.defineItem(STORAGE_KEYS.IS_SIDEPANEL_ACTIVE, {
  defaultValue: false,
})

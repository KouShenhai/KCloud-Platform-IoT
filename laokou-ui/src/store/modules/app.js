import storage from 'store'
import {
  SIDE_COLLAPSED,
  TOGGLE_MOBILE_TYPE,
  TOGGLE_NAV_THEME,
  TOGGLE_LAYOUT,
  TOGGLE_FIXED_HEADER,
  TOGGLE_FIXED_SIDEBAR,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_COLOR,
  TOGGLE_WEAK,
  TOGGLE_MULTI_TAB,
  APP_LANGUAGE,
  TABLE_SIZE,
  TABLE_BORDERED,
  HIDE_FOOTER
} from '@/store/mutation-types'
import { loadLanguageAsync } from '@/locales'

const app = {
  state: {
    sideCollapsed: false,
    isMobile: false,
    theme: 'dark',
    layout: '',
    contentWidth: '',
    fixedHeader: false,
    fixedSidebar: false,
    color: '',
    weak: false,
    multiTab: true,
    lang: 'zh-CN',
    _antLocale: {},
    tableSize: 'default',
    tableBordered: false,
    hideFooter: false
  },
  mutations: {
    [SIDE_COLLAPSED]: (state, type) => {
      state.sideCollapsed = type
      storage.set(SIDE_COLLAPSED, type)
    },
    [TOGGLE_MOBILE_TYPE]: (state, isMobile) => {
      state.isMobile = isMobile
    },
    [TOGGLE_NAV_THEME]: (state, theme) => {
      state.theme = theme
      storage.set(TOGGLE_NAV_THEME, theme)
    },
    [TOGGLE_LAYOUT]: (state, mode) => {
      state.layout = mode
      storage.set(TOGGLE_LAYOUT, mode)
    },
    [TOGGLE_FIXED_HEADER]: (state, mode) => {
      state.fixedHeader = mode
      storage.set(TOGGLE_FIXED_HEADER, mode)
    },
    [TOGGLE_FIXED_SIDEBAR]: (state, mode) => {
      state.fixedSidebar = mode
      storage.set(TOGGLE_FIXED_SIDEBAR, mode)
    },
    [TOGGLE_CONTENT_WIDTH]: (state, type) => {
      state.contentWidth = type
      storage.set(TOGGLE_CONTENT_WIDTH, type)
    },
    [TOGGLE_COLOR]: (state, color) => {
      state.color = color
      storage.set(TOGGLE_COLOR, color)
    },
    [TOGGLE_WEAK]: (state, mode) => {
      state.weak = mode
      storage.set(TOGGLE_WEAK, mode)
    },
    [APP_LANGUAGE]: (state, lang, antd = {}) => {
      state.lang = lang
      state._antLocale = antd
      storage.set(APP_LANGUAGE, lang)
    },
    [TOGGLE_MULTI_TAB]: (state, bool) => {
      storage.set(TOGGLE_MULTI_TAB, bool)
      state.multiTab = bool
    },
    [TABLE_SIZE]: (state, tableSize) => {
      state.tableSize = tableSize
      storage.set(TABLE_SIZE, tableSize)
    },
    [TABLE_BORDERED]: (state, tableBordered) => {
      state.tableBordered = tableBordered
      storage.set(TABLE_BORDERED, tableBordered)
    },
    [HIDE_FOOTER]: (state, hideFooter) => {
      state.hideFooter = hideFooter
      storage.set(HIDE_FOOTER, hideFooter)
    }
  },
  actions: {
    setLang ({ commit }, lang) {
      return new Promise((resolve, reject) => {
        commit(APP_LANGUAGE, lang)
        loadLanguageAsync(lang).then(() => {
          resolve()
        }).catch((e) => {
          reject(e)
        })
      })
    }
  }
}

export default app

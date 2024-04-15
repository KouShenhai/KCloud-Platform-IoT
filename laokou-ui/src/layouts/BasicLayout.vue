<template>
  <pro-layout
    :menus="menus"
    :collapsed="collapsed"
    :mediaQuery="query"
    :isMobile="isMobile"
    :handleMediaQuery="handleMediaQuery"
    :handleCollapse="handleCollapse"
    :i18nRender="i18nRender"
    v-bind="settings"
  >
    <!-- Ads begin
      广告代码 真实项目中请移除
      production remove this Ads
    -->
    <!-- <ads v-if="isProPreviewSite && !collapsed"/> -->
    <!-- Ads end -->

    <!-- layout content -->
    <!-- 2021.01.15 默认固定页头，去掉样式paddingTop: fixedHeader ? '64' : '0'  -->
    <a-layout-content :style="{ height: '100%', margin: '0 0 0px 0'}">
      <multi-tab v-if="settings.multiTab"></multi-tab>
      <transition name="page-transition">
      </transition>
    </a-layout-content>
    <!-- 1.0.0+ 版本 pro-layout 提供 API，
          我们推荐使用这种方式进行 LOGO 和 title 自定义
    -->
    <template v-slot:menuHeaderRender>
      <div>
        <img src="~@/assets/logo.png" class="logo" alt="logo">
        <h1>{{ title }}</h1>
      </div>
    </template>

    <setting-drawer v-if="isProPreviewSite" :settings="settings" @change="handleSettingChange" />
    <template v-slot:rightContentRender>
      <right-content :top-menu="settings.layout === 'topmenu'" :is-mobile="isMobile" :theme="settings.theme" />
    </template>
    <template v-slot:footerRender v-if="!hideFooter">
      <global-footer />
    </template>
    <keep-alive :include="this.cachedViews">
      <router-view :key="key" />
    </keep-alive>
  </pro-layout>
</template>

<script>
import MultiTab from '@/components/MultiTab'
import { updateTheme } from '@/components/ProLayout'
import SettingDrawer from '@/components/SettingDrawer'
import { i18nRender } from '@/locales'
import { mapState } from 'vuex'
import {
  CONTENT_WIDTH_TYPE,
  SIDE_COLLAPSED,
  TOGGLE_MOBILE_TYPE,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_FIXED_HEADER,
  TOGGLE_FIXED_SIDEBAR,
  TOGGLE_LAYOUT,
  TOGGLE_NAV_THEME,
  TOGGLE_WEAK,
  TOGGLE_COLOR,
  TOGGLE_MULTI_TAB,
  TABLE_SIZE,
  TABLE_BORDERED,
  HIDE_FOOTER
} from '@/store/mutation-types'

import defaultSettings from '@/config/defaultSettings'
import RightContent from '@/components/GlobalHeader/RightContent'
import GlobalFooter from '@/components/GlobalFooter'
import Ads from '@/components/Other/CarbonAds'
import { baseMixin } from '@/store/app-mixin'

export default {
  name: 'BasicLayout',
  components: {
    SettingDrawer,
    RightContent,
    GlobalFooter,
    Ads,
    MultiTab
  },
  mixins: [baseMixin],
  data () {
    return {
      // preview.pro.antdv.com only use.
      isProPreviewSite: process.env.VUE_APP_PREVIEW === 'true',
      // end
      // base
      menus: [],
      // 侧栏展开状态
      collapsed: false,
      title: defaultSettings.title,
      settings: {
        // 布局类型
        layout: defaultSettings.layout, // 'sidemenu', 'topmenu'
        // CONTENT_WIDTH_TYPE
        contentWidth: defaultSettings.layout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : defaultSettings.contentWidth,
        // 主题 'dark' | 'light'
        theme: defaultSettings.navTheme,
        // 主色调
        primaryColor: defaultSettings.primaryColor,
        fixedHeader: defaultSettings.fixedHeader,
        fixSiderbar: defaultSettings.fixSiderbar,
        multiTab: defaultSettings.multiTab,
        colorWeak: defaultSettings.colorWeak,

        hideHintAlert: true,
        hideCopyButton: false,
        tableSize: defaultSettings.tableSize,
        tableBordered: defaultSettings.tableBordered,
        hideFooter: defaultSettings.hideFooter
      },
      // 媒体查询
      query: {}
    }
  },
  computed: {
    ...mapState({
      // 动态主路由
      mainMenu: state => state.permission.menus
    }),
    cachedViews () {
      return this.$store.state.tagsView.cachedViews
    },
    key () {
      return this.$route.path
    }
  },
  created () {
    const routes = this.mainMenu.find(item => item.path === '/')

    this.menus = (routes && routes.children) || []
    // 处理侧栏展开状态
    this.$watch('collapsed', () => {
      this.$store.commit(SIDE_COLLAPSED, this.collapsed)
    })
  },
  mounted () {
    if (this.isProPreviewSite) {
      this.settings.layout = this.layout
      this.settings.contentWidth = this.contentWidth
      this.settings.theme = this.navTheme
      this.settings.primaryColor = this.primaryColor
      this.settings.fixedHeader = this.fixedHeader
      this.settings.fixSiderbar = this.fixedSidebar
      this.settings.multiTab = this.multiTab
      this.settings.colorWeak = this.colorWeak
      this.settings.tableSize = this.tableSize
      this.settings.tableBordered = this.tableBordered
      this.settings.hideFooter = this.hideFooter
    }
    this.collapsed = this.sideCollapsed
    const userAgent = navigator.userAgent
    if (userAgent.indexOf('Edge') > -1) {
      this.$nextTick(() => {
        this.collapsed = !this.collapsed
        setTimeout(() => {
          this.collapsed = !this.collapsed
        }, 16)
      })
    }

    // first update color
    // TIPS: THEME COLOR HANDLER!! PLEASE CHECK THAT!!
    if (process.env.NODE_ENV !== 'production' || process.env.VUE_APP_PREVIEW === 'true') {
      updateTheme(this.settings.primaryColor)
    }
  },
  methods: {
    i18nRender,
    handleMediaQuery (val) {
      this.query = val
      if (this.isMobile && !val['screen-xs']) {
        this.$store.commit(TOGGLE_MOBILE_TYPE, false)
        return
      }
      if (!this.isMobile && val['screen-xs']) {
        this.$store.commit(TOGGLE_MOBILE_TYPE, true)
        this.collapsed = true
        this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fluid
        // this.settings.fixSiderbar = false
      }
    },
    handleCollapse (val) {
      this.collapsed = val
    },
    handleSettingChange ({ type, value }) {
      type && (this.settings[type] = value)
      switch (type) {
        case 'theme':
          this.$store.commit(TOGGLE_NAV_THEME, value)
          break
        case 'primaryColor':
          this.$store.commit(TOGGLE_COLOR, value)
          break
        case 'contentWidth':
          this.settings[type] = value
          this.$store.commit(TOGGLE_CONTENT_WIDTH, value)
          break
        case 'layout':
          this.$store.commit(TOGGLE_LAYOUT, value)
          if (value === 'sidemenu') {
            this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fluid
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fluid)
          } else {
            this.settings.fixSiderbar = false
            this.$store.commit(TOGGLE_FIXED_SIDEBAR, false)
            this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fixed
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fixed)
          }
          break
        case 'fixSiderbar':
          this.$store.commit(TOGGLE_FIXED_SIDEBAR, value)
          break
        case 'fixedHeader':
          this.$store.commit(TOGGLE_FIXED_HEADER, value)
          break
        case 'multiTab':
          this.$store.commit(TOGGLE_MULTI_TAB, value)
          break
        case 'colorWeak':
          this.$store.commit(TOGGLE_WEAK, value)
          break
        case 'tableSize':
          this.$store.commit(TABLE_SIZE, value)
          break
        case 'tableBordered':
          this.$store.commit(TABLE_BORDERED, value)
          break
        case 'hideFooter':
          this.$store.commit(HIDE_FOOTER, value)
          break
      }
    }
  }
}
</script>

<style lang="less">
@import "./BasicLayout.less";
</style>

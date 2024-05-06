<template>
  <div :class="wrpCls">
    <a-space size="middle">
      <ScreenFull />
      <notice-icon />
      <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
      <div class="user-layout-lang">
        <select-lang class="select-lang-trigger" />
      </div>
    </a-space>
  </div>
</template>

<script>
import AvatarDropdown from './AvatarDropdown'
import NoticeIcon from '@/components/NoticeIcon'
import ScreenFull from '@/components/Screenfull'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'RightContent',
  components: {
    AvatarDropdown,
    NoticeIcon,
    ScreenFull,
    SelectLang
  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action'
    },
    isMobile: {
      type: Boolean,
      default: () => false
    },
    topMenu: {
      type: Boolean,
      required: true
    },
    theme: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      showMenu: true,
      currentUser: {},
      docUrl: 'https://docs.geekera.cn/RuoYi-Antdv/',
      githubUrl: 'https://github.com/fuzui/RuoYi-Antdv'
    }
  },
  computed: {
    wrpCls () {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${(this.isMobile || !this.topMenu) ? 'light' : this.theme}`]: true
      }
    }
  },
  mounted () {
    setTimeout(() => {
      this.currentUser = {
        name: 'RuoYi'
      }
    }, 1500)
  },
  methods: {
  }
}
</script>

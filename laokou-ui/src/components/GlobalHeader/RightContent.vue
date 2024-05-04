<template>
  <div :class="wrpCls">
    <a-space size="middle">
      <a-tooltip placement="bottom">
        <template slot="title">
          源码地址
        </template>
        <a-icon type="github" @click="toGithub" :style="{ fontSize: '20px' }"/>
      </a-tooltip>
      <a-tooltip placement="bottom">
        <template slot="title">
          文档地址
        </template>
        <a-icon type="question-circle-o" @click="toDoc" :style="{ fontSize: '20px' }"/>
      </a-tooltip>
      <screenfull />
      <notice-icon v-hasPermi="['sys:notice:list']" />
      <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
      <!-- 暂只支持中文，国际化可自行扩展 -->
      <select-lang :class="prefixCls" />
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
    toDoc () {
      window.open(this.docUrl)
    },
    toGithub () {
      window.open(this.githubUrl)
    }
  }
}
</script>

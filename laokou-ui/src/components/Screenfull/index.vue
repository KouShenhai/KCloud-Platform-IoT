<template>
  <div>
    <a-tooltip placement="bottom">
      <template slot="title">
        {{ isFullscreen ? '退出全屏' : '切为全屏' }}
      </template>
      <a-icon :type="isFullscreen ? 'fullscreen-exit' : 'fullscreen'" @click="click" :style="{ fontSize: '20px' }"/>
    </a-tooltip>
  </div>
</template>

<script>
import ScreenFull from 'screenfull'

export default {
  name: 'ScreenFull',
  data () {
    return {
      isFullscreen: false
    }
  },
  mounted () {
    this.init()
  },
  beforeDestroy () {
    this.destroy()
  },
  methods: {
    click () {
      if (!ScreenFull.isEnabled) {
        this.$message.warning('你的浏览器不支持全屏')
        return false
      }
      ScreenFull.toggle()
    },
    change () {
      this.isFullscreen = ScreenFull.isFullscreen
    },
    init () {
      if (ScreenFull.isEnabled) {
        ScreenFull.on('change', this.change)
      }
    },
    destroy () {
      if (ScreenFull.isEnabled) {
        ScreenFull.off('change', this.change)
      }
    }
  }
}
</script>

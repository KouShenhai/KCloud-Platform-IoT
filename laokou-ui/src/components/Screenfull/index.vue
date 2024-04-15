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
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
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
      if (!screenfull.isEnabled) {
        this.$message.warning('你的浏览器不支持全屏')
        return false
      }
      screenfull.toggle()
    },
    change () {
      this.isFullscreen = screenfull.isFullscreen
    },
    init () {
      if (screenfull.isEnabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy () {
      if (screenfull.isEnabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

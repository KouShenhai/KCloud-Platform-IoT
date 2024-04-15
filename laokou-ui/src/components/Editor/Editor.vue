<template>
  <div>
    <div id="editor" class="editor" ref="editor"></div>
  </div>
</template>
<script>
import storage from 'store'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import E from 'wangeditor'
import hljs from 'highlight.js'

export default {
  name: 'Editor',
  components: {
  },
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default: ''
    },
    /* 高度 */
    height: {
      type: Number,
      default: null
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false
    },
    // 上传文件大小限制(MB)
    fileSize: {
      type: Number,
      default: 1024
    },
    imageSize: {
      type: Number,
      default: 5
    },
    videoSize: {
      type: Number,
      default: 1024
    }
  },
  data () {
    return {
      uploadImgUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      headers: {
        Authorization: 'Bearer ' + storage.get(ACCESS_TOKEN),
        Accept: 'application/json, text/plain, */*'
      },
      editor: null,
      currentValue: ''
    }
  },
  computed: {
  },
  watch: {
    value: {
      handler (val) {
        if (val !== this.currentValue) {
          this.currentValue = val === null ? '' : val
          if (this.editor) {
            this.editor.txt.html(this.currentValue)
          }
        }
      },
      immediate: true
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      var that = this
      this.editor = new E('#editor')
      // 代码高亮
      this.editor.highlight = hljs
      // 高度设置
      if (this.height) {
        this.editor.config.height = this.height
      }
      // z-index
      this.editor.config.zIndex = 0
      // 自定义提示信息
      this.editor.config.customAlert = function (s, t) {
        switch (t) {
          case 'success':
            that.$message.success(s)
            break
          case 'info':
            that.$message.info(s)
            break
          case 'warning':
            that.$message.warning(s)
            break
          case 'error':
            that.$message.error(s)
            break
          default:
            that.$message.info(s)
            break
        }
      }
      // 图片
      this.editor.config.uploadImgMaxSize = 1024 * 1024 * this.imageSize
      this.editor.config.uploadImgServer = this.uploadImgUrl
      this.editor.config.uploadImgHeaders = this.headers
      this.editor.config.uploadFileName = 'file'
      this.editor.config.uploadImgHooks = {
        customInsert: function (insertImgFn, result) {
            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertImgFn(result.url)
        }
      }
      // 视频
      this.editor.config.uploadVideoMaxSize = 1024 * 1024 * this.videoSize
      this.editor.config.uploadVideoServer = this.uploadImgUrl
      this.editor.config.uploadVideoHeaders = this.headers
      this.editor.config.uploadVideoName = 'file'
      this.editor.config.uploadVideoHooks = {
        customInsert: function (insertVideoFn, result) {
            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertVideoFn(result.url)
        }
      }
      // 数据双向绑定
      this.editor.config.onchange = function (newHtml) {
        that.currentValue = newHtml
        that.$emit('input', newHtml)
      }
      this.editor.create()
      this.editor.txt.html(this.currentValue)
      if (this.readOnly) {
        this.editor.disable()
      }
    },
    handleUploadError () {
      this.$message.error('图片插入失败')
    }
  }
}
</script>

<style>

</style>

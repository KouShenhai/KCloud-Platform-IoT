<template>
  <a-modal
    :title="title"
    :visible="open"
    :confirm-loading="uploading"
    @cancel="importExcelHandleCancel"
    @ok="importExcelHandleChange"
  >
    <a-spin tip="上传中..." :spinning="uploading">
      <a-upload-dragger
        name="file"
        accept=".xlsx, .xls"
        :file-list="fileList"
        :multiple="false"
        :remove="handleRemove"
        :before-upload="beforeUpload"
      >
        <p class="ant-upload-drag-icon">
          <a-icon type="inbox" />
        </p>
        <p class="ant-upload-text">
          请将文件拖拽到此处上传❥(^_-)
        </p>
        <p class="ant-upload-hint">
          支持单个上传，也可以点击后选择文件上传
        </p>
      </a-upload-dragger>
      <a-checkbox @change="handleCheckedUpdateSupport" :checked="updateSupport != 0">
        是否更新已经存在的用户数据
      </a-checkbox>
      <a @click="importTemplate">下载模板</a>
    </a-spin>
  </a-modal>
</template>

<script>

import { importData } from '@/api/system/user'

export default {
  name: 'ImportExcel',
  props: {
  },
  components: {
  },
  data () {
    return {
      title: '用户导入',
      open: false,
      uploadStatus: '',
      fileList: [],
      // 是否禁用上传
      uploading: false,
      updateSupport: 0
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 导入excel窗体关闭 */
    importExcelHandleCancel (e) {
      this.open = false
      this.fileList = []
      // 关闭后刷新列表
      this.$emit('ok')
    },
    /** 下载模板操作 */
    importTemplate () {
      this.download('system/user/importTemplate', {
        ...this.queryParams
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    /** 导入excel窗体开启 */
    importExcelHandleOpen (e) {
      this.open = true
    },
    beforeUpload (file) {
      this.fileList = [file]
      return false
    },
    /** 导入excel */
    importExcelHandleChange () {
      this.uploading = true
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      formData.append('updateSupport', this.updateSupport)
      importData(formData).then(response => {
        this.fileList = []
        this.$message.success(response.msg)
        this.open = false
        this.$emit('ok')
      }).finally(() => {
        this.uploading = false
      })
    },
    handleCheckedUpdateSupport () {
      this.updateSupport = this.updateSupport === 0 ? 1 : 0
    },
    handleRemove () {
      this.fileList = []
      this.uploading = false
    }
  }
}
</script>

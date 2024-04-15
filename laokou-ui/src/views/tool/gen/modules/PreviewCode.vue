<template>
  <div>
    <a-drawer
      title="代码预览"
      :width="800"
      :visible="visible"
      @close="close"
    >
      <a-tabs v-if="isShowCodeTabs">
        <a-tab-pane
          v-for="(value, key) in previewData"
          :tab="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :key="key"
        >
          <div id="codeView" v-highlight>
            <pre class="highlight-wrap"><code v-text="value"></code></pre>
          </div>
        </a-tab-pane>
      </a-tabs>
      <div class="bottom-control">
        <a-space>
          <a-button type="dashed" @click="close">
            关闭
          </a-button>
        </a-space>
      </div>
    </a-drawer>
  </div>
</template>
<script>
import { previewTable } from '@/api/tool/gen'
export default {
  data () {
    return {
      isShowCodeTabs: false,
      previewData: {},
      visible: false,
      // 模态框数据
      data: {},
      labelCol: {
        xs: { span: 12, push: 1 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24, push: 1 },
        sm: { span: 18 }
      }
    }
  },
  created () {},
  methods: {
    // 关闭模态框
    close () {
      this.visible = false
    },
    // 打开抽屉(由外面的组件调用)
    handlePreview (data) {
      if (data) {
        const tableId = data.tableId
        previewTable(tableId).then(response => {
            this.previewData = response.data
            this.isShowCodeTabs = true
        })
      }
      this.visible = true
    }
  }
}
</script>
<style lang="less" scoped>
  .highlight-wrap {
    position: relative;
    background: #21252b;
    border-radius: 5px;
    font: 15px/22px "Microsoft YaHei",Arial,Sans-Serif;
    line-height: 1.6;
    margin-bottom: 1.6em;
    max-width: 100%;
    text-shadow: none;
    color: #000;
    padding-top: 30px;
    box-shadow: 0 10px 30px 0 rgb(0 0 0 / 40%);
  }
  .highlight-wrap:before {
    content: " ";
    position: absolute;
    border-radius: 50%;
    background: #fc625d;
    width: 12px;
    height: 12px;
    left: 12px;
    margin-top: -18px;
    box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
    z-index: 2;
  }
</style>

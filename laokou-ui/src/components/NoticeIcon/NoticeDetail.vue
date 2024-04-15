<template>
  <a-modal
    ref="noticeDetail"
    :width="900"
    :visible="visible"
    @cancel="close"
    :footer="null"
  >
    <template slot="title" >
      <center><a-tag color="red"><dict-tag :options="typeOptions" :value="form.noticeType" /></a-tag>{{ form.noticeTitle }}</center>
    </template>
    <div class="notice-detail" v-html="form.noticeContent" v-highlight>
    </div>
  </a-modal>
</template>

<script>
import { getNotice } from '@/api/system/notice'

export default {
  name: 'NoticeDetail',
  components: {
  },
  props: {
    typeOptions: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      loading: false,
      loadingMore: false,
      showLoadingMore: true,
      visible: false,
      form: {
      }
    }
  },
  methods: {
    /** 修改按钮操作 */
    getNotice (id) {
      const noticeId = id
      getNotice(noticeId).then(response => {
        this.form = response.data
      })
      this.visible = true
    },
    // 关闭模态框
    close () {
      this.visible = false
      this.form = {}
    }
  }
}
</script>
<style lang="less" scoped>
  .notice-detail {
    /* table 样式 */
    /deep/ table {
      border-top: 1px solid #ccc;
      border-left: 1px solid #ccc;
    }
    /deep/ table td,
    table th {
      border-bottom: 1px solid #ccc;
      border-right: 1px solid #ccc;
      padding: 3px 5px;
    }
    /deep/ table th {
      border-bottom: 2px solid #ccc;
      text-align: center;
    }

    /* blockquote 样式 */
    /deep/ blockquote {
      display: block;
      border-left: 8px solid #d0e5f2;
      padding: 5px 10px;
      margin: 10px 0;
      line-height: 1.4;
      font-size: 100%;
      background-color: #f1f1f1;
    }

    /* code 样式 */
    /deep/ code {
      display: inline-block;
      *display: inline;
      *zoom: 1;
      background-color: #f1f1f1;
      border-radius: 3px;
      padding: 3px 5px;
      margin: 0 3px;
    }
    /deep/ pre code {
      display: block;
    }

    /* ul ol 样式 */
    /deep/ ul, ol {
      margin: 10px 0 10px 20px;
    }
  }
</style>

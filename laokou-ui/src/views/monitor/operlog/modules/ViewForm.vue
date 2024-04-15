<template>
  <a-drawer width="35%" placement="right" :closable="false" :visible="openView" @close="onCloseView">
    <a-descriptions title="操作信息" layout="vertical">
      <a-descriptions-item label="操作模块">
        {{ form.title }}
      </a-descriptions-item>
      <a-descriptions-item label="登录信息">
        {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}
      </a-descriptions-item>
      <a-descriptions-item label="请求地址">
        {{ form.operUrl }}
      </a-descriptions-item>
      <a-descriptions-item label="操作方法" span="2">
        <div style="word-break: break-all;">{{ form.method }}</div>
      </a-descriptions-item>
      <a-descriptions-item label="请求方式">
        {{ form.requestMethod }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="接口信息" layout="vertical">
      <a-descriptions-item label="请求参数" span="3">
        {{ form.operParam }}
      </a-descriptions-item>
      <a-descriptions-item label="返回参数" span="3">
        {{ form.jsonResult }}
      </a-descriptions-item>
      <a-descriptions-item label="操作状态" span="1">
        <a-badge v-if="form.status === 0" status="processing" text="正常" />
        <a-badge v-if="form.status === 1" status="error" text="失败" />
      </a-descriptions-item>
      <a-descriptions-item label="消耗时间" span="1">
        {{ form.costTime }} 毫秒
      </a-descriptions-item>
      <a-descriptions-item label="操作日期" span="1">
        {{ parseTime(form.operTime) }}
      </a-descriptions-item>
      <a-descriptions-item label="异常信息" span="3" v-if="form.status === 1">
        {{ form.errorMsg }}
      </a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script>

export default {
  name: 'ViewForm',
  props: {
  },
  data () {
    return {
      // 表单参数
      form: {},
      openView: false
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
    handleView (row) {
      this.openView = true
      this.form = row
    },
    onCloseView () {
      this.openView = false
    }
  }
}
</script>

<template>
  <a-modal
    ref="genCrontab"
    :title="'Cron表达式生成器'"
    :width="900"
    :visible="visible"
    :confirm-loading="submitLoading"
    @cancel="close"
    @ok="confirm"
  >
    <crontab v-if="visible" ref="crontab" :expression="expression"> </crontab>
  </a-modal>
</template>
<script>
import Crontab from '@/components/Crontab'

export default {
  components: { Crontab },
  name: 'GenCrontab',
  props: {
  },
  data () {
    return {
      // 当前控件配置:
      submitLoading: false,
      visible: false,
      expression: ''
    }
  },
  created () {
  },
  methods: {
    // 关闭模态框
    close () {
      this.visible = false
    },
    // 确认导入
    confirm () {
      this.$emit('fill', this.$refs.crontab.crontabValueString)
      this.visible = false
    },
    // 打开抽屉(由外面的组件调用)
    show (expression) {
      this.expression = expression
      this.visible = true
    }
  }
}
</script>

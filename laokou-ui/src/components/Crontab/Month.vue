<template>
  <a-form-model>
    <a-radio-group v-model="radioValue" @change="radioChange">
      <a-radio :style="radioStyle" :value="1">
        月，允许的通配符[, - * /]
      </a-radio>
      <a-radio :style="radioStyle" :value="2">
        周期从
        <a-input-number v-model="cycle01" :min="1" :max="11" />
        -
        <a-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : 2" :max="12" />
        月
      </a-radio>
      <a-radio :style="radioStyle" :value="3">
        从
        <a-input-number v-model="average01" :min="1" :max="11" />
        月开始，每
        <a-input-number v-model="average02" :min="1" :max="12 - average01 || 0" />
        月执行一次
      </a-radio>
      <a-radio :style="radioStyle" :value="4">
        指定
        <a-select
          mode="multiple"
          v-model="checkboxList"
          style="width: 100%"
          placeholder="可多选"
        >
          <a-select-option v-for="i in 60" :key="i - 1">
            {{ i - 1 }}
          </a-select-option>
        </a-select>
      </a-radio>
    </a-radio-group>
  </a-form-model>
</template>

<script>

export default {
  name: 'Min',
  props: {
    check: {
      type: Function,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      radioValue: 1,
      radioStyle: {
        display: 'block',
        height: '50px',
        lineHeight: '50px'
      },
      cycle01: 1,
      cycle02: 2,
      average01: 1,
      average02: 1,
      checkboxList: [],
      checkNum: this.$options.propsData.check
    }
  },
  watch: {
    radioValue: 'radioChange',
    cycleTotal: 'cycleChange',
    averageTotal: 'averageChange',
    checkboxString: 'checkboxChange'
  },
  computed: {
    // 计算两个周期值
		cycleTotal: function () {
      const cycle01 = this.checkNum(this.cycle01, 1, 11)
			const cycle02 = this.checkNum(this.cycle02, cycle01 ? cycle01 + 1 : 2, 12)
			return cycle01 + '-' + cycle02
		},
		// 计算平均用到的值
		averageTotal: function () {
      const average01 = this.checkNum(this.average01, 1, 11)
			const average02 = this.checkNum(this.average02, 1, 12 - average01 || 0)
			return average01 + '/' + average02
		},
    // 计算勾选的checkbox值合集
    checkboxString: function () {
      const str = this.checkboxList.join()
      return str === '' ? '*' : str
    }
  },
  methods: {
    // 单选按钮值变化时
    radioChange (e) {
      switch (this.radioValue) {
      case 1:
        this.$emit('update', 'month', '*')
        break
      case 2:
        this.$emit('update', 'month', this.cycleTotal)
        break
      case 3:
        this.$emit('update', 'month', this.averageTotal)
        break
      case 4:
        this.$emit('update', 'month', this.checkboxString)
        break
      }
    },
    // 周期两个值变化时
    cycleChange () {
      if (this.radioValue === 2) {
        this.$emit('update', 'month', this.cycleTotal)
      }
    },
    // 平均两个值变化时
    averageChange () {
      if (this.radioValue === 3) {
        this.$emit('update', 'month', this.averageTotal)
      }
    },
    // checkbox值变化时
    checkboxChange () {
      if (this.radioValue === 4) {
        this.$emit('update', 'month', this.checkboxString)
      }
    }
  }
}
</script>

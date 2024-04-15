<template>
  <a-form-model>
    <a-radio-group v-model="radioValue" @change="radioChange">
      <a-radio :style="radioStyle" :value="1">
        分钟，允许的通配符[, - * /]
      </a-radio>
      <a-radio :style="radioStyle" :value="2">
        周期从
        <a-input-number v-model="cycle01" :min="0" :max="58" />
        -
        <a-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : 1" :max="59" />
        分钟
      </a-radio>
      <a-radio :style="radioStyle" :value="3">
        从
        <a-input-number v-model="average01" :min="0" :max="58" />
        分钟开始，每
        <a-input-number v-model="average02" :min="1" :max="59 - average01 || 0" />
        分钟执行一次
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
      cycle01: 0,
      cycle02: 1,
      average01: 0,
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
      const cycle01 = this.checkNum(this.cycle01, 0, 58)
			const cycle02 = this.checkNum(this.cycle02, cycle01 ? cycle01 + 1 : 1, 59)
			return cycle01 + '-' + cycle02
		},
		// 计算平均用到的值
		averageTotal: function () {
      const average01 = this.checkNum(this.average01, 0, 58)
			const average02 = this.checkNum(this.average02, 1, 59 - average01 || 0)
			return average01 + '/' + average02
		},
    // 计算勾选的checkbox值合集
    checkboxString: function () {
      const str = this.checkboxList.join()
      return str === '' ? '*' : str
    }
  },
  methods: {
    radioChange () {
      switch (this.radioValue) {
      case 1:
        this.$emit('update', 'min', '*', 'min')
        break
      case 2:
        this.$emit('update', 'min', this.cycleTotal, 'min')
        break
      case 3:
        this.$emit('update', 'min', this.averageTotal, 'min')
        break
      case 4:
        this.$emit('update', 'min', this.checkboxString, 'min')
        break
      }
    },
    // 周期两个值变化时
    cycleChange () {
      if (this.radioValue === 2) {
        this.$emit('update', 'min', this.cycleTotal, 'min')
      }
    },
    // 平均两个值变化时
    averageChange () {
      if (this.radioValue === 3) {
        this.$emit('update', 'min', this.averageTotal, 'min')
      }
    },
    // checkbox值变化时
    checkboxChange () {
      if (this.radioValue === 4) {
        this.$emit('update', 'min', this.checkboxString, 'min')
      }
    }
  }
}
</script>

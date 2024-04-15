<template>
  <a-form-model>
    <a-radio-group v-model="radioValue" @change="radioChange">
      <a-radio :style="radioStyle" :value="1">
        不填，允许的通配符[, - * /]
      </a-radio>
      <a-radio :style="radioStyle" :value="2">
        每年
      </a-radio>
      <a-radio :style="radioStyle" :value="3">
        周期从
        <a-input-number v-model="cycle01" :min="fullYear" :max="2098" />
        -
        <a-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : fullYear + 1" :max="2099"/>
        年
      </a-radio>
      <a-radio :style="radioStyle" :value="4">
        从
        <a-input-number v-model="average01" :min="fullYear" :max="2098"/>
        年开始，每
        <a-input-number v-model="average02" :min="1" :max="2099 - average01 || fullYear"/>
        年执行一次
      </a-radio>
      <a-radio :style="radioStyle" :value="5">
        指定
        <a-select
          mode="multiple"
          v-model="checkboxList"
          style="width: 100%"
          placeholder="可多选"
        >
          <a-select-option v-for="i in 10" :key="i - 1 + fullYear">
            {{ i - 1 + fullYear }}
          </a-select-option>
        </a-select>
      </a-radio>
    </a-radio-group>
  </a-form-model>
</template>

<script>

export default {
  name: 'Year',
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
      fullYear: 0,
      checkNum: this.$options.propsData.check
    }
  },
  watch: {
    radioValue: 'radioChange',
    cycleTotal: 'cycleChange',
    averageTotal: 'averageChange',
    checkboxString: 'checkboxChange'
  },
  mounted () {
    // 仅获取当前年份
    this.fullYear = Number(new Date().getFullYear())
  },
  computed: {
    // 计算两个周期值
		cycleTotal: function () {
      const cycle01 = this.checkNum(this.cycle01, this.fullYear, 2098)
			const cycle02 = this.checkNum(this.cycle02, cycle01 ? cycle01 + 1 : this.fullYear + 1, 2099)
			return cycle01 + '-' + cycle02
		},
		// 计算平均用到的值
		averageTotal: function () {
      const average01 = this.checkNum(this.average01, this.fullYear, 2098)
			const average02 = this.checkNum(this.average02, 1, 2099 - average01 || this.fullYear)
			return average01 + '/' + average02
		},
    // 计算勾选的checkbox值合集
    checkboxString: function () {
      const str = this.checkboxList.join()
      return str
    }
  },
  methods: {
    radioChange () {
      switch (this.radioValue) {
      case 1:
        this.$emit('update', 'year', '')
        break
      case 2:
        this.$emit('update', 'year', '*')
        break
      case 3:
        this.$emit('update', 'year', this.cycleTotal)
        break
      case 4:
        this.$emit('update', 'year', this.averageTotal)
        break
      case 5:
        this.$emit('update', 'year', this.checkboxString)
        break
      }
    },
    // 周期两个值变化时
    cycleChange () {
      if (this.radioValue === 3) {
        this.$emit('update', 'year', this.cycleTotal)
      }
    },
    // 平均两个值变化时
    averageChange () {
      if (this.radioValue === 4) {
        this.$emit('update', 'year', this.averageTotal)
      }
    },
    // checkbox值变化时
    checkboxChange () {
      if (this.radioValue === 5) {
        this.$emit('update', 'year', this.checkboxString)
      }
    }
  }
}
</script>

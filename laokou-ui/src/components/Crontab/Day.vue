<template>
  <a-form-model>
    <a-radio-group v-model="radioValue" @change="radioChange">
      <a-radio :style="radioStyle" :value="1">
        日，允许的通配符[, - * ? / L W]
      </a-radio>
      <a-radio :style="radioStyle" :value="2">
        不指定
      </a-radio>
      <a-radio :style="radioStyle" :value="3">
        周期从
        <a-input-number v-model="cycle01" :min="1" :max="30" />
        -
        <a-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : 2" :max="31" />
        日
      </a-radio>
      <a-radio :style="radioStyle" :value="4">
        从
        <a-input-number v-model="average01" :min="1" :max="30" />
        号开始，每
        <a-input-number v-model="average02" :min="1" :max="31 - average01 || 1" />
        日执行一次
      </a-radio>
      <a-radio :style="radioStyle" :value="5">
        每月
        <a-input-number v-model="workday" :min="0" :max="31" />
        号最近的那个工作日
      </a-radio>
      <a-radio :style="radioStyle" :value="6">
        本月最后一天
      </a-radio>
      <a-radio :style="radioStyle" :value="7">
        指定
        <a-select
          mode="multiple"
          v-model="checkboxList"
          style="width: 100%"
          placeholder="可多选"
        >
          <a-select-option v-for="i in 31" :key="i">
            {{ i }}
          </a-select-option>
        </a-select>
      </a-radio>
    </a-radio-group>
  </a-form-model>
</template>

<script>

export default {
  name: 'Day',
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
      workday: 1,
      checkboxList: [],
      checkNum: this.$options.propsData.check
    }
  },
  watch: {
		radioValue: 'radioChange',
		cycleTotal: 'cycleChange',
		averageTotal: 'averageChange',
		workdayCheck: 'workdayChange',
		checkboxString: 'checkboxChange'
	},
	computed: {
		// 计算两个周期值
		cycleTotal: function () {
      const cycle01 = this.checkNum(this.cycle01, 1, 30)
			const cycle02 = this.checkNum(this.cycle02, cycle01 ? cycle01 + 1 : 2, 31)
			return cycle01 + '-' + cycle02
		},
		// 计算平均用到的值
		averageTotal: function () {
      const average01 = this.checkNum(this.average01, 1, 30)
			const average02 = this.checkNum(this.average02, 1, 31 - average01 || 0)
			return average01 + '/' + average02
		},
		// 计算工作日格式
		workdayCheck: function () {
      const workday = this.checkNum(this.workday, 1, 31)
			return workday
		},
		// 计算勾选的checkbox值合集
		checkboxString: function () {
			const str = this.checkboxList.join()
			return str === '' ? '*' : str
		}
	},
  methods: {
    radioChange () {
			if (this.radioValue !== 2) {
				this.$emit('update', 'week', '?', 'day')
			}
			switch (this.radioValue) {
				case 1:
					this.$emit('update', 'day', '*')
					break
				case 2:
					this.$emit('update', 'day', '?')
					break
				case 3:
					this.$emit('update', 'day', this.cycleTotal)
					break
				case 4:
					this.$emit('update', 'day', this.averageTotal)
					break
				case 5:
					this.$emit('update', 'day', this.workdayCheck + 'W')
					break
				case 6:
					this.$emit('update', 'day', 'L')
					break
				case 7:
					this.$emit('update', 'day', this.checkboxString)
					break
			}
		},
		// 周期两个值变化时
		cycleChange () {
			if (this.radioValue === 3) {
				this.$emit('update', 'day', this.cycleTotal)
			}
		},
		// 平均两个值变化时
		averageChange () {
			if (this.radioValue === 4) {
				this.$emit('update', 'day', this.averageTotal)
			}
		},
		// 最近工作日值变化时
		workdayChange () {
			if (this.radioValue === 5) {
				this.$emit('update', 'day', this.workdayCheck + 'W')
			}
		},
		// checkbox值变化时
		checkboxChange () {
			if (this.radioValue === 7) {
				this.$emit('update', 'day', this.checkboxString)
			}
		},
		// 父组件传递的week发生变化触发
		weekChange () {
			// 判断week值与day不能同时为“?”
			if (this.cron.week === '?' && this.radioValue === 2) {
				this.radioValue = 1
			} else if (this.cron.week !== '?' && this.radioValue !== 2) {
				this.radioValue = 2
			}
		}
  }
}
</script>

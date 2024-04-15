<template>
  <a-form-model>
    <a-radio-group v-model="radioValue" @change="radioChange">
      <a-radio :style="radioStyle" :value="1">
        周，允许的通配符[, - * ? / L #]
      </a-radio>
      <a-radio :style="radioStyle" :value="2">
        不指定
      </a-radio>
      <a-radio :style="radioStyle" :value="3">
        周期从
        <a-select
          v-model="cycle01"
          style="width: 100px"
          placeholder="可多选"
        >
          <a-select-option v-for="(w, index) in weekList" :key="index" :value="w.key" >
            {{ w.value }}
          </a-select-option>
        </a-select>
        -
        <a-select
          v-model="cycle02"
          style="width: 100px"
          placeholder="可多选"
        >
          <a-select-option v-for="(w, index) in weekList" :key="index" :value="w.key" >
            {{ w.value }}
          </a-select-option>
        </a-select>
      </a-radio>
      <a-radio :style="radioStyle" :value="4">
        第
        <a-input-number v-model="average01" :min="1" :max="4" />
        周的
        <a-select
          v-model="average02"
          style="width: 100px"
          placeholder="可多选"
        >
          <a-select-option v-for="(w, index) in weekList" :key="index" :value="w.key" >
            {{ w.value }}
          </a-select-option>
        </a-select>
      </a-radio>
      <a-radio :style="radioStyle" :value="5">
        指定月的最后一个
        <a-select
          v-model="weekday"
          style="width: 100px"
          placeholder="可多选"
        >
          <a-select-option v-for="(w, index) in weekList" :key="index" :value="w.key" >
            {{ w.value }}
          </a-select-option>
        </a-select>
      </a-radio>
      <a-radio :style="radioStyle" :value="6">
        指定
        <a-select
          mode="multiple"
          v-model="checkboxList"
          style="width: 100%"
          placeholder="可多选"
        >
          <a-select-option v-for="(w, index) in weekList" :key="index" :value="String(w.key)" >
            {{ w.value }}
          </a-select-option>
        </a-select>
      </a-radio>
    </a-radio-group>
  </a-form-model>
</template>

<script>

export default {
  name: 'Week',
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
      weekday: 2,
      cycle01: 2,
      cycle02: 3,
      average01: 1,
      average02: 2,
      checkboxList: [],
      weekList: [
        {
          key: 2,
          value: '星期一'
        },
        {
          key: 3,
          value: '星期二'
        },
        {
          key: 4,
          value: '星期三'
        },
        {
          key: 5,
          value: '星期四'
        },
        {
          key: 6,
          value: '星期五'
        },
        {
          key: 7,
          value: '星期六'
        },
        {
          key: 1,
          value: '星期日'
        }
      ],
      checkNum: this.$options.propsData.check
    }
  },
  watch: {
    radioValue: 'radioChange',
    cycleTotal: 'cycleChange',
    averageTotal: 'averageChange',
    weekdayCheck: 'weekdayChange',
    checkboxString: 'checkboxChange'
  },
  computed: {
    // 计算两个周期值
    cycleTotal: function () {
      return this.cycle01 + '-' + this.cycle02
    },
    // 计算平均用到的值
    averageTotal: function () {
      const average01 = this.checkNum(this.average01, 1, 4)
      return this.average02 + '#' + average01
    },
    // 最近的工作日（格式）
    weekdayCheck: function () {
      return this.weekday
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
      if (this.radioValue !== 2) {
        this.$emit('update', 'day', '?', 'week')
      }
      switch (this.radioValue) {
      case 1:
        this.$emit('update', 'week', '*')
        break
      case 2:
        this.$emit('update', 'week', '?')
        break
      case 3:
        this.$emit('update', 'week', this.cycleTotal)
        break
      case 4:
        this.$emit('update', 'week', this.averageTotal)
        break
      case 5:
        this.$emit('update', 'week', this.weekday + 'L')
        break
      case 6:
        this.$emit('update', 'week', this.checkboxString)
        break
      }
    },
    // 根据互斥事件，更改radio的值

    // 周期两个值变化时
    cycleChange () {
      if (this.radioValue === 3) {
        this.$emit('update', 'week', this.cycleTotal)
      }
    },
    // 平均两个值变化时
    averageChange () {
      if (this.radioValue === 4) {
        this.$emit('update', 'week', this.averageTotal)
      }
    },
    // 最近工作日值变化时
    weekdayChange () {
      if (this.radioValue === 5) {
        this.$emit('update', 'week', this.weekday + 'L')
      }
    },
    // checkbox值变化时
    checkboxChange () {
      if (this.radioValue === 6) {
        this.$emit('update', 'week', this.checkboxString)
      }
    }
  }
}
</script>

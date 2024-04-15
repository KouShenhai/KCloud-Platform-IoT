<template>
  <div>
    <a-card
      style="width:100%"
      :tab-list="types"
      :active-tab-key="currentType"
      @tabChange="key => onTabChange(key)"
    >
      <p v-show="currentType === 'second'">
        <crontab-second
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronsecond"
        />
      </p>
      <p v-show="currentType === 'min'">
        <crontab-min
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronmin"
        />
      </p>
      <p v-show="currentType === 'hour'">
        <crontab-hour
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronhour"
        />
      </p>
      <p v-show="currentType === 'day'">
        <crontab-day
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronday"
        />
      </p>
      <p v-show="currentType === 'month'">
        <crontab-month
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronmonth"
        />
      </p>
      <p v-show="currentType === 'week'">
        <crontab-week
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronweek"
        />
      </p>
      <p v-show="currentType === 'year'">
        <crontab-year
          @update="updateCrontabValue"
          :check="checkNumber"
          ref="cronyear"
        />
      </p>
    </a-card>
    <a-card
      style="width:100%"
    >
      <a-divider orientation="left">
        表达式字段
      </a-divider>
      <a-row :gutter="16">
        <a-col :md="3" :sm="8">
          <a-input addon-after="秒" v-model="crontabValueObj.second" disabled />
        </a-col>
        <a-col :md="4" :sm="8">
          <a-input addon-after="分钟" v-model="crontabValueObj.min" disabled />
        </a-col>
        <a-col :md="4" :sm="8">
          <a-input addon-after="小时" v-model="crontabValueObj.hour" disabled />
        </a-col>
        <a-col :md="3" :sm="6">
          <a-input addon-after="日" v-model="crontabValueObj.day" disabled />
        </a-col>
        <a-col :md="3" :sm="6">
          <a-input addon-after="月" v-model="crontabValueObj.month" disabled />
        </a-col>
        <a-col :md="3" :sm="6">
          <a-input addon-after="星期" v-model="crontabValueObj.week" disabled />
        </a-col>
        <a-col :md="4" :sm="6">
          <a-input addon-after="年" v-model="crontabValueObj.year" disabled />
        </a-col>
      </a-row>
      <a-divider orientation="left">
        Corn表达式
      </a-divider>
      <a-input v-model="crontabValueString" disabled>
        <a-tooltip slot="addonAfter" title="复制" @click="doCopy">
          <a-icon type="copy" />
        </a-tooltip>
      </a-input>
      <a-divider orientation="left">
        最近5次运行时间
      </a-divider>
      <crontab-result
        :ex="crontabValueString"
      />
    </a-card>
  </div>
</template>

<script>
import CrontabDay from './Day'
import CrontabHour from './Hour'
import CrontabMin from './Min'
import CrontabMonth from './Month'
import CrontabSecond from './Second'
import CrontabWeek from './Week'
import CrontabYear from './Year'
import CrontabResult from './Result'

export default {
  name: 'Crontab',
  props: {
    expression: {
      type: String,
      default: ''
    }
  },
  components: {
    CrontabDay,
    CrontabHour,
    CrontabMin,
    CrontabMonth,
    CrontabSecond,
    CrontabWeek,
    CrontabYear,
    CrontabResult
  },
  data () {
    return {
      currentType: 'second',
      types: [
        {
          key: 'second',
          tab: '秒'
        },
        {
          key: 'min',
          tab: '分钟'
        },
        {
          key: 'hour',
          tab: '小时'
        },
        {
          key: 'day',
          tab: '日'
        },
        {
          key: 'month',
          tab: '月'
        },
        {
          key: 'week',
          tab: '周'
        },
        {
          key: 'year',
          tab: '年'
        }
      ],
      crontabValueObj: {
        second: '*',
        min: '*',
        hour: '*',
        day: '*',
        month: '*',
        week: '?',
        year: ''
      },
      loading: false,
      open: false
    }
  },
  mounted () {
    this.resolveExp()
  },
  watch: {
    expression: 'resolveExp'
  },
  computed: {
    crontabValueString: function () {
      const obj = this.crontabValueObj
      const str =
        obj.second +
        ' ' +
        obj.min +
        ' ' +
        obj.hour +
        ' ' +
        obj.day +
        ' ' +
        obj.month +
        ' ' +
        obj.week +
        (obj.year === '' ? '' : ' ' + obj.year)
      return str
    }
  },
  methods: {
    resolveExp () {
      // 反解析 表达式
      if (this.expression) {
        const arr = this.expression.split(' ')
        if (arr.length >= 6) {
          // 6 位以上是合法表达式
          const obj = {
            second: arr[0],
            min: arr[1],
            hour: arr[2],
            day: arr[3],
            month: arr[4],
            week: arr[5],
            year: arr[6] ? arr[6] : ''
          }
          this.crontabValueObj = {
            ...obj
          }
          for (const i in obj) {
            if (obj[i]) this.changeRadio(i, obj[i])
          }
        }
      } else {
        // 没有传入的表达式 则还原
        this.clearCron()
      }
    },
    // 由子组件触发，更改表达式组成的字段值
    updateCrontabValue (name, value, from) {
      this.crontabValueObj[name] = value
      if (from && from !== name) {
        this.changeRadio(name, value)
      }
    },
    // 赋值到组件
    changeRadio (name, value) {
      const arr = ['second', 'min', 'hour', 'month']
      const refName = 'cron' + name
      let insValue
      if (!this.$refs[refName]) return

      if (arr.includes(name)) {
        if (value === '*') {
          insValue = 1
        } else if (value.indexOf('-') > -1) {
          const indexArr = value.split('-')
          isNaN(indexArr[0])
            ? (this.$refs[refName].cycle01 = 0)
            : (this.$refs[refName].cycle01 = Number(indexArr[0]))
          this.$refs[refName].cycle02 = Number(indexArr[1])
          insValue = 2
        } else if (value.indexOf('/') > -1) {
          const indexArr = value.split('/')
          isNaN(indexArr[0])
            ? (this.$refs[refName].average01 = 0)
            : (this.$refs[refName].average01 = Number(indexArr[0]))
          this.$refs[refName].average02 = Number(indexArr[1])
          insValue = 3
        } else {
          insValue = 4
          this.$refs[refName].checkboxList = value.split(',')
        }
      } else if (name === 'day') {
        if (value === '*') {
          insValue = 1
        } else if (value === '?') {
          insValue = 2
        } else if (value.indexOf('-') > -1) {
          const indexArr = value.split('-')
          isNaN(indexArr[0])
            ? (this.$refs[refName].cycle01 = 0)
            : (this.$refs[refName].cycle01 = Number(indexArr[0]))
          this.$refs[refName].cycle02 = Number(indexArr[1])
          insValue = 3
        } else if (value.indexOf('/') > -1) {
          const indexArr = value.split('/')
          isNaN(indexArr[0])
            ? (this.$refs[refName].average01 = 0)
            : (this.$refs[refName].average01 = Number(indexArr[0]))
          this.$refs[refName].average02 = Number(indexArr[1])
          insValue = 4
        } else if (value.indexOf('W') > -1) {
          const indexArr = value.split('W')
          isNaN(indexArr[0])
            ? (this.$refs[refName].workday = 0)
            : (this.$refs[refName].workday = Number(indexArr[0]))
          insValue = 5
        } else if (value === 'L') {
          insValue = 6
        } else {
          this.$refs[refName].checkboxList = value.split(',')
          insValue = 7
        }
      } else if (name === 'week') {
        if (value === '*') {
          insValue = 1
        } else if (value === '?') {
          insValue = 2
        } else if (value.indexOf('-') > -1) {
          const indexArr = value.split('-')
          isNaN(indexArr[0])
            ? (this.$refs[refName].cycle01 = 0)
            : (this.$refs[refName].cycle01 = Number(indexArr[0]))
          this.$refs[refName].cycle02 = Number(indexArr[1])
          insValue = 3
        } else if (value.indexOf('#') > -1) {
          const indexArr = value.split('#')
          isNaN(indexArr[0])
            ? (this.$refs[refName].average02 = 1)
            : (this.$refs[refName].average02 = Number(indexArr[0]))
          this.$refs[refName].average01 = Number(indexArr[1])
          insValue = 4
        } else if (value.indexOf('L') > -1) {
          const indexArr = value.split('L')
          isNaN(indexArr[0])
            ? (this.$refs[refName].weekday = 1)
            : (this.$refs[refName].weekday = Number(indexArr[0]))
          insValue = 5
        } else {
          this.$refs[refName].checkboxList = value.split(',')
          insValue = 6
        }
      } else if (name === 'year') {
        if (value === '') {
          insValue = 1
        } else if (value === '*') {
          insValue = 2
        } else if (value.indexOf('-') > -1) {
          insValue = 3
        } else if (value.indexOf('/') > -1) {
          insValue = 4
        } else {
          this.$refs[refName].checkboxList = value.split(',')
          insValue = 5
        }
      }
      this.$refs[refName].radioValue = insValue
    },
    clearCron () {
      // 还原选择项
      this.crontabValueObj = {
        second: '*',
        min: '*',
        hour: '*',
        day: '*',
        month: '*',
        week: '?',
        year: ''
      }
      for (const j in this.crontabValueObj) {
        this.changeRadio(j, this.crontabValueObj[j])
      }
    },
    // 表单选项的子组件校验数字格式（通过-props传递）
    checkNumber (value, minLimit, maxLimit) {
      // 检查必须为整数
      value = Math.floor(value)
      if (value < minLimit) {
        value = minLimit
      } else if (value > maxLimit) {
        value = maxLimit
      }
      return value
    },
    onTabChange (key) {
      this.currentType = key
    },
    doCopy () {
      this.$copyText(this.crontabValueString).then(message => {
        this.$message.success('复制完毕')
      }).catch(() => {
        this.$message.error('复制失败')
      })
    }
  }
}
</script>

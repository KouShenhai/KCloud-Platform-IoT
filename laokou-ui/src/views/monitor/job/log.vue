<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="任务名称">
                <a-input v-model="queryParam.jobName" placeholder="请输入任务名称" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="任务组名">
                <a-select placeholder="请选择任务组名" v-model="queryParam.jobGroup" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in dict.type['sys_job_group']" :key="index" :value="d.value">{{ d.label }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="执行状态">
                  <a-select placeholder="请选择执行状态" v-model="queryParam.status" style="width: 100%" allow-clear>
                    <a-select-option v-for="(d, index) in dict.type['sys_common_status']" :key="index" :value="d.value">{{ d.label }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="执行时间">
                  <a-range-picker style="width: 100%" v-model="dateRange" valueFormat="YYYY-MM-DD" format="YYYY-MM-DD" allow-clear/>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="!advanced && 8 || 24" :sm="24">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['monitor:job:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="danger" @click="handleClean" v-hasPermi="['monitor:job:remove']">
          <a-icon type="delete" />清空
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['monitor:job:export']">
          <a-icon type="download" />导出
        </a-button>
        <table-setting
          :style="{float: 'right'}"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList" />
      </div>
      <!-- 详细信息 -->
      <log-view-form ref="logViewForm" :jobGroupOptions="dict.type['sys_job_group']" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="jobLogId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="jobGroup" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_job_group']" :value="record.jobGroup" />
        </span>
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_common_status']" :value="record.status" />
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.logViewForm.handleView(record)" v-hasPermi="['monitor:job:query']">
            <a-icon type="eye" />详细
          </a>
        </span>
      </a-table>
      <!-- 分页 -->
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="total => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>
  </page-header-wrapper>
</template>

<script>

import { listJobLog, delJobLog, cleanJobLog } from '@/api/monitor/jobLog'
import LogViewForm from './modules/LogViewForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'JobLog',
  components: {
    LogViewForm
  },
  mixins: [tableMixin],
  dicts: ['sys_job_group', 'sys_common_status'],
  data () {
    return {
      list: [],
      total: 0,
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // 高级搜索 展开/关闭
      advanced: false,
      loading: false,
      // 选中数组
      ids: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 非多个禁用
      multiple: true,
      columns: [
        {
          title: '日志编号',
          dataIndex: 'jobLogId',
          align: 'center'
        },
        {
          title: '系统模块',
          dataIndex: 'jobName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作类型',
          dataIndex: 'jobGroup',
          scopedSlots: { customRender: 'jobGroup' },
          align: 'center'
        },
        {
          title: '调用目标字符串',
          dataIndex: 'invokeTarget',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '日志信息',
          dataIndex: 'jobMessage',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '执行状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '执行时间',
          dataIndex: 'createTime',
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {
  },
  created () {
    this.getList()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询登录日志列表 */
    getList () {
      this.loading = true
      listJobLog(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
          this.list = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize (current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.jobLogId)
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const jobLogIds = row.jobLogId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中日志编号为' + jobLogIds + '的数据',
        onOk () {
          return delJobLog(jobLogIds)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
          })
        },
        onCancel () {}
      })
    },
    /** 清空按钮操作 */
    handleClean () {
      var that = this
      this.$confirm({
        title: '是否确认清空?',
        content: '此操作将会清空所有调度日志数据项',
        onOk () {
          return cleanJobLog()
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '清空成功',
                3
              )
          })
        },
        onCancel () {}
      })
    },
    /** 导出按钮操作 */
    handleExport () {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk () {
          that.download('/monitor/jobLog/export', {
            ...that.queryParam
          }, `job_log_${new Date().getTime()}.xlsx`)
        },
        onCancel () {}
      })
    }
  }
}
</script>

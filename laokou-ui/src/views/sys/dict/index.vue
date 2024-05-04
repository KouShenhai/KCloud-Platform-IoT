<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="字典名称">
                <a-input v-model="queryParam.dictName" placeholder="请输入字典名称" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="字典类型">
                <a-input v-model="queryParam.dictType" placeholder="请输入字典类型" allow-clear/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="状态">
                  <a-select placeholder="字典状态" v-model="queryParam.status" style="width: 100%">
                    <a-select-option v-for="(d, index) in dict.type['sys_normal_disable']" :key="index" :value="d.value">{{ d.label }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="创建时间">
                  <a-range-picker style="width: 100%" v-model="dateRange" valueFormat="YYYY-MM-DD" format="YYYY-MM-DD" />
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['sys:dict:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button type="primary" :disabled="single" @click="$refs.createForm.handleUpdate(undefined, ids)" v-hasPermi="['sys:dict:edit']">
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['sys:dict:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['sys:dict:export']">
          <a-icon type="download" />导出
        </a-button>
        <a-button type="dashed" :loading="refreshing" @click="handleRefreshCache" v-hasPermi="['sys:dict:remove']">
          <a-icon type="redo" />刷新缓存
        </a-button>
        <table-setting
          :style="{float: 'right'}"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList" />
      </div>
      <!-- 增加修改 -->
      <create-form
        ref="createForm"
        :statusOptions="dict.type['sys_normal_disable']"
        @ok="getList"
      />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="dictId"
        :columns="columns"
        :expandedRowKeys="expandedKeys"
        @expand="onExpandCurrent"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered">
        <span
          slot="expandedRowRender"
          slot-scope="text"
        >
          <dict-data
            ref="dictData"
            :dictId="text.dictId"
            :dictType="text.dictType"
          />
        </span>
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.status" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['sys:dict:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)" v-hasPermi="['sys:dict:remove']">
            <a-icon type="delete" />删除
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

import { listType, delType, refreshCache } from '@/api/system/dict/type'
import CreateForm from './modules/CreateForm'
import DictData from './modules/DictData'
import CreateDataForm from './modules/CreateDataForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Dict',
  components: {
    CreateForm,
    DictData,
    CreateDataForm
  },
  mixins: [tableMixin],
  dicts: ['sys_normal_disable'],
  data () {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      refreshing: false,
      total: 0,
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      expandedKeys: [],
      columns: [
        {
          title: '字典编号',
          dataIndex: 'dictId',
          align: 'center'
        },
        {
          title: '字典名称',
          dataIndex: 'dictName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '字典类型',
          dataIndex: 'dictType',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '备注',
          dataIndex: 'remark',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          ellipsis: true,
          scopedSlots: { customRender: 'createTime' },
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '15%',
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
    /** 查询字典列表 */
    getList () {
      this.loading = true
      listType(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
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
        dictName: undefined,
        dictType: undefined,
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
      this.ids = this.selectedRows.map(item => item.dictId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const dictIds = row.dictId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中字典编号为' + dictIds + '的数据',
        onOk () {
          return delType(dictIds)
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
    /** 导出按钮操作 */
    handleExport () {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk () {
          that.download('system/dict/type/export', {
            ...that.queryParam
          }, `type_${new Date().getTime()}.xlsx`)
        },
        onCancel () {}
      })
    },
    /** 清理缓存按钮操作 */
    handleRefreshCache () {
      this.refreshing = true
      refreshCache().then(() => {
        this.$message.success('刷新成功')
        this.$store.dispatch('dict/cleanDict')
      }).finally(() => {
        this.refreshing = false
      })
    },
    onExpandCurrent (expandedKeys, row) {
      if (expandedKeys) {
        this.expandedKeys = [row.dictId]
      } else {
        this.expandedKeys = []
      }
    }
  }
}
</script>

<template>
  <div>
    <create-data-form
      ref="createDataForm"
      :statusOptions="dict.type['sys_normal_disable']"
      :dictType="dictType"
      @ok="getList"
    />
    <a-card :bordered="false" :style="{ background: '#f2f4f5'}">
      <div class="table-operations">
        <a-button type="primary" size="small" @click="$refs.createDataForm.handleAdd()" v-hasPermi="['sys:dict:add']" ghost>
          <a-icon type="plus" />新增
        </a-button>
        <a-button
          type="danger"
          :disabled="multiple"
          @click="handleDelete"
          size="small"
          v-hasPermi="['sys:dict:remove']"
          ghost>
          <a-icon type="delete" />删除
        </a-button>
        <table-setting
          :style="{float: 'right'}"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList" />
      </div>
      <a-table
        :loading="loading"
        rowKey="dictCode"
        :size="tableSize"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :scroll="{ y: 300 }"
        :pagination="false">
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.status" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createDataForm.handleUpdate(record)" v-hasPermi="['sys:dict:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)" v-hasPermi="['sys:dict:remove']">
            <a-icon type="delete" />删除
          </a>
        </span>
      </a-table>
    </a-card>
  </div>
</template>

<script>

import { listData, delData } from '@/api/system/dict/data'
import CreateDataForm from './CreateDataForm'

export default {
  name: 'DictData',
  props: {
    dictType: {
      type: String,
      required: true
    },
    dictId: {
      type: Number,
      required: true
    }
  },
  components: {
    CreateDataForm
  },
  dicts: ['sys_normal_disable'],
  data () {
    return {
      list: [],
      tableSize: 'small',
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
      total: 0,
      queryParam: {
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      columns: [
        {
          title: '字典编码',
          dataIndex: 'dictCode',
          align: 'center'
        },
        {
          title: '字典标签',
          dataIndex: 'dictLabel',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '字典键值',
          dataIndex: 'dictValue',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '字典排序',
          dataIndex: 'dictSort',
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
    this.queryParam.dictType = this.dictType
    this.getList()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    getList () {
      this.loading = true
      listData(this.queryParam).then(response => {
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
        dictName: undefined,
        dictType: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.dictCode)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const dictCodes = row.dictCode || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中字典编码为' + dictCodes + '的数据',
        onOk () {
          return delData(dictCodes)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
              that.$store.dispatch('dict/removeDict', row.dictType)
          })
        },
        onCancel () {}
      })
    }
  }
}
</script>

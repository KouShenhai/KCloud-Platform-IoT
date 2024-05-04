<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="公告标题">
                <a-input v-model="queryParam.noticeTitle" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="操作人员">
                <a-input v-model="queryParam.createBy" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="公告类型">
                  <a-select placeholder="请选择" v-model="queryParam.noticeType" style="width: 100%" allow-clear>
                    <a-select-option v-for="(d, index) in dict.type['sys_notice_type']" :key="index" :value="d.value">{{ d.label }}</a-select-option>
                  </a-select>
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
        <a-button type="primary" @click="handleAdd()" v-hasPermi="['sys:notice:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button type="primary" :disabled="single" @click="handleUpdate(undefined, ids)" v-hasPermi="['sys:notice:edit']">
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['sys:notice:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <table-setting
          :style="{float: 'right'}"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList" />
      </div>
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="noticeId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered">
        <span slot="noticeType" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_notice_type']" :value="record.noticeType" />
        </span>
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.status" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="handleUpdate(record, undefined)" v-hasPermi="['sys:notice:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-hasPermi="['sys:notice:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['sys:notice:remove']">
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

import { listNotice, delNotice } from '@/api/system/notice'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Notice',
  components: {
  },
  mixins: [tableMixin],
  dicts: ['sys_notice_status', 'sys_notice_type'],
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
      total: 0,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      columns: [
        {
          title: '公告编号',
          dataIndex: 'noticeId',
          align: 'center'
        },
        {
          title: '公告标题',
          dataIndex: 'noticeTitle',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '公告类型',
          dataIndex: 'noticeType',
          scopedSlots: { customRender: 'noticeType' },
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '操作人员',
          dataIndex: 'createBy',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
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
    /** 查询公告列表 */
    getList () {
      this.loading = true
      listNotice(this.queryParam).then(response => {
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
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
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
      this.ids = this.selectedRows.map(item => item.noticeId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const noticeIds = row.noticeId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + noticeIds + '的数据',
        onOk () {
          return delNotice(noticeIds)
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
     /** 新增按钮操作 */
    handleAdd () {
      this.$router.push({
        name: 'NoticeForm',
        params:
        {
          id: undefined,
          formTitle: '添加公告'
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      const noticeId = row ? row.noticeId : ids
      this.$router.push({
        name: 'NoticeForm',
        params:
        {
          id: noticeId,
          formTitle: '修改公告'
        }
      })
    }
  }
}
</script>

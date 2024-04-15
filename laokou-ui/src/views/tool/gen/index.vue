<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="表名称">
                <a-input v-model="queryParam.tableName" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="表描述">
                <a-input v-model="queryParam.tableComment" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="创建时间">
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
      <div class="table-operations">
        <a-button type="primary" @click="$refs.importTable.show()" v-hasPermi="['tool:gen:import']">
          <a-icon type="cloud-upload" />导入
        </a-button>
        <a-button type="primary" @click="handleGenTable" v-hasPermi="['tool:gen:code']">
          <a-icon type="cloud-download" />生成
        </a-button>
        <a-button type="primary" :disabled="single" @click="handleEditTable" v-hasPermi="['tool:gen:edit']">
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['tool:gen:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button
          type="dashed"
          shape="circle"
          :loading="loading"
          :style="{float: 'right'}"
          icon="reload"
          @click="getList" />
      </div>
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="tableId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered">
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="updateTime" slot-scope="text, record">
          {{ parseTime(record.updateTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="handleEditTable(record)" v-hasPermi="['tool:gen:edit']">
            <a-icon type="edit" />
            编辑
          </a>
          <a-divider type="vertical" v-hasPermi="['tool:gen:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['tool:gen:remove']">
            <a-icon type="delete" />
            删除
          </a>
          <a-divider type="vertical" v-hasPermi="['tool:gen:preview', 'tool:gen:edit', 'tool:gen:code']" />
          <a-dropdown v-hasPermi="['tool:gen:preview', 'tool:gen:edit', 'tool:gen:code']">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              <a-icon type="double-right" />
              更多
            </a>
            <a-menu slot="overlay">
              <a-menu-item v-hasPermi="['tool:gen:preview']">
                <a @click="$refs.previewCode.handlePreview(record)" v-hasPermi="['tool:gen:preview']">
                  <a-icon type="eye" />
                  预览
                </a>
              </a-menu-item>
              <a-menu-item v-hasPermi="['tool:gen:edit']">
                <a @click="handleSynchDb(record)">
                  <a-icon type="cloud-sync" />
                  同步
                </a>
              </a-menu-item>
              <a-menu-item v-hasPermi="['tool:gen:code']">
                <a @click="handleGenTable(record)">
                  <a-icon type="cloud-download" />
                  生成代码
                </a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
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
      <!-- 预览 -->
      <preview-code ref="previewCode" />
      <!-- 导入 -->
      <import-table ref="importTable" @ok="handleOk" />
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { delTable, listTable, synchDb, genCode } from '@/api/tool/gen'
import PreviewCode from './modules/PreviewCode'
import ImportTable from './modules/ImportTable'
import { downLoadZip } from '@/utils/zipdownload'
import { tableMixin } from '@/store/table-mixin'
export default {
  name: 'Gen',
  components: {
    PreviewCode,
    ImportTable
  },
  mixins: [tableMixin],
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
      // 选中表数组
      tableNames: [],
      loading: false,
      total: 0,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      },
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: 'tableId',
          align: 'center'
        },
        {
          title: '表名称',
          dataIndex: 'tableName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '表描述',
          dataIndex: 'tableComment',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '实体',
          dataIndex: 'className',
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
          title: '更新时间',
          dataIndex: 'updateTime',
          scopedSlots: { customRender: 'updateTime' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '20%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  created () {
    this.getList()
  },
  methods: {
    /** 查询表集合 */
    getList () {
      this.loading = true
      listTable(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
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
        tableName: undefined,
        tableComment: undefined
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
      this.ids = this.selectedRows.map(item => item.tableId)
      this.tableNames = this.selectedRows.map(item => item.tableName)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const tableIds = row.tableId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + tableIds + '的数据',
        onOk () {
          return delTable(tableIds)
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
    /** 同步数据库操作 */
    handleSynchDb (row) {
      var that = this
      const tableName = row.tableName
      this.$confirm({
        title: '确认强制同步数据?',
        content: '当前同步表名为' + tableName + '的数据',
        onOk () {
          return synchDb(tableName)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '同步成功',
                3
              )
          })
        },
        onCancel () {}
      })
    },
    /** 修改按钮操作 */
    handleEditTable (row) {
      const tableId = row.tableId || this.ids[0]
      this.$router.push({
        name: 'GenEdit',
        params:
        {
          tableId: tableId
        }
      })
    },
    /** 生成代码操作 */
    handleGenTable (row) {
      const tableNames = row.tableName || this.tableNames
      if (tableNames === '') {
        this.$message.error(
          '请选择要生成的数据',
          3
        )
        return
      }
      if (row.genType === '1') {
        genCode(row.tableName).then(response => {
          this.msgSuccess('成功生成到自定义路径：' + row.genPath)
        })
      } else {
        downLoadZip('/tool/gen/batchGenCode?tables=' + tableNames, 'ruoyi')
      }
    },
    handleOk () {
      this.resetQuery()
    }
  }
}
</script>

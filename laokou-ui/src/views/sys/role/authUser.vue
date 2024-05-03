<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="用户名称">
                <a-input v-model="queryParam.userName" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="手机号码">
                <a-input v-model="queryParam.phonenumber" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <span class="table-page-search-submitButtons">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.selectUser.handleAuthUser()" v-hasPermi="['sys:role:add']">
          <a-icon type="plus" />
          添加用户
        </a-button>
        <a-button type="danger" :loading="authing" :disabled="multiple" @click="cancelAuthUserAll" v-hasPermi="['sys:role:remove']">
          <a-icon type="delete" />
          取消批量授权
        </a-button>
        <a-button type="primary" @click="back">
          <a-icon type="edit" />
          返回
        </a-button>
        <table-setting
          :style="{float: 'right'}"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList" />
      </div>
      <select-user
        ref="selectUser"
        :roleId="queryParam.roleId"
        :statusOptions="dict.type['sys_normal_disable']"
        @ok="getList"
      />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="userId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered">
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.status"/>
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="cancelAuthUser(record)" v-hasPermi="['sys:role:remove']">
            <a-icon type="edit" />
            取消授权
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

import { allocatedUserList, authUserCancel, authUserCancelAll } from '@/api/system/role'
import CreateForm from './modules/CreateForm'
import CreateDataScopeForm from './modules/CreateDataScopeForm'
import SelectUser from './modules/SelectUser'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'AuthUser',
  components: {
    CreateForm,
    CreateDataScopeForm,
    SelectUser
  },
  mixins: [tableMixin],
  dicts: ['sys_normal_disable'],
  data () {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      authing: false,
      total: 0,
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        roleId: '',
        userName: undefined,
        phonenumber: undefined
      },
      columns: [
        {
          title: '用户名称',
          dataIndex: 'userName',
          align: 'center'
        },
        {
          title: '用户昵称',
          dataIndex: 'nickName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '邮箱',
          dataIndex: 'email',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '手机',
          dataIndex: 'phonenumber',
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
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
          width: '20%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {
  },
  created () {
  },
  mounted () {
    const roleId = this.$route.query && this.$route.query.roleId
    if (roleId) {
      this.queryParam.roleId = roleId
      this.getList()
    }
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询授权用户列表 */
    getList () {
      this.loading = true
      allocatedUserList(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
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
        roleId: this.queryParam.roleId,
        userName: undefined,
        phonenumber: undefined
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
      this.ids = this.selectedRows.map(item => item.userId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    /** 取消授权按钮操作 */
    cancelAuthUser (row) {
      var that = this
      const userName = row.userName
      const roleId = this.queryParam.roleId
      this.$confirm({
        title: '确认要取消该用户的角色吗?',
        content: '当前选中用户' + userName,
        onOk () {
          const param = {
            userId: row.userId,
            roleId: roleId
          }
          return authUserCancel(param).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(
              '取消授权成功',
              3
            )
          })
        },
        onCancel () {}
      })
    },
    /** 批量取消授权按钮操作 */
    cancelAuthUserAll () {
      var that = this
      const roleId = this.queryParam.roleId
      this.$confirm({
        title: '是否取消选中用户授权数据项?',
        onOk () {
          const param = {
            roleId: roleId,
            userIds: that.ids
          }
          that.authing = true
          return authUserCancelAll(param).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(
              '取消授权成功',
              3
            )
          }).finally(() => {
            this.authing = false
          })
        },
        onCancel () {}
      })
    },
    back () {
      this.$router.push({ path: '/sys/role' })
    }
  }
}
</script>

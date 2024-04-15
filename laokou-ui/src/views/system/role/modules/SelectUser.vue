<template>
  <a-modal
    ref="authRole"
    :title="'选择用户'"
    :width="900"
    :visible="visible"
    :confirm-loading="submitLoading"
    @cancel="close"
    @ok="confirm"
  >

    <div class="page-header-content">
      <a-card :bordered="false" class="content">
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
        <a-table
          :loading="loading"
          size="small"
          rowKey="userId"
          :columns="columns"
          :data-source="list"
          :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          :scroll="{ y: tableHeight }"
          :pagination="false">
          <span slot="status" slot-scope="text, record">
            <dict-tag :options="statusOptions" :value="record.status"/>
          </span>
          <span slot="createTime" slot-scope="text, record">
            {{ parseTime(record.createTime) }}
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
    </div>
  </a-modal>
</template>

<script>

import { unallocatedUserList, authUserSelectAll } from '@/api/system/role'

export default {
  name: 'AuthUser',
  props: {
    roleId: {
      type: String,
      required: true
    },
    statusOptions: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      // 表格数据
      list: [],
      user: {},
      selectedRowKeys: [],
      selectedRows: [],
        // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 500 + 'px',
      // 选中表数组
      userIds: [],
      loading: false,
      total: 0,
      // 当前控件配置:
      submitLoading: false,
      visible: false,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        roleId: undefined,
        userName: undefined,
        phonenumber: undefined
      },
      // 表格属性
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
          title: '创建时间',
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          ellipsis: true,
          align: 'center'
        }
      ]
    }
  },
  created () {
  },
  methods: {
    // 查询表数据
    getList () {
      this.loading = true
      unallocatedUserList(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 关闭模态框
    close () {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
    },
    // 打开(由外面的组件调用)
    handleAuthUser () {
      this.queryParam.roleId = this.roleId
      this.visible = true
      this.getList()
    },
    // 确认
    confirm () {
      const param = {
        roleId: this.queryParam.roleId,
        userIds: this.userIds
      }
      this.submitLoading = true
      authUserSelectAll(param).then(res => {
        this.$message.success(res.msg)
        this.visible = false
        this.$emit('ok')
      }).finally(() => {
        this.submitLoading = false
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
      this.userIds = this.selectedRows.map(item => item.userId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    }
  }
}
</script>

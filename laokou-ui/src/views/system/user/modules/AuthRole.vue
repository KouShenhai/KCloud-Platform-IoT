<template>
  <a-modal
    ref="authRole"
    :title="'分配角色'"
    :width="900"
    :visible="visible"
    :confirm-loading="submitLoading"
    @cancel="close"
    @ok="confirm"
  >

    <div class="page-header-content">
      <a-card :bordered="false" class="content">
        <a-table
          :loading="loading"
          :size="tableSize"
          rowKey="roleId"
          :columns="columns"
          :data-source="list"
          :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          :scroll="{ y: tableHeight }"
          :pagination="false"
          :bordered="tableBordered">
          <span slot="createTime" slot-scope="text, record">
            {{ parseTime(record.createTime) }}
          </span>
        </a-table>
      </a-card>
    </div>
  </a-modal>
</template>

<script>

import { getAuthRole, updateAuthRole } from '@/api/system/user'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'AuthRole',
  props: {
  },
  components: {
  },
  mixins: [tableMixin],
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
      roleIds: [],
      loading: false,
      // 当前控件配置:
      submitLoading: false,
      visible: false,
      // 表格属性
      columns: [
        {
          title: '角色编号',
          dataIndex: 'roleId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '角色名称',
          dataIndex: 'roleName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '权限字符',
          dataIndex: 'roleKey',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          align: 'center'
        }
      ]
    }
  },
  created () {
  },
  methods: {
    // 查询表数据
    getList (userId) {
      this.loading = true
      // getAuthRole(userId).then(res => {
      //   if (res.code === 200) {
      //     this.list = res.roles
      //     this.user = res.user
      //     this.$nextTick(() => {
      //       const roleIds = res.user.roles.map(item => item.roleId)
      //       this.roleIds = roleIds
      //       this.selectedRowKeys = roleIds
      //     })
      //     this.loading = false
      //   }
      // })
    },
    // 关闭模态框
    close () {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
    },
    // 打开(由外面的组件调用)
    handleAuthRole (row) {
      this.visible = true
      this.getList(row.userId)
    },
    // 确认
    confirm () {
      this.submitLoading = true
      updateAuthRole({ userId: this.user.userId, roleIds: this.roleIds }).then(res => {
        this.$message.success(res.msg)
        this.visible = false
        this.$emit('ok')
      }).finally(() => {
        this.submitLoading = false
      })
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.roleIds = this.selectedRows.map(item => item.roleId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    }
  }
}
</script>

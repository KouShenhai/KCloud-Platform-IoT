<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="菜单名称">
                <a-input v-model="queryParam.menuName" placeholder="请输入" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="状态">
                <a-select placeholder="请选择" v-model="queryParam.status" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in dict.type['sys_normal_disable']" :key="index" :value="d.value">{{ d.label }}</a-select-option>
                </a-select>
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['sys:menu:add']">
          <a-icon type="plus" />新增
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
        :menuOptions="menuOptions"
        :statusOptions="dict.type['sys_normal_disable']"
        :visibleOptions="dict.type['sys_show_hide']"
        @ok="getList"
        @select-tree="getTreeselect"
      />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="menuId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered">
        <span slot="icon" slot-scope="text">
          <a-icon :component="allIcon[text + 'Icon']" v-if="allIcon[text + 'Icon']"/>
          <a-icon :type="text" v-if="!allIcon[text + 'Icon']"/>
        </span>
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.status" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record)" v-hasPermi="['sys:menu:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-if="record.menuId != 0" v-hasPermi="['sys:menu:remove']" />
          <a @click="handleDelete(record)" v-if="record.menuId != 0" v-hasPermi="['sys:menu:remove']">
            <a-icon type="delete" />删除
          </a>
        </span>
      </a-table>
    </a-card>
  </page-header-wrapper>
</template>

<script>

import { listMenu, delMenu } from '@/api/system/menu'
import CreateForm from './modules/CreateForm'
import allIcon from '@/core/icons'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Menu',
  components: {
    CreateForm
  },
  mixins: [tableMixin],
  dicts: ['sys_normal_disable', 'sys_show_hide'],
  data () {
    return {
      allIcon,
      iconVisible: false,
      list: [],
      // 部门树选项
      menuOptions: [],
      loading: false,
      queryParam: {
        name: undefined,
        status: undefined
      },
      columns: [
        {
          title: '菜单名称',
          dataIndex: 'menuName',
          ellipsis: true,
          width: '15%'
        },
        {
          title: '图标',
          dataIndex: 'icon',
          scopedSlots: { customRender: 'icon' },
          width: '5%',
          align: 'center'
        },
        {
          title: '排序',
          dataIndex: 'orderNum',
          width: '5%',
          align: 'center'
        },
        {
          title: '权限标识',
          dataIndex: 'perms',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '组件路径',
          dataIndex: 'component',
          scopedSlots: { customRender: 'component' },
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
          ellipsis: true,
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
    this.getList()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询菜单列表 */
    getList () {
      this.loading = true
      listMenu(this.queryParam).then(response => {
          this.list = this.handleTree(response.data, 'menuId')
          this.loading = false
        }
      )
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParam = {
        name: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    /** 查询菜单下拉树结构 */
    getTreeselect () {
      listMenu().then(response => {
        this.menuOptions = []
        const menu = { menuId: 0, menuName: '主目录', children: [] }
        menu.children = this.handleTree(response.data, 'menuId')
        this.menuOptions.push(menu)
      })
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const menuId = row.menuId
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + menuId + '的数据',
        onOk () {
          return delMenu(menuId)
            .then(() => {
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
          })
        },
        onCancel () {}
      })
    }
  }
}
</script>

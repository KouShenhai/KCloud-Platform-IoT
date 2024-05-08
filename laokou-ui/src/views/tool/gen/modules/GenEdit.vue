<template>
  <page-header-wrapper @back="back">
    <template v-slot:breadcrumb>
      {{ formTitle }}
    </template>
    <template v-slot:title>
      {{ formTitle }}
    </template>
    <template v-slot:content>
    </template>
    <template v-slot:extraContent>
    </template>
    <a-result title="请至少选择一个表修改" v-if="tableId == 0">
      <template #icon>
        <a-icon type="smile" theme="twoTone" />
      </template>
      <template #extra>
        <a-button type="primary" @click="back">
          返回生成页
        </a-button>
      </template>
    </a-result>
    <a-card :bordered="false" v-if="tableId != 0">
      <a-tabs default-active-key="2">
        <a-tab-pane key="1" tab="基本信息" force-render>
          <basic-info-form ref="basicInfo" :info="info" />
        </a-tab-pane>
        <a-tab-pane key="2" tab="字段信息" force-render>
          <!-- 表格 -->
          <a-table
            ref="table"
            size="small"
            :columns="columns"
            :loading="tableLoading"
            :data-source="tableList"
            row-key="columnId"
            :pagination="false"
          >
            <!-- 字段描述 -->
            <template slot="columnComment" slot-scope="text, record">
              <a-input v-model="record.columnComment"></a-input>
            </template>
            <!-- Java类型 -->
            <template slot="javaType" slot-scope="text, record" style="width:100%">
              <a-select v-model="record.javaType">
                <a-select-option value="Long">Long</a-select-option>
                <a-select-option value="String">String</a-select-option>
                <a-select-option value="Integer">Integer</a-select-option>
                <a-select-option value="Double">Double</a-select-option>
                <a-select-option value="BigDecimal">BigDecimal</a-select-option>
                <a-select-option value="Date">Date</a-select-option>
                <a-select-option value="Boolean">Boolean</a-select-option>
              </a-select>
            </template>
            <!-- Java属性 -->
            <template slot="javaField" slot-scope="text, record">
              <a-input v-model="record.javaField"></a-input>
            </template>
            <!-- 插入 -->
            <template slot="isInsert" slot-scope="text, record">
              <a-checkbox v-model="record.isInsert" ></a-checkbox>
            </template>
            <!-- 编辑 -->
            <template slot="isEdit" slot-scope="text, record">
              <a-checkbox v-model="record.isEdit"></a-checkbox>
            </template>
            <!-- 列表 -->
            <template slot="isList" slot-scope="text, record">
              <a-checkbox v-model="record.isList"></a-checkbox>
            </template>
            <!-- 查询 -->
            <template slot="isQuery" slot-scope="text, record">
              <a-checkbox v-model="record.isQuery"></a-checkbox>
            </template>
            <!-- 查询方式 -->
            <template slot="queryType" slot-scope="text, record">
              <a-select v-model="record.queryType" style="width:100%">
                <a-select-option value="EQ">=</a-select-option>
                <a-select-option value="NE">!=</a-select-option>
                <a-select-option value="GT">></a-select-option>
                <a-select-option value="GTE">>=</a-select-option>
                <a-select-option value="LT">&lt;</a-select-option>
                <a-select-option value="LTE">&lt;=</a-select-option>
                <a-select-option value="LIKE">LIKE</a-select-option>
                <a-select-option value="BETWEEN">BETWEEN</a-select-option>
              </a-select>
            </template>
            <!-- 必填 -->
            <template slot="isRequired" slot-scope="text, record">
              <a-checkbox v-model="record.isRequired"></a-checkbox>
            </template>
            <!-- 显示类型 -->
            <template slot="htmlType" slot-scope="text, record">
              <a-select v-model="record.htmlType" style="width:100%">
                <a-select-option value="input">文本框</a-select-option>
                <a-select-option value="textarea">文本域</a-select-option>
                <a-select-option value="select">下拉框</a-select-option>
                <a-select-option value="radio">单选框</a-select-option>
                <a-select-option value="checkbox">复选框</a-select-option>
                <a-select-option value="datetime">日期控件</a-select-option>
                <a-select-option value="imageUpload">图片上传</a-select-option>
                <a-select-option value="fileUpload">文件上传</a-select-option>
                <a-select-option value="editor">富文本控件</a-select-option>
              </a-select>
            </template>
            <!-- 字典类型 -->
            <template slot="dictType" slot-scope="text, record">
              <a-select v-model="record.dictType" placeholder="请选择" style="width:100%">
                <a-select-option
                  v-for="item in dictOptions"
                  :key="item.dictType"
                  :value="item.dictType">
                  {{ item.dictName }}
                </a-select-option>
              </a-select>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="3" tab="生成信息" force-render>
          <gen-info-form ref="genInfo" :info="info" :tables="tables" :menus="menus"/>
        </a-tab-pane>
      </a-tabs>
      <a-form label-width="100px">
        <footer-tool-bar>
          <a-space>
            <a-button type="primary" :loading="submitLoading" @click="submitForm">
              保存
            </a-button>
            <a-button type="dashed" @click="back">
              取消
            </a-button>
          </a-space>
        </footer-tool-bar>
      </a-form>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { getGenTable, updateGenTable } from '@/api/tool/gen'
import { optionselect as getDictOptionselect } from '@/api/sys/dict/type'
import { listMenu as getMenuTreeselect } from '@/api/system/menu'
import BasicInfoForm from './BasicInfoForm'
import GenInfoForm from './GenInfoForm'
export default {
  name: 'GenEdit',
  components: {
    BasicInfoForm,
    GenInfoForm
  },
  data () {
    return {
      tableId: 0,
      formTitle: '修改生成配置',
      // 表格加载
      tableLoading: false,
      submitLoading: false,
      // 字典信息
      dictOptions: [],
      // 菜单信息
      menus: [],
      // 表信息
      tables: [],
      // 表详细信息
      info: {},
      // 表数据
      tableList: [],
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: 'columnId',
          scopedSlots: { customRender: 'columnId' },
          align: 'center',
          width: '5%'
        },
        {
          title: '字段列名',
          dataIndex: 'columnName',
          align: 'center',
          ellipsis: true,
          width: '10%'
        },
        {
          title: '字段描述',
          dataIndex: 'columnComment',
          scopedSlots: { customRender: 'columnComment' },
          align: 'center',
          width: '8%'
        },
        {
          title: '物理类型',
          dataIndex: 'columnType',
          scopedSlots: { customRender: 'columnType' },
          align: 'center',
          ellipsis: true,
          width: '10%'
        },
        {
          title: 'Java类型',
          dataIndex: 'javaType',
          scopedSlots: { customRender: 'javaType' },
          align: 'center',
          width: '8%'
        },
        {
          title: 'java属性',
          dataIndex: 'javaField',
          scopedSlots: { customRender: 'javaField' },
          align: 'center',
          width: '10%'
        },
        {
          title: '插入',
          dataIndex: 'isInsert',
          scopedSlots: { customRender: 'isInsert' },
          align: 'center',
          width: '3%'
        },
        {
          title: '编辑',
          dataIndex: 'isEdit',
          scopedSlots: { customRender: 'isEdit' },
          align: 'center',
          width: '3%'
        },
        {
          title: '列表',
          dataIndex: 'isList',
          scopedSlots: { customRender: 'isList' },
          align: 'center',
          width: '3%'
        },
        {
          title: '查询',
          dataIndex: 'isQuery',
          scopedSlots: { customRender: 'isQuery' },
          align: 'center',
          width: '3%'
        },
        {
          title: '查询方式',
          dataIndex: 'queryType',
          scopedSlots: { customRender: 'queryType' },
          align: 'center',
          width: '10%'
        },
        {
          title: '必填',
          dataIndex: 'isRequired',
          scopedSlots: { customRender: 'isRequired' },
          align: 'center',
          width: '3%'
        },
        {
          title: '显示类型',
          dataIndex: 'htmlType',
          scopedSlots: { customRender: 'htmlType' },
          align: 'center',
          width: '10%'
        },
        {
          title: '字典类型',
          dataIndex: 'dictType',
          scopedSlots: { customRender: 'dictType' },
          align: 'center',
          width: '10%'
        }
      ]
    }
  },
  created () {
    const tableId = this.$route.params && this.$route.params.tableId
    if (tableId) {
      this.tableId = tableId
    }
    this.tableLoading = true
    if (this.tableId) {
      // 获取表详细信息
      getGenTable(this.tableId).then(res => {
        const tableList = res.data.rows
        tableList.forEach(e => {
          e.isInsert = e.isInsert === '1'
          e.isEdit = e.isEdit === '1'
          e.isList = e.isList === '1'
          e.isQuery = e.isQuery === '1'
          e.isRequired = e.isRequired === '1'
        })
        this.tableList = tableList
        this.info = res.data.info
        this.tables = res.data.tables
        this.tableLoading = false
      })
      /** 查询字典下拉列表 */
      getDictOptionselect().then(response => {
        this.dictOptions = response.data
      })
      /** 查询菜单下拉列表 */
      getMenuTreeselect().then(response => {
        this.menus = this.handleTree(response.data, 'menuId')
      })
    }
  },
  methods: {
      /** 提交按钮 */
    submitForm () {
      const basicForm = this.$refs.basicInfo.info
      const genForm = this.$refs.genInfo.info

      if (basicForm && genForm) {
        this.submitLoading = true
        const genTable = Object.assign({}, basicForm, genForm)
        const tableList = this.tableList
        tableList.forEach(e => {
          e.isInsert = e.isInsert ? '1' : '0'
          e.isEdit = e.isEdit ? '1' : '0'
          e.isList = e.isList ? '1' : '0'
          e.isQuery = e.isQuery ? '1' : '0'
          e.isRequired = e.isRequired ? '1' : '0'
        })
        genTable.columns = tableList
        genTable.params = {
          treeCode: genTable.treeCode,
          treeName: genTable.treeName,
          treeParentCode: genTable.treeParentCode,
          parentMenuId: genTable.parentMenuId
        }
        updateGenTable(genTable).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.back()
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => {
          this.submitLoading = false
        })
      } else {
        this.msgError('表单校验未通过，请重新检查提交内容')
      }
    },
    /** 关闭按钮 */
    back () {
      this.$router.push('/tool/gen')
    }
  }
}
</script>

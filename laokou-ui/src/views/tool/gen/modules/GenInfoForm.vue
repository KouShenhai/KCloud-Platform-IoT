<template>
  <div>
    <a-form-model
      ref="genInfoForm"
      :model="info"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-row>
        <a-col :span="12">
          <a-form-model-item prop="tplCategory">
            <span slot="label">
              生成模板
            </span>
            <a-select v-model="info.tplCategory" placeholder="请选择" @change="tplSelectChange">
              <a-select-option value="crud">单表（增删改查）</a-select-option>
              <a-select-option value="tree">树表（增删改查）</a-select-option>
              <a-select-option value="sub">主子表（增删改查）</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="packageName">
            <span slot="label">
              生成包路径
              <a-tooltip>
                <template slot="title">
                  生成在哪个java包下，例如 com.ruoyi.system
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input placeholder="请输入生成包路径" v-model="info.packageName" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="moduleName">
            <span slot="label">
              生成模块名
              <a-tooltip>
                <template slot="title">
                  可理解为子系统名，例如 system
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input placeholder="请输入生成模块名" v-model="info.moduleName" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="businessName">
            <span slot="label">
              生成业务名
              <a-tooltip>
                <template slot="title">
                  可理解为功能英文名，例如 user
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input placeholder="请输入生成业务名" v-model="info.businessName" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="functionName">
            <span slot="label">
              生成功能名
              <a-tooltip>
                <template slot="title">
                  用作类描述，例如 用户
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input placeholder="请输入生成功能名" v-model="info.functionName" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              上级菜单
              <a-tooltip>
                <template slot="title">
                  分配到指定菜单下，例如 系统管理
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-tree-select
              v-model="info.parentMenuId"
              :tree-data="menus"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="请选择系统菜单"
              :replaceFields="treeReplaceFields"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              生成代码方式
              <a-tooltip>
                <template slot="title">
                  默认为zip压缩包下载，也可以自定义生成路径
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-radio-group v-model="info.genType">
              <a-radio :value="'0'">zip压缩包</a-radio>
              <a-radio :value="'1'">自定义路径</a-radio>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
        </a-col>
        <a-col :span="24" v-if="info.genType == '1'">
          <a-form-model-item :labelCol="{ xs: { span: 6 },sm: { span: 3 } }" :wrapperCol="{ xs: { span: 42 },sm: { span: 21 } }">
            <span slot="label">
              自定义路径
              <a-tooltip>
                <template slot="title">
                  填写磁盘绝对路径，若不填写，则生成到当前Web项目下
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input v-model="info.genPath">
              <a-dropdown slot="suffix">
                <a-button style="width:100%;"> 最近路径快速选择 <a-icon type="down" /> </a-button>
                <a-menu slot="overlay">
                  <a-menu-item key="1" @click.native="info.genPath = '/'"> <a-icon type="user" />1st menu item </a-menu-item>
                </a-menu>
              </a-dropdown>
            </a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row v-show="info.tplCategory == 'tree'">
        <a-divider orientation="left">
          其他信息
        </a-divider>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              树编码字段
              <a-tooltip>
                <template slot="title">
                  树显示的编码字段名， 如：dept_id
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeCode" placeholder="请选择">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName" >
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              树父编码字段
              <a-tooltip>
                <template slot="title">
                  树显示的父编码字段名， 如：parent_Id
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeParentCode" placeholder="请选择">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName" >
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              树名称字段
              <a-tooltip>
                <template slot="title">
                  树节点的显示名称字段名， 如：dept_name
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeName" placeholder="请选择">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName" >
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
      </a-row>
      <!-- 主子表 -->
      <a-row v-show="info.tplCategory == 'sub'">
        <a-divider orientation="left">
          关联信息
        </a-divider>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              关联子表的表名
              <a-tooltip>
                <template slot="title">
                  关联子表的表名， 如：sys_user
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.subTableName" placeholder="请选择" @change="subSelectChange">
              <a-select-option v-for="(item, index) in tables" :key="index" :value="item.tableName" >
                {{ item.tableName + '：' + item.tableComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              子表关联的外键名
              <a-tooltip>
                <template slot="title">
                  子表关联的外键名， 如：user_id
                </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.subTableFkName" placeholder="请选择">
              <a-select-option v-for="(item, index) in subColumns" :key="index" :value="item.columnName" >
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
export default {
  name: 'GenInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    },
    tables: {
      type: Array,
      default: null
    },
    menus: {
      type: Array,
      default: null
    }
  },
  data () {
    return {
      visible: false,
      loading: false,
      // 模态框数据
      data: {},
      subColumns: [],
      rules: {
        tplCategory: [{ required: true, message: '请选择生成模板', trigger: 'blur' }],
        packageName: [{ required: true, message: '请输入生成包路径', trigger: 'blur' }],
        moduleName: [{ required: true, message: '请输入生成模块名', trigger: 'blur' }],
        businessName: [{ required: true, message: '请输入生成业务名', trigger: 'blur' }],
        functionName: [{ required: true, message: '请输入生成功能名', trigger: 'blur' }]
      },
      labelCol: {
        xs: { span: 12 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 }
      },
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: [],
      treeReplaceFields: {
        children: 'children',
        title: 'menuName',
        key: 'menuId',
        value: 'menuId'
      }
    }
  },
  watch: {
    'info.subTableName': function (val) {
      this.setSubTableColumns(val)
    }
  },
  methods: {
    // 关闭模态框
    close () {
      this.visible = false
    },
    // 打开抽屉(由外面的组件调用)
    show (data) {
      if (data) {
        this.data = data
      }
      this.visible = true
    },
    /** 选择子表名触发 */
    subSelectChange () {
      this.info.subTableFkName = ''
    },
    /** 选择生成模板触发 */
    tplSelectChange (value) {
      if (value !== 'sub') {
        this.info.subTableName = ''
        this.info.subTableFkName = ''
      }
    },
    /** 设置关联外键 */
    setSubTableColumns (value) {
      for (var item in this.tables) {
        const name = this.tables[item].tableName
        if (value === name) {
          this.subColumns = this.tables[item].columns
          break
        }
      }
    }
  }
}
</script>

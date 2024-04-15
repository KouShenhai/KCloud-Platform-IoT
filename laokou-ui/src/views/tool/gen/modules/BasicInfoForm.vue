<template>
  <div>
    <a-form-model
      ref="basicInfoForm"
      :model="info"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="表名称" prop="tableName">
            <a-input placeholder="请输入仓库名称" v-model="info.tableName" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="表描述" prop="tableComment">
            <a-input placeholder="请输入仓库名称" v-model="info.tableComment" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="实体类名称" prop="className">
            <a-input placeholder="请输入仓库名称" v-model="info.className" />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="作者" prop="functionAuthor">
            <a-input placeholder="请输入仓库名称" v-model="info.functionAuthor" />
          </a-form-model-item>
        </a-col>
        <a-col :span="24" :pull="3">
          <a-form-model-item label="备注">
            <a-textarea placeholder="textarea with clear icon" allow-clear v-model="info.remark" />
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
export default {
  name: 'BasticInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      visible: false,
      loading: false,
      // 模态框数据
      data: {},
      rules: {
        tableName: [{ required: true, message: '请输入表名称', trigger: 'blur' }],
        tableComment: [{ required: true, message: '请输入表描述', trigger: 'blur' }],
        className: [{ required: true, message: '请输入实体类名称', trigger: 'blur' }],
        functionAuthor: [{ required: true, message: '请输入作者', trigger: 'blur' }]
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
      statusOptions: []
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
    }
  }
}
</script>

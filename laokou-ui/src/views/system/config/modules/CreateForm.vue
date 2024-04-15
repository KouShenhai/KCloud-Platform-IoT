<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="参数名称" prop="configName">
        <a-input v-model="form.configName" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="参数键名" prop="configKey">
        <a-input v-model="form.configKey" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="参数键值" prop="configValue">
        <a-input v-model="form.configValue" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="系统内置" prop="configType">
        <a-select placeholder="是否内置" v-model="form.configType">
          <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.value" >{{ d.label }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="备注" prop="remark">
        <a-input v-model="form.remark" placeholder="请输入备注" type="textarea" allow-clear />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>

import { getConfig, addConfig, updateConfig } from '@/api/system/config'

export default {
  name: 'CreateForm',
  props: {
    typeOptions: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'Y',
        remark: undefined
      },
      open: false,
      rules: {
        configName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
        configKey: [{ required: true, message: '参数键名不能为空', trigger: 'blur' }],
        configValue: [{ required: true, message: '参数键值不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  methods: {
    onClose () {
      this.open = false
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'Y',
        remark: undefined
      }
    },
     /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.open = true
      this.formTitle = '添加参数'
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      const configId = row ? row.configId : ids
      getConfig(configId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改参数'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.configId !== undefined) {
            updateConfig(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            addConfig(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

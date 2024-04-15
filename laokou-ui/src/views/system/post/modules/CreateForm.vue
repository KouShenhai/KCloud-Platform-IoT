<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="岗位名称" prop="postName">
        <a-input v-model="form.postName" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="岗位编码" prop="postCode">
        <a-input v-model="form.postCode" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="显示顺序" prop="postSort">
        <a-input-number v-model="form.postSort" :min="0" style="width: 100%"/>
      </a-form-model-item>
      <a-form-model-item label="状态" prop="status">
        <a-select placeholder="请选择" v-model="form.status">
          <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.value" >{{ d.label }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="备注" prop="remark">
        <a-input v-model="form.remark" placeholder="请输入" type="textarea" allow-clear />
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

import { getPost, addPost, updatePost } from '@/api/system/post'

export default {
  name: 'CreateForm',
  props: {
    statusOptions: {
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
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: '0',
        remark: undefined
      },
      open: false,
      rules: {
        postName: [{ required: true, message: '岗位编码不能为空', trigger: 'blur' }],
        postCode: [{ required: true, message: '岗位名称不能为空', trigger: 'blur' }],
        postSort: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }]
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
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: '0',
        remark: undefined
      }
    },
     /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.open = true
      this.formTitle = '添加岗位'
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      const postId = row ? row.postId : ids
      getPost(postId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改岗位'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.postId !== undefined) {
            updatePost(this.form).then(response => {
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
            addPost(this.form).then(response => {
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

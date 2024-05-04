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
    <template v-slot:extra>
      <a-space>
        <a-button type="primary" @click="handleSubmit">
          发布
        </a-button>
        <a-button type="dashed" @click="back">
          取消
        </a-button>
      </a-space>
    </template>
    <a-card :bordered="false">
      <a-row :gutter="12">
        <a-col :span="24">
          <a-form-model ref="baseForm" :model="form" :rules="baseRules" :wrapper-col="wrapperCol">
            <a-form-model-item prop="noticeTitle">
              <a-input size="large" v-model="form.noticeTitle" placeholder="请输入标题" />
            </a-form-model-item>
            <a-form-model-item prop="noticeContent">
              <editor v-model="form.noticeContent" />
            </a-form-model-item>
          </a-form-model>
        </a-col>
      </a-row>
    </a-card>
    <a-drawer width="30%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
      <a-divider orientation="left">
        <b>{{ formTitle }}</b>
      </a-divider>
      <a-form-model ref="form" :model="form" :rules="rules">
        <a-form-model-item label="公告类型" prop="noticeType">
          <a-select placeholder="请选择" v-model="form.noticeType">
            <a-select-option v-for="(d, index) in dict.type['sys_notice_type']" :key="index" :value="d.value" >{{ d.label }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="状态" prop="status">
          <a-radio-group v-model="form.status" button-style="solid">
            <a-radio-button v-for="(d, index) in dict.type['sys_notice_status']" :key="index" :value="d.value" >{{ d.label }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <div class="bottom-control">
          <a-space>
            <a-button type="primary" :loading="submitLoading" @click="submitForm">
              发布
            </a-button>
            <a-button type="dashed" @click="onClose">
              取消
            </a-button>
          </a-space>
        </div>
      </a-form-model>
    </a-drawer>
  </page-header-wrapper>
</template>

<script>

import { getNotice, addNotice, updateNotice } from '@/api/system/notice'
import Editor from '@/components/Editor'

export default {
  name: 'NoticeForm',
  components: {
    Editor
  },
  dicts: ['sys_notice_status', 'sys_notice_type'],
  data () {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 24 },
      submitLoading: false,
      total: 0,
      id: undefined,
      formTitle: '',
      // 表单参数
      form: {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: '',
        status: '0'
      },
      baseRules: {
        noticeTitle: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }]
      },
      rules: {
        noticeType: [{ required: true, message: '公告类型不能为空', trigger: 'change' }]
      },
      open: false
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
  mounted () {
    this.id = this.$route.params.id
    this.formTitle = this.$route.params.formTitle
    if (this.id) {
      this.handleUpdate(this.id)
    } else {
      this.handleAdd()
    }
  },
  methods: {
    // 表单重置
    reset () {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: '',
        status: '0'
      }
    },
     /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.formTitle = '添加公告'
    },
    /** 修改按钮操作 */
    handleUpdate (id) {
      this.reset()
      const noticeId = id
      getNotice(noticeId).then(response => {
        this.form = response.data
        this.formTitle = '修改公告'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.noticeId !== undefined) {
            updateNotice(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.back()
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            addNotice(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.back()
            }).finally(() => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },
    back () {
      this.$router.push('/sys/notice')
    },
    onClose () {
      this.open = false
    },
    handleSubmit () {
      this.$refs.baseForm.validate(valid => {
        if (valid) {
          this.open = true
        } else {
          return false
        }
      })
    }
  }
}
</script>

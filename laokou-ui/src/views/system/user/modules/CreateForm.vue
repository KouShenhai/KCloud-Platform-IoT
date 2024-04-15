<template>
  <!-- 增加修改 -->
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="用户昵称" prop="nickName">
        <a-input v-model="form.nickName" placeholder="请输入" :maxLength="30" />
      </a-form-model-item>
      <a-form-model-item label="部门" prop="deptId">
        <a-tree-select
          v-model="form.deptId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="deptOptions"
          placeholder="请选择"
          :replaceFields="replaceFields"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item>
      <a-form-model-item label="手机号" prop="phonenumber">
        <a-input v-model="form.phonenumber" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="邮箱" prop="email">
        <a-input v-model="form.email" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="用户名" prop="userName" v-if="form.userId == undefined">
        <a-input v-model="form.userName" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="密码" prop="password" v-if="form.userId == undefined">
        <a-input-password v-model="form.password" placeholder="请输入" :maxLength="20" />
      </a-form-model-item>
      <a-form-model-item label="性别" prop="sex">
        <a-radio-group v-model="form.sex" button-style="solid">
          <a-radio-button v-for="(d, index) in sexOptions" :key="index" :value="d.value">{{ d.label }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="状态" prop="status">
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.value">{{ d.label }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="岗位" prop="postIds">
        <a-select
          mode="multiple"
          v-model="form.postIds"
          placeholder="请选择"
        >
          <a-select-option v-for="(d, index) in postOptions" :key="index" :value="d.postId">
            {{ d.postName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="角色" prop="roleIds">
        <a-select
          mode="multiple"
          v-model="form.roleIds"
          placeholder="请选择"
        >
          <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
            {{ d.roleName }}
          </a-select-option>
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

import { getUser, addUser, updateUser } from '@/api/system/user'

export default {
  name: 'CreateForm',
  props: {
    deptOptions: {
      type: Array,
      required: true
    },
    statusOptions: {
      type: Array,
      required: true
    },
    sexOptions: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      submitLoading: false,
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 默认密码
      initPassword: undefined,
      formTitle: '',
      // 表单参数
      form: {
        userId: undefined,
        deptId: 0,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: '3',
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      },
      open: false,
      rules: {
        userName: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        deptId: [
          { required: true, message: '部门不能为空', trigger: 'change' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          {
            type: 'email',
            message: '请正确填写邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phonenumber: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请正确填写手机号',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {
  },
  created () {
    this.getConfigKey('sys.user.initPassword').then(response => {
      this.initPassword = response.msg
    })
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: '3',
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      }
    },
     /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.$emit('select-tree')
      getUser().then(response => {
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.open = true
        this.formTitle = '新增用户'
        this.form.password = this.initPassword
      })
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      this.$emit('select-tree')
      const userId = row ? row.userId : ids
      getUser(userId).then(response => {
        this.form = response.data
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.form.postIds = response.postIds
        this.form.roleIds = response.roleIds
        this.open = true
        this.formTitle = '修改用户'
        this.form.password = ''
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.userId !== undefined) {
            updateUser(this.form).then(response => {
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
            addUser(this.form).then(response => {
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

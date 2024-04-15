<template>
  <div class="main user-layout-register">
    <h3><span>注册</span></h3>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-alert
        v-if="isRegisterError"
        type="error"
        showIcon
        style="margin-bottom: 24px;"
        :message="registerErrorInfo"
        closable
        :after-close="handleCloseRegisterError"
      />
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" autocomplete="off" placeholder="账户" />
      </a-form-model-item>
      <a-form-model-item has-feedback prop="password">
        <a-input-password v-model="form.password" size="large" autocomplete="off" placeholder="密码" :maxLength="20" />
      </a-form-model-item>
      <a-form-model-item has-feedback prop="confirmPassword">
        <a-input-password v-model="form.confirmPassword" size="large" autocomplete="off" placeholder="确认密码" :maxLength="20" />
      </a-form-model-item>
      <a-row :gutter="16" v-if="captchaEnabled">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="code">
            <a-input v-model="form.code" size="large" type="text" autocomplete="off" placeholder="验证码">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="codeUrl" @click="getCode">
        </a-col>
      </a-row>
      <a-form-item>
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="register-button"
          :loading="registering"
          @click.stop.prevent="handleRegister"
          :disabled="registering"
        >
          注册
        </a-button>
        <router-link class="login" :to="{ name: 'login' }">使用已有账户登录</router-link>
      </a-form-item>
    </a-form-model>
  </div>
</template>

<script>
import { getCodeImg, register } from '@/api/login'
export default {
  name: 'Register',
  components: {},
  data () {
    const validateNewPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (!/^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$)([^\u4e00-\u9fa5\s]){5,20}$/.test(value)) {
        callback(new Error('请输入5-20位英文字母、数字或者符号（除空格），且字母、数字和标点符号至少包含两种'))
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码确认'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      codeUrl: '',
      isRegisterError: false,
      registerErrorInfo: '',
      form: {
        username: undefined,
        password: undefined,
        confirmPassword: undefined,
        code: undefined,
        uuid: undefined
      },
      rules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' },
          { required: true, validator: validateNewPass, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请再次输入您的密码' },
          { required: true, validator: validateConfirmPass, trigger: 'blur' }
        ],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
      },
      registerBtn: false,
      registering: false,
      captchaEnabled: true
    }
  },
  computed: {},
  created () {
    this.getCode()
  },
  methods: {
    getCode () {
      getCodeImg().then((res) => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.form.uuid = res.uuid
        }
      })
    },
    handleRegister () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.registering = true
          register(this.form)
            .then(res => this.registerSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.registering = false
            })
        } else {
          setTimeout(() => {
            this.registering = false
          }, 600)
        }
      })
    },
    registerSuccess (res) {
      this.$router.push({ path: '/login' })
      // 延迟 1 秒显示欢迎信息
      const username = this.form.username
      setTimeout(() => {
        this.$notification.success({
          message: '恭喜你，您的账号 ' + username + ' 注册成功！'
        })
      }, 1000)
      this.handleCloseRegisterError()
    },
    requestFailed (err) {
      this.isRegisterError = true
      this.registerErrorInfo = err
      this.form.code = undefined
      if (this.captchaEnabled) {
        this.getCode()
      }
    },
    handleCloseRegisterError () {
      this.isRegisterError = false
      this.registerErrorInfo = ''
    }
  }
}
</script>
<style lang="less">
.user-register {
  &.error {
    color: #ff0000;
  }
  &.warning {
    color: #ff7e05;
  }
  &.success {
    color: #52c41a;
  }
}
.user-layout-register {
  .ant-input-group-addon:first-child {
    background-color: #fff;
  }
}
</style>
<style lang="less" scoped>
.user-layout-register {
  & > h3 {
    font-size: 16px;
    margin-bottom: 20px;
  }
  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }
  .register-button {
    width: 50%;
  }
  .login {
    float: right;
    line-height: 40px;
  }
}
</style>

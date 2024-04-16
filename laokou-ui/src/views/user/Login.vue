<template>
  <div class="main">
    <a-form-model id="formLogin" ref="form" class="user-layout-login" :model="form" :rules="rules">
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" allow-clear autocomplete="new-password" placeholder="账号" >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password">
        <a-input-password v-model="form.password" allow-clear autocomplete="new-password" size="large" placeholder="密码">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input-password>
      </a-form-model-item>
      <a-row :gutter="16" v-if="captchaEnabled">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="code">
            <a-input v-model="form.code" size="large" allow-clear type="text" autocomplete="off" placeholder="验证码">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="codeUrl" @click="getCode" alt="暂无验证码">
        </a-col>
      </a-row>
      <a-form-item>
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="loginIng"
          :disabled="loginIng"
          @click="handleSubmit"
        >确定</a-button>
      </a-form-item>
    </a-form-model>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import storage from 'store'
import { v4 as uid } from 'uuid'
import { JSEncrypt } from 'jsencrypt'

export default {
  components: {
  },
  data () {
    return {
      publicKey: '',
      tenantOptions: [],
      isLoginError: false,
      loginErrorInfo: '',
      form: {
        username: '',
        password: '',
        captcha: '',
        uuid: '',
      },
      rules: {
        username: [{ required: true, message: '请输入帐号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      loginIng: false,
      captchaEnabled: true
    }
  },
  created () {
    this.getVerifyCode()
  },
  methods: {
    getVerifyCode () {
      // getCodeImg().then(res => {
      //   this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
      //   if (this.captchaEnabled) {
      //     this.codeUrl = 'data:image/gif;base64,' + res.img
      //     this.form.uuid = res.uuid
      //   }
      // })
    },
    ...mapActions(['Login', 'Logout']),
    handleSubmit () {
      this.loginIng = true
      this.$refs.form.validate(valid => {
        if (valid) {
          this.Login(this.form)
            .then((res) => this.loginSuccess())
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.loginIng = false
            })
        } else {
          setTimeout(() => {
            this.loginIng = false
          }, 600)
        }
      })
    },
    loginSuccess () {
      this.$router.push({ path: '/' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
      this.handleCloseLoginError()
    },
    requestFailed (err) {
      this.form.captcha = ''
      if (this.captchaEnabled) {
        this.getCode()
      }
    },
  }
}
</script>

<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }
  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;
    .register {
      float: right;
    }
  }
}
</style>

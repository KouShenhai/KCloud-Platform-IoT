<template>
  <div class="main">
    <a-form-model id="formLogin" ref="form" class="user-layout-login" :model="form" :rules="rules">
      <a-alert
        v-if="isLoginError"
        type="error"
        showIcon
        style="margin-bottom: 24px;"
        :message="loginErrorInfo"
        closable
        :after-close="handleCloseLoginError"
      />
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" placeholder="账户: admin" >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password">
        <a-input-password v-model="form.password" size="large" placeholder="密码: admin123">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input-password>
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
      <a-form-model-item prop="rememberMe">
        <a-checkbox :checked="form.rememberMe" @change="rememberMe">记住密码</a-checkbox>
      </a-form-model-item>
      <a-form-item style="margin-top:24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="logining"
          :disabled="logining"
          @click="handleSubmit"
        >确定</a-button>
      </a-form-item>
      <div class="user-login-other">
        <!--
          ruoyi后台不支持获取是否开启账户.
          故此处不做隐藏处理. 在ruoyi原前端中是在注册页面定义一个属性手动修改处理.
        -->
        <router-link class="register" :to="{ name: 'register' }">注册账户</router-link>
      </div>
    </a-form-model>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import { getCodeImg } from '@/api/login'
import { LOGIN_USERNAME, LOGIN_PASSWORD, LOGIN_REMEMBERME } from '@/store/mutation-types'
import storage from 'store'

export default {
  components: {
  },
  data () {
    return {
      codeUrl: '',
      isLoginError: false,
      loginErrorInfo: '',
      form: {
        username: 'admin',
        password: 'admin123',
        code: undefined,
        uuid: '',
        rememberMe: false
      },
      rules: {
        username: [{ required: true, message: '请输入帐户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      logining: false,
      captchaEnabled: true
    }
  },
  created () {
    this.getStorage()
    this.getCode()
  },
  methods: {
    getCode () {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.form.uuid = res.uuid
        }
      })
    },
    getStorage () {
      const username = storage.get(LOGIN_USERNAME)
      const password = storage.get(LOGIN_PASSWORD)
      const rememberMe = storage.get(LOGIN_REMEMBERME)
      if (username) {
        this.form = {
          username: username,
          password: password,
          rememberMe: rememberMe
        }
      }
    },
    rememberMe (e) {
      this.form.rememberMe = e.target.checked
    },
    ...mapActions(['Login', 'Logout']),
    handleSubmit () {
      this.logining = true
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.rememberMe) {
            storage.set(LOGIN_USERNAME, this.form.username)
            storage.set(LOGIN_PASSWORD, this.form.password)
            storage.set(LOGIN_REMEMBERME, this.form.rememberMe)
          } else {
            storage.remove(LOGIN_USERNAME)
            storage.remove(LOGIN_PASSWORD)
            storage.remove(LOGIN_REMEMBERME)
          }
          this.Login(this.form)
            .then((res) => this.loginSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.logining = false
            })
        } else {
          setTimeout(() => {
            this.logining = false
          }, 600)
        }
      })
    },
    loginSuccess (res) {
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
      this.isLoginError = true
      this.loginErrorInfo = err
      this.form.code = undefined
      if (this.captchaEnabled) {
        this.getCode()
      }
    },
    handleCloseLoginError () {
      this.isLoginError = false
      this.loginErrorInfo = ''
    }
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

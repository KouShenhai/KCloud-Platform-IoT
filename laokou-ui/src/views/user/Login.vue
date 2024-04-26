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
      <a-row :gutter="16">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="captcha">
            <a-input
              v-model="form.captcha"
              size="large"
              allow-clear
              type="text"
              autocomplete="off"
              placeholder="验证码">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="verifyCodeUrl" @click="getVerifyCode" alt="暂无验证码">
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
import { v4 as uid } from 'uuid'
import { JSEncrypt } from 'jsencrypt'
import { getCaptcha, getSecret } from '@/api/login'

export default {
  components: {
  },
  data () {
    return {
      publicKey: '',
      tenantOptions: [],
      verifyCodeUrl: '',
      form: {
        username: '',
        password: '',
        captcha: '',
        uuid: '',
        tenantId: 0
      },
      rules: {
        username: [{ required: true, message: '请输入帐号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      loginIng: false
    }
  },
  created () {
    this.getPublicKey()
    this.getVerifyCode()
  },
  methods: {
    getPublicKey () {
      getSecret().then(res => {
        this.publicKey = res.data.publicKey
      })
    },
    getVerifyCode () {
      this.form.uuid = uid()
      getCaptcha(this.form.uuid).then(res => {
        this.verifyCodeUrl = res.data
      })
    },
    ...mapActions(['Login', 'Logout']),
    handleSubmit () {
      this.loginIng = true
      this.$refs.form.validate(valid => {
        if (valid) {
          this.Login(this.getParams())
            .then(() => this.loginSuccess())
            .catch(() => this.requestFailed())
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
    },
    // eslint-disable-next-line handle-callback-err
    requestFailed () {
      this.form.captcha = ''
      this.getVerifyCode()
    },
    getParams () {
      const encrypt = new JSEncrypt()
      encrypt.setPublicKey(this.publicKey)
      return {
        username: encodeURIComponent(encrypt.encrypt(this.form.username)),
        password: encodeURIComponent(encrypt.encrypt(this.form.password)),
        captcha: this.form.captcha,
        uuid: this.form.uuid,
        grant_type: 'password',
        tenant_id: this.form.tenantId
      }
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

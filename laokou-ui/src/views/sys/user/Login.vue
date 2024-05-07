<template>
  <div class="main">
    <a-form-model id="formLogin" ref="form" class="user-layout-login" :model="form" :rules="rules">
      <a-form-model-item prop="tenantId">
        <a-select
          size="large"
          v-model="form.tenantId"
          :placeholder="$t('user.login.tenant')">
          <a-select-option key="0" value="0">{{ $t('user.login.tenant.name.default') }}</a-select-option>
          <a-select-option v-for="(d, index) in tenantOptions" :key="index + 1" :value="d.value">
            {{ d.label }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" allow-clear autocomplete="new-password" :placeholder="$t('user.login.username')" >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password">
        <a-input-password v-model="form.password" allow-clear autocomplete="new-password" size="large" :placeholder="$t('user.login.password')">
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
              :placeholder="$t('user.login.captcha')">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="verifyCodeUrl" @click="getVerifyCode" alt="Not found">
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
    <div>
      <a-table
        bordered
        :size="tableSize"
        :columns="columns"
        rowKey="id"
        :data-source="list"
        :pagination="false" />
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import { v4 as uid } from 'uuid'
import { JSEncrypt } from 'jsencrypt'
import { tableMixin } from '@/store/table-mixin'

export default {
  components: {
  },
  mixins: [tableMixin],
  data () {
    return {
      publicKey: '',
      tenantOptions: [],
      verifyCodeUrl: '',
      list: [
        {
          id: 1,
          tenant: '老寇云集团',
          username: 'admin',
          password: 'admin123'
        },
        {
          id: 2,
          tenant: '老寇云集团',
          username: 'test',
          password: 'test123'
        },
        {
          id: 3,
          tenant: '老寇云集团',
          username: 'laok5',
          password: 'test123'
        },
        {
          id: 4,
          tenant: '阿里云集团',
          username: 'tenant',
          password: 'tenant123'
        }
      ],
      columns: [
        {
          title: '序号',
          dataIndex: 'id',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '租户',
          dataIndex: 'tenant',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '用户名',
          dataIndex: 'username',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '密码',
          dataIndex: 'password',
          ellipsis: true,
          align: 'center'
        }
      ],
      form: {
        username: '',
        password: '',
        captcha: '',
        uuid: '',
        tenantId: '0'
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
    this.getTenantIdByDomainName()
  },
  methods: {
    ...mapActions(['Login', 'GetSecret', 'GetCaptcha', 'ListTenantOption', 'GetTenantIdByDomainName']),
    async getPublicKey () {
      this.GetSecret().then(res => {
        this.publicKey = res.data.publicKey
      })
    },
    async getVerifyCode () {
      this.form.uuid = uid()
      this.GetCaptcha(this.form.uuid).then(res => {
        this.verifyCodeUrl = res.data
      })
    },
    async getTenantIdByDomainName () {
      this.tenantOptions = await this.ListTenantOption()
      this.form.tenantId = await this.GetTenantIdByDomainName()
    },
    async handleSubmit () {
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
/* 表格斑马样式 **/
/deep/.ant-table-tbody tr:nth-child(2n)
{
  background-color:#fafafa;
}
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

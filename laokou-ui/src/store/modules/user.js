import storage from 'store'
import { login, getInfo, logout } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    id: '',
    token: '',
    avatar: '',
    username: '',
    tenantId: '',
    permissions: []
  },

  mutations: {
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_TENANT_ID: (state, tenantId) => {
      state.tenantId = tenantId
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USERNAME: (state, username) => {
      state.username = username
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login ({ commit }, params) {
      return new Promise((resolve, reject) => {
        login(params).then(res => {
          storage.set(ACCESS_TOKEN, res.access_token, 60 * 60 * 1000)
          commit('SET_TOKEN', res.access_token)
          resolve()
        })
        .catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetInfo ({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(res => {
          const user = res.user
          const avatar = user.avatar === '' ? require('@/assets/images/profile.jpg') : process.env.VUE_APP_BASE_API + user.avatar
          commit('SET_USERNAME', user.nickName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(error => {
          reject(error)
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_PERMISSIONS', [])
          commit('SET_USERNAME', '')
          commit('SET_AVATAR', '')
          storage.remove(ACCESS_TOKEN)
        })
      })
    }
  }
}

export default user

import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import tagsView from './modules/tagsView'
import dict from './modules/dict'

// default router permission control
// import permission from './modules/permission'
import permission from './modules/async-router'

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    dict,
    permission
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})

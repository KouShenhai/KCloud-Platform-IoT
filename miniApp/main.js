import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import uviewPlus, { setConfig } from 'uview-plus'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
app.use(store)
	.use(i18n)
	.use(uviewPlus, () => {
		return {
			options: {
				// 修改$u.config对象的属性
				config: {
					// 修改默认单位为rpx，相当于执行 uni.$u.config.unit = 'rpx'
					unit: 'rpx'
				}
			}
		}
	})
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif
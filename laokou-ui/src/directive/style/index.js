import highlight from './highlight'

const install = function (Vue) {
  Vue.directive('highlight', highlight)
}

if (window.Vue) {
  window['highlight'] = highlight
  Vue.use(install); // eslint-disable-line
}

export default install

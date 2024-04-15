import hljs from 'highlight.js'
import 'highlight.js/styles/darcula.css' // 代码高亮风格，选择更多风格需导入 node_modules/hightlight.js/styles/ 目录下其它css文件

export default {
    inserted: function (el) {
        const blocks = el.querySelectorAll('pre code')
        for (let i = 0; i < blocks.length; i++) {
            hljs.highlightBlock(blocks[i])
        }
    },
    // 指令所在组件的 VNode 及其子 VNode 全部更新后调用
    componentUpdated: function (el) {
        const blocks = el.querySelectorAll('pre code')
        for (let i = 0; i < blocks.length; i++) {
            hljs.highlightBlock(blocks[i])
        }
    }
}

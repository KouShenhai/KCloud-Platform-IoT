<script>
import events from './events'
import { i18nRender } from '@/locales'

// 首页
const homeRoute = {
  fullPath: '/index',
  path: '/index',
  meta: {
    title: '首页'
  }
}
export default {
  name: 'MultiTab',
  data () {
    return {
      pages: [],
      activeIndex: 0
    }
  },
  created () {
    // bind event
    events.$on('open', val => {
      if (!val) {
        throw new Error(`multi-tab: open tab ${val} err`)
      }
      this.activeIndex = val
    }).$on('close', val => {
      if (!val) {
        this.closeThat(this.activeIndex)
        return
      }
      this.closeThat(val)
    }).$on('rename', ({ key, name }) => {
      try {
        const item = this.pages.find(item => item.path === key)
        item.meta.customTitle = name
        this.$forceUpdate()
      } catch (e) {
      }
    })
    if (homeRoute.path !== this.$route.path) {
      this.pages.push(homeRoute)
    }
    this.pages.push(this.$route)
    this.selectedLastPath()
  },
  methods: {
    onEdit (targetKey, action) {
      this[action](targetKey)
    },
    remove (targetIndex) {
      this.$store.dispatch('tagsView/delCachedView', this.pages[targetIndex])
      this.pages.splice(targetIndex, 1)
      // 判断当前标签是否关闭，若关闭则跳转到最后一个还存在的标签页
      if (this.activeIndex === targetIndex) {
        this.selectedLastPath()
      } else if (this.activeIndex > targetIndex) {
        this.activeIndex--
      }
    },
    selectedLastPath () {
      this.activeIndex = this.pages.length - 1
      const page = this.pages[this.activeIndex]
      if (page.name) {
        this.$store.dispatch('tagsView/addCachedView', page)
      }
      this.$router.push({ path: page.path, query: page.query, params: page.params })
    },

    // content menu
    closeThat (e) {
      // 判断是否为最后一个标签页，如果是最后一个，则无法被关闭
      if (this.pages.length > 1) {
        this.remove(e)
      } else {
        this.$message.info('这是最后一个标签了, 无法被关闭')
      }
    },
    closeLeft (e) {
      if (e > 1) {
        for (let index = 1; index < e; index++) {
          this.remove(1)
        }
      } else {
        this.$message.info('左侧没有标签')
      }
    },
    closeRight (e) {
      if (e < (this.pages.length - 1)) {
        while (e < this.pages.length - 1) {
          this.remove(e + 1)
        }
      } else {
        this.$message.info('右侧没有标签')
      }
    },
    closeAll (e) {
      while (this.pages.length > 1) {
        this.remove(1)
      }
    },
    closeMenuClick (key, route) {
      this[key](route)
    },
    renderTabPaneMenu (e, index) {
      return (
        <a-menu {...{ on: { click: ({ key, item, domEvent }) => { this.closeMenuClick(key, index) } } }}>
          <a-menu-item disabled={index === 0} key="closeThat">关闭当前标签</a-menu-item>
          <a-menu-item disabled={this.pages.length === index + 1} key="closeRight">关闭右侧</a-menu-item>
          <a-menu-item disabled={index <= 1} key="closeLeft">关闭左侧</a-menu-item>
          <a-menu-item disabled={this.pages.length === 1} key="closeAll">关闭全部</a-menu-item>
        </a-menu>
      )
    },
    // render
    renderTabPane (title, keyPath, index) {
      const menu = this.renderTabPaneMenu(keyPath, index)

      return (
        <a-dropdown overlay={menu} trigger={['contextmenu']}>
          <span style={{ userSelect: 'none' }}>{ i18nRender(title) } </span>
        </a-dropdown>
      )
    }
  },
  watch: {
    '$route': function (newVal) {
      let currentIndex = this.pages.findIndex((item) => item.path === newVal.path)
      if (currentIndex < 0) {
        this.pages.push(newVal)
        currentIndex = this.pages.length - 1
      } else {
        const page = this.pages[currentIndex]
        // 判断fullPath及params，因fullPath会包含query，无需判断query
        if (page.fullPath !== newVal.fullPath || JSON.stringify(page.params) !== JSON.stringify(newVal.params)) {
          this.pages.splice(currentIndex, 1, newVal)
        }
      }
      this.activeIndex = currentIndex
    },
    activeIndex: function (index) {
      const page = this.pages[index]
      if (page.name) {
        this.$store.dispatch('tagsView/addCachedView', page)
      }
      this.$router.push({ path: page.path, query: page.query, params: page.params })
    }
  },
  render () {
    const { onEdit, $data: { pages } } = this
    const panes = pages.map((page, index) => {
      return (
        <a-tab-pane
          style={{ height: 0 }}
          tab={this.renderTabPane(page.meta.customTitle || page.meta.title, page.path, index)}
          key={index} closable={page.path !== homeRoute.path}
        >
        </a-tab-pane>)
    })

    return (
      <div class="ant-pro-multi-tab">
        <div class="ant-pro-multi-tab-wrapper">
          <a-tabs
            hideAdd
            type={'editable-card'}
            v-model={this.activeIndex}
            tabBarStyle={{ background: '#FFF', margin: 0, paddingLeft: '16px', paddingTop: '1px' }}
            {...{ on: { edit: onEdit } }}>
            {panes}
          </a-tabs>
        </div>
      </div>
    )
  }
}
</script>

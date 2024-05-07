<template>
  <div class="setting-drawer">
    <a-drawer
      width="300"
      placement="right"
      @close="onClose"
      :closable="false"
      :visible="visible"
      style="z-index: 999"
    >
      <div class="setting-drawer-index-content">

        <setting-item title="整体风格设置">
          <div class="setting-drawer-index-blockChecbox">
            <a-tooltip>
              <template slot="title">
                暗色菜单风格
              </template>
              <div class="setting-drawer-index-blockChecbox-item" @click="handleChange('theme', 'dark')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/LCkqqYNmvBEbokSDscrm.svg" alt="dark">
                <div class="setting-drawer-index-selectIcon" v-if="navTheme === 'dark'">
                  <a-icon type="check"/>
                </div>
              </div>
            </a-tooltip>

            <a-tooltip>
              <template slot="title">
                亮色菜单风格
              </template>
              <div class="setting-drawer-index-blockChecbox-item" @click="handleChange('theme', 'light')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/jpRkZQMyYRryryPNtyIC.svg" alt="light">
                <div class="setting-drawer-index-selectIcon" v-if="navTheme !== 'dark'">
                  <a-icon type="check"/>
                </div>
              </div>
            </a-tooltip>
          </div>
        </setting-item>

        <setting-item title="主题色" divider>
          <div style="height: 20px">
            <a-tooltip class="setting-drawer-theme-color-colorBlock" v-for="(item, index) in colorList" :key="index">
              <template slot="title">
                {{ item.key }}
              </template>
              <a-tag :color="item.color" @click="handleChange('primaryColor', item.color)">
                <a-icon type="check" v-if="item.color === primaryColor"></a-icon>
              </a-tag>
            </a-tooltip>

          </div>
        </setting-item>

        <setting-item title="导航模式" divider>
          <div class="setting-drawer-index-blockChecbox">
            <a-tooltip>
              <template slot="title">
                侧边栏导航
              </template>
              <div class="setting-drawer-index-blockChecbox-item" @click="handleChange('layout', 'sidemenu')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/JopDzEhOqwOjeNTXkoje.svg" alt="sidemenu">
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'sidemenu'">
                  <a-icon type="check"/>
                </div>
              </div>
            </a-tooltip>

            <a-tooltip>
              <template slot="title">
                顶部栏导航
              </template>
              <div class="setting-drawer-index-blockChecbox-item" @click="handleChange('layout', 'topmenu')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/KDNDBbriJhLwuqMoxcAr.svg" alt="topmenu">
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'topmenu'">
                  <a-icon type="check"/>
                </div>
              </div>
            </a-tooltip>
          </div>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>内容区域宽度</span>
                <a-tooltip slot="actions">
                  <template slot="title">
                    该设定仅 [顶部栏导航] 时有效
                  </template>
                  <a-select size="small" style="width: 80px;" v-model="contentWidth" @change="(value) => handleChange('contentWidth', value)">
                    <a-select-option value="Fixed" v-if="layout === 'topmenu'">固定</a-select-option>
                    <a-select-option value="Fluid">流式</a-select-option>
                  </a-select>
                </a-tooltip>
              </a-list-item>
              <a-list-item>
                <span>固定 Header</span>
                <a-switch slot="actions" size="small" :checked="fixedHeader" @change="(checked) => handleChange('fixedHeader', checked)" />
              </a-list-item>
              <a-list-item >
                <span :style="{ opacity: layout === 'topmenu' ? 0.5 : 1 }">固定侧边菜单</span>
                <a-switch slot="actions" size="small" :disabled="(layout === 'topmenu')" :checked="fixedSidebar" @change="(checked) => handleChange('fixSiderbar', checked)" />
              </a-list-item>
              <a-list-item>
                <span>隐藏 Footer</span>
                <a-switch slot="actions" size="small" :defaultChecked="hideFooter" @change="(checked) => handleChange('hideFooter', checked)" />
              </a-list-item>
              <a-list-item>
                <span>多页签模式</span>
                <a-switch slot="actions" size="small" :defaultChecked="multiTab" @change="(checked) => handleChange('multiTab', checked)" />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <setting-item title="表格通用样式" divider>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>表格大小</span>
                <a-radio-group slot="actions" :default-value="tableSize" size="small" button-style="solid" @change="(e) => handleChange('tableSize', e.target.value)">
                  <a-radio-button value="default">
                    默认
                  </a-radio-button>
                  <a-radio-button value="middle">
                    中等
                  </a-radio-button>
                  <a-radio-button value="small">
                    紧凑
                  </a-radio-button>
                </a-radio-group>
              </a-list-item>
              <a-list-item>
                <span>显示边框</span>
                <a-switch slot="actions" size="small" :checked="tableBordered" @change="(checked) => handleChange('tableBordered', checked)" />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <setting-item title="其他设置" divider>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>色弱模式</span>
                <a-switch slot="actions" size="small" :checked="colorWeak" @change="(checked) => handleChange('colorWeak', checked)" />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <a-button
          @click="doCopy"
          icon="copy"
          block
        >拷贝设置</a-button>
      </div>
      <div class="setting-drawer-index-handle" @click="toggle" slot="handle">
        <a-icon type="setting" v-if="!visible"/>
        <a-icon type="close" v-else/>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import SettingItem from './SettingItem'
import { updateTheme, updateColorWeak, colorList } from './settingConfig'
import { baseMixin } from '@/store/app-mixin'
import { tableMixin } from '@/store/table-mixin'

export default {
  components: {
    SettingItem
  },
  mixins: [baseMixin, tableMixin],
  data () {
    return {
      visible: false,
      colorList
    }
  },
  watch: {
  },
  mounted () {
  },
  methods: {
    handleChange (type, value) {
      if (type === 'primaryColor') {
        // 更新主色调
        updateTheme(value)
      }
      if (type === 'colorWeak') {
        updateColorWeak(value)
      }
      this.$emit('change', { type, value })
    },
    showDrawer () {
      this.visible = true
    },
    onClose () {
      this.visible = false
    },
    toggle () {
      this.visible = !this.visible
    },
    doCopy () {
      // get current settings from mixin or this.$store.state.app, pay attention to the property name
      const text = `export default {
  navTheme: '${this.navTheme}', // theme for nav menu
  primaryColor: '${this.primaryColor}', // primary color of ant design
  layout: '${this.layout}', // nav menu position: sidemenu or topmenu
  contentWidth: '${this.contentWidth}', // layout of content: Fluid or Fixed, only works when layout is topmenu
  fixedHeader: ${this.fixedHeader}, // sticky header
  fixSiderbar: ${this.fixedSidebar}, // sticky siderbar
  colorWeak: ${this.colorWeak},
  multiTab: ${this.multiTab},
  tableSize: '${this.tableSize}',
  tableBordered: ${this.tableBordered},
  hideFooter: ${this.hideFooter},
  title: '老寇IoT云平台',
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
}`
      this.$copyText(text).then(message => {
        console.log('copy', message)
        this.$message.success('复制完毕')
      }).catch(err => {
        console.log('copy.err', err)
        this.$message.error('复制失败')
      })
    }
  }
}
</script>

<style lang="less" scoped>

  .setting-drawer-index-content {
    position: relative;
    min-height: 100%;

    .setting-drawer-index-blockChecbox {
      display: flex;

      .setting-drawer-index-blockChecbox-item {
        margin-right: 16px;
        position: relative;
        border-radius: 4px;
        cursor: pointer;

        img {
          width: 48px;
        }

        .setting-drawer-index-selectIcon {
          position: absolute;
          top: 0;
          right: 0;
          width: 100%;
          padding-top: 15px;
          padding-left: 24px;
          height: 100%;
          color: #1890ff;
          font-size: 14px;
          font-weight: 700;
        }
      }
    }
    .setting-drawer-theme-color-colorBlock {
      width: 20px;
      height: 20px;
      border-radius: 2px;
      float: left;
      cursor: pointer;
      margin-right: 8px;
      padding-left: 0px;
      padding-right: 0px;
      text-align: center;
      color: #fff;
      font-weight: 700;

      i {
        font-size: 14px;
      }
    }
  }

  .setting-drawer-index-handle {
    position: absolute;
    top: 240px;
    background: #1890ff;
    width: 48px;
    height: 48px;
    right: 300px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    pointer-events: auto;
    z-index: 1001;
    text-align: center;
    font-size: 16px;
    border-radius: 4px 0 0 4px;

    i {
      color: rgb(255, 255, 255);
      font-size: 20px;
    }
  }
</style>

<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="account-settings-info-main" :class="{ 'mobile': isMobile }">
        <div class="account-settings-info-left">
          <a-menu
            :mode="isMobile ? 'horizontal' : 'inline'"
            v-model="currentKey"
            :style="{ border: '0', width: isMobile ? '560px' : 'auto'}"
            type="inner"
          >
            <a-menu-item key="base">
              基本设置
            </a-menu-item>
            <a-menu-item key="security">
              安全设置
            </a-menu-item>
          </a-menu>
        </div>
        <div class="account-settings-info-right">
          <div class="account-settings-info-title">
            <span>{{ currentKey.indexOf('base') > -1 ? '基本设置' : '安全设置' }}</span>
          </div>
          <base-setting v-if="currentKey.indexOf('base') > -1" />
          <security v-if="currentKey.indexOf('security') > -1" />
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
import { baseMixin } from '@/store/app-mixin'
import Security from './Security'
import BaseSetting from './BaseSetting'

export default {
  name: 'Settings',
  components: {
    Security,
    BaseSetting
  },
  mixins: [baseMixin],
  data () {
    return {
      // horizontal  inline
      mode: 'inline',
      currentKey: ['base']
    }
  },
  mounted () {
  },
  methods: {
  },
  watch: {
  }
}
</script>

<style lang="less" scoped>
  .account-settings-info-main {
    width: 100%;
    display: flex;
    height: 100%;
    overflow: auto;

    &.mobile {
      display: block;

      .account-settings-info-left {
        border-right: unset;
        border-bottom: 1px solid #e8e8e8;
        width: 100%;
        height: 50px;
        overflow-x: auto;
        overflow-y: scroll;
      }
      .account-settings-info-right {
        padding: 20px 40px;
      }
    }

    .account-settings-info-left {
      border-right: 1px solid #e8e8e8;
      width: 224px;
    }

    .account-settings-info-right {
      flex: 1 1;
      padding: 8px 40px;

      .account-settings-info-title {
        color: rgba(0,0,0,.85);
        font-size: 20px;
        font-weight: 500;
        line-height: 28px;
        margin-bottom: 12px;
      }
      .account-settings-info-view {
        padding-top: 12px;
      }
    }
  }

</style>

<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <div class="container">
      <div class="top">
        <div class="header">
          <a href="/">
            <img src="~@/assets/logo.png" class="logo" alt="logo">
            <span class="title">RuoYi-Antdv</span>
          </a>
        </div>
        <div class="desc">
          基于SpringBoot，Spring Security，JWT，Vue 的前后端分离权限管理系统
        </div>
      </div>

      <router-view />

      <div class="footer">
        <div class="links">
          <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">帮助</a>
          <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">隐私</a>
          <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">条款</a>
        </div>
        <div class="copyright">
          Copyright &copy; 2022 <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">RuoYi-Antdv</a><br/>
        </div>
        <a-space size="large">
          <a href="https://beian.miit.gov.cn/" target="_blank">
            <img src="https://cdn.fuzui.net/icon/beian.png?x-oss-process=style/ys" style="width:18px">
            宁ICP备 2020001732号-1
          </a>
          <a href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11011202003445" target="_blank">
            <img src="https://oss.fuzui.net/img/202202010105048.png" style="width:18px">
            京公网安备 11011202003445号
          </a>
        </a-space>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'

export default {
  name: 'UserLayout',
  mixins: [deviceMixin],
  mounted () {
    document.body.classList.add('userLayout')
  },
  beforeDestroy () {
    document.body.classList.remove('userLayout')
  }
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
      background-size: 100%;
      padding: 110px 0 144px;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 260px;
        width: 368px;
        margin: 0 auto;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }
  }
</style>

<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">帮助</a>
      <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">隐私</a>
      <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">条款</a>
    </template>
    <template v-slot:copyright>
      Copyright &copy; 2022 <a href="https://github.com/fuzui/RuoYi-Antdv" target="_blank">RuoYi-Antdv</a><br/>
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
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@/components/ProLayout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>

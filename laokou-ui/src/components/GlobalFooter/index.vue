<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="#">帮助</a>
      <a href="#">隐私</a>
      <a href="#">条款</a>
    </template>
    <template v-slot:copyright>
      Copyright &copy; 2022 <a href="https://github.com/KouShenhai/KCloud-Platform-IoT" target="_blank">KCloud-Platform-IoT</a><br/>
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

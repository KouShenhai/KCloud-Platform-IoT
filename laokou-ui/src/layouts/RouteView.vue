<template>
  <keep-alive :include="this.cachedViews">
    <router-view :key="key" />
  </keep-alive>
</template>
<script>
export default {
  name: 'RouteView',
  props: {
    keepAlive: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    cachedViews () {
      return this.$store.state.tagsView.cachedViews
    },
    key () {
      return this.$route.path
    }
  },
  data () {
    return {}
  }
}
</script>

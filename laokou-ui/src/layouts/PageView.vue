<template>
  <page-header-wrapper>
    <keep-alive :include="this.cachedViews">
      <router-view />
    </keep-alive>
  </page-header-wrapper>
</template>

<script>

export default {
  name: 'PageView',
  computed: {
    cachedViews () {
      return this.$store.state.tagsView.cachedViews
    }
  }
}
</script>

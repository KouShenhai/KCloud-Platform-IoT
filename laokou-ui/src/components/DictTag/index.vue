<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span
          v-if="item.raw.listClass == 'default' || item.raw.listClass == ''"
          :key="item.value"
          :index="index"
          :class="item.raw.cssClass"
        >
          {{ item.label }}
        </span>
        <a-badge
          v-else
          :key="item.value"
          :index="index"
          :status="item.raw.listClass == 'primary' || 'info' ? 'processing' : (tem.raw.listClass == 'danger' ? 'error' : item.raw.listClass)"
          :class="item.raw.cssClass"
          :text="item.label"
        >
        </a-badge>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: 'DictTag',
  props: {
    options: {
      type: Array,
      default: null
    },
    value: {
      type: [Number, String, Array],
      default: null
    }
  },
  computed: {
    values () {
      if (this.value !== null && typeof this.value !== 'undefined') {
        return Array.isArray(this.value) ? this.value : [String(this.value)]
      } else {
        return []
      }
    }
  }
}
</script>

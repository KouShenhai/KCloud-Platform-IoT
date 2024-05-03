<template>
  <div>
    <a-input-search style="margin-bottom: 8px" placeholder="Search" @change="filterNode" />
    <a-tree
      :expanded-keys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      :tree-data="deptOptions"
      :replaceFields="replaceFields"
      @select="handleNodeClick"
      @expand="onExpand"
    >
    </a-tree>
  </div>
</template>
<script>

const getParentKey = (id, tree) => {
  let parentKey
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i]
    if (node.children) {
      if (node.children.some(item => item.id === id)) {
        parentKey = node.id
      } else if (getParentKey(id, node.children)) {
        parentKey = getParentKey(id, node.children)
      }
    }
  }
  return parentKey
}
export default {
  name: 'DeptTree',
  props: {
    deptOptions: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      deptNodes: [],
      expandedKeys: [],
      searchValue: '',
      autoExpandParent: true
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  methods: {
    getAllDeptNode (nodes) {
      if (!nodes || nodes.length === 0) {
        return []
      }
      nodes.forEach(node => {
        this.deptNodes.push({ id: node.id, label: node.label })
        return this.getAllDeptNode(node.children)
      })
    },
    // 筛选节点
    filterNode (e) {
      this.getAllDeptNode(this.deptOptions)
      const value = e.target.value
      const gData = this.deptOptions
      const expandedKeys = this.deptNodes
        .map(item => {
          if (item.label.indexOf(value) > -1) {
            return getParentKey(item.id, gData)
          }
          return null
        })
        .filter((item, i, self) => item && self.indexOf(item) === i)
      Object.assign(this, {
        expandedKeys: expandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
      this.deptNodes = []
    },
    // 节点单击事件
    handleNodeClick (keys, event) {
      this.$emit('select', keys[0])
    },
    onExpand (expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    }
  }
}
</script>

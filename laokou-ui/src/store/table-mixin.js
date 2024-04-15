import { mapState } from 'vuex'

const tableMixin = {
  data () {
    return {
      tableSize1: '',
      lastName: ''
    }
  },
  computed: {
    ...mapState({
      tableBordered: state => state.app.tableBordered
    }),
    tableSize: {
      get () { return this.tableSize1 || this.$store.state.app.tableSize },
      set (value) {
        this.tableSize1 = value
      }
    }
  }
}

export {
  tableMixin
}

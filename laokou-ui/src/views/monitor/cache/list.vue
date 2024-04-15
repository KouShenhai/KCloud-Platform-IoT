<template>
  <page-header-wrapper>
    <a-space direction="vertical" style="width: 100%">
      <a-row :gutter="24">
        <a-col :span="8">
          <a-card :loading="loading" title="缓存列表" :bordered="false">
            <a-space slot="extra">
              <a-popconfirm
                ok-text="是"
                cancel-text="否"
                @confirm="handleClearCacheAll()"
              >
                <span slot="title">确认<b>清除全部缓存</b>吗?</span>
                <a>
                  <a-icon type="delete" />
                </a>
              </a-popconfirm>
              <a-divider type="vertical" />
              <a @click="refreshCacheNames()">
                <a-icon type="sync" />
              </a>
            </a-space>
            <a-table
              :loading="loading"
              size="middle"
              rowKey="cacheName"
              :columns="cacheColumns"
              :data-source="cacheNames"
              :customRow="customRow"
              :pagination="false"
              :bordered="false">
              <span slot="cacheName" slot-scope="text, record">
                {{ nameFormatter(record) }}
              </span>
              <span slot="operation" slot-scope="text, record">
                <a-popconfirm
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleClearCacheName(record)"
                >
                  <span slot="title">确认<b>删除</b>缓存: {{ record.cacheName }} 吗?</span>
                  <a><a-icon type="delete" /></a>
                </a-popconfirm>
              </span>
            </a-table>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card :loading="loading" title="键名列表" :bordered="false">
            <a slot="extra" @click="refreshCacheKeys()">
              <a-icon type="sync" />
            </a>
            <a-list :data-source="cacheKeys">
              <a-list-item slot="renderItem" slot-scope="item">
                <a-popconfirm
                  slot="actions"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleClearCacheKey(item)"
                >
                  <span slot="title">确认<b>删除</b>缓存: {{ item }} 吗?</span>
                  <a><a-icon type="delete" /></a>
                </a-popconfirm>
                <a-list-item-meta @click="handleCacheValue(item)">
                  <ellipsis slot="description" :length="35">
                    {{ keyFormatter(item) }}
                  </ellipsis>
                </a-list-item-meta>
              </a-list-item>
              <div slot="header">
                {{ nowCacheName }}
              </div>
            </a-list>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card :loading="loading" title="缓存内容" :bordered="false">
            <a-descriptions size="middle" style="word-break: break-all;" layout="vertical" :column="3" bordered>
              <a-descriptions-item label="缓存名称" :span="3">
                {{ cacheForm.cacheName }}
              </a-descriptions-item>
              <a-descriptions-item label="缓存键名" :span="3">
                {{ cacheForm.cacheKey }}
              </a-descriptions-item>
              <a-descriptions-item label="缓存内容" :span="3">
                {{ cacheForm.cacheValue }}
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
    </a-space>
  </page-header-wrapper>
</template>

<script>

import { listCacheName, listCacheKey, getCacheValue, clearCacheName, clearCacheKey, clearCacheAll } from '@/api/monitor/cache'
import { Ellipsis } from '@/components'

export default {
  name: 'CacheList',
  components: {
    Ellipsis
  },
  data () {
    return {
      loading: true,
      cacheColumns: [
        {
          title: '缓存名称',
          dataIndex: 'cacheName',
          scopedSlots: { customRender: 'cacheName' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '备注',
          dataIndex: 'remark',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '20%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      cacheSubColumns: [
        {
          title: '缓存键名',
          dataIndex: 'cacheKey',
          scopedSlots: { customRender: 'cacheKey' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      // 统计命令信息
      commandstats: null,
      // 使用内存
      usedmemory: null,
      // cache信息
      cache: [],
      cacheNames: [],
      cacheKeys: [],
      cacheForm: {},
      subLoading: false,
      nowCacheName: ''
    }
  },
  filters: {
  },
  created () {
    this.getCacheNames()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询缓存名称列表 */
    getCacheNames () {
      this.loading = true
      listCacheName().then(response => {
        this.cacheNames = response.data
        this.loading = false
      })
    },
    customRow (record, index) {
      const style = record.cacheName === this.nowCacheName ? {} : { opacity: 0.7 }
      return {
        on: {
          click: () => {
            this.getCacheKeys(record)
          }
        },
        style
      }
    },
    /** 刷新缓存名称列表 */
    refreshCacheNames () {
      this.getCacheNames()
      this.$message.success('刷新缓存列表成功', 3)
    },
    /** 清理指定名称缓存 */
    handleClearCacheName (row) {
      clearCacheName(row.cacheName).then(response => {
        this.$message.success('清理缓存名称[' + this.nowCacheName + ']成功', 3)
        this.getCacheKeys()
      })
    },
    /** 查询缓存键名列表 */
    getCacheKeys (row) {
      const cacheName = row !== undefined ? row.cacheName : this.nowCacheName
      if (cacheName === '') {
        return
      }
      this.subLoading = true
      listCacheKey(cacheName).then(response => {
        this.cacheKeys = response.data
        this.subLoading = false
        this.nowCacheName = cacheName
      })
    },
    /** 刷新缓存键名列表 */
    refreshCacheKeys () {
      this.getCacheKeys()
      this.$message.success('刷新键名列表成功', 3)
    },
    /** 清理指定键名缓存 */
    handleClearCacheKey (cacheKey) {
      clearCacheKey(cacheKey).then(response => {
        this.$message.success('清理缓存键名[' + cacheKey + ']成功', 3)
        this.getCacheKeys()
      })
    },
    /** 列表前缀去除 */
    nameFormatter (row) {
      return row.cacheName.replace(':', '')
    },
    /** 键名前缀去除 */
    keyFormatter (cacheKey) {
      return cacheKey.replace(this.nowCacheName, '')
    },
    /** 查询缓存内容详细 */
    handleCacheValue (cacheKey) {
      getCacheValue(this.nowCacheName, cacheKey).then(response => {
        this.cacheForm = response.data
      })
    },
    /** 清理全部缓存 */
    handleClearCacheAll () {
      clearCacheAll().then(response => {
        this.$message.success('清理全部缓存成功', 3)
      })
    }
  }
}
</script>

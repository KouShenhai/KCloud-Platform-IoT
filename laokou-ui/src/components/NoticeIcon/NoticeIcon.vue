<template>
  <div>
    <a-popover
      v-model="visible"
      trigger="click"
      placement="bottomLeft"
      overlayClassName="header-notice-wrapper"
      :getPopupContainer="() => $refs.noticeRef.parentElement"
      :autoAdjustOverflow="true"
      :arrowPointAtCenter="true"
      :overlayStyle="{ width: '300px', top: '50px' }"
    >
      <template slot="content">
        <a-spin :spinning="loading">
          <a-tabs v-model="queryParam.noticeType" @change="changeType">
            <a-tab-pane v-for="d in typeOptions" :key="d.dictValue" :tab="d.dictLabel">
              <a-list style="max-height: 300px; overflow:auto;">
                <div
                  v-if="showLoadingMore"
                  slot="loadMore"
                  :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
                >
                  <a-spin v-if="loadingMore" />
                  <a-button v-else @click="onLoadMore">
                    查看更多
                  </a-button>
                </div>
                <a-list-item v-for="(item, index) in list" :key="index">
                  <a-list-item-meta :description="item.createTime">
                    <a slot="title" @click="$refs.noticeDetail.getNotice(item.noticeId)">
                      <ellipsis :length="32" tooltip>{{ item.noticeTitle }}</ellipsis>
                    </a>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </template>
      <span @click="fetchNotice" class="header-notice" ref="noticeRef">
        <a-icon style="font-size: 20px;" type="bell" />
      </span>
    </a-popover>
    <notice-detail ref="noticeDetail" :typeOptions="typeOptions" />
  </div>
</template>

<script>
import { listNotice } from '@/api/system/notice'
import Ellipsis from '@/components/Ellipsis'
import NoticeDetail from './NoticeDetail'

export default {
  name: 'HeaderNotice',
  components: {
    Ellipsis,
    NoticeDetail
  },
  data () {
    return {
      loading: false,
      loadingMore: false,
      showLoadingMore: true,
      visible: false,
      queryParam: {
        pageNum: 1,
        pageSize: 5,
        status: 0,
        noticeType: '1'
      },
      list: [],
      typeOptions: []
    }
  },
  methods: {
    getList () {
      this.loading = true
      listNotice(this.queryParam).then(response => {
          this.list = this.list.concat(response.rows)
          this.total = response.total
          if (this.total <= this.queryParam.pageNum * this.queryParam.pageSize) {
            this.showLoadingMore = false
          } else {
            this.showLoadingMore = true
          }
          this.loading = false
        }
      )
    },
    fetchNotice () {
      this.resetQuery()
      if (!this.visible) {
        if (this.typeOptions.length === 0) {
          this.getDicts('sys_notice_type').then(response => {
            this.typeOptions = response.data
          })
        }
        this.getList()
      }
      this.visible = !this.visible
    },
    resetQuery () {
      this.queryParam = {
        pageNum: 1,
        pageSize: 5,
        status: 0,
        noticeType: '1'
      }
      this.list = []
    },
    changeType (key) {
      this.resetQuery()
      this.queryParam.noticeType = key
      this.getList()
    },
    onLoadMore () {
      this.loadingMore = true
      this.queryParam.pageNum++
      this.getList()
      this.loadingMore = false
    }
  }
}
</script>

<style lang="css">
  .header-notice-wrapper {
    top: 50px !important;
  }
</style>
<style lang="less" scoped>
  .header-notice{
    display: inline-block;
    transition: all 0.3s;

    span {
      vertical-align: initial;
    }
  }
</style>

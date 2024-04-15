<template>
  <page-header-wrapper>
    <a-space direction="vertical" style="width: 100%">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-card :loading="loading" title="CPU" :bordered="false">
            <a-row :gutter="24" v-if="server.cpu">
              <a-col :span="12">
                <a-space direction="vertical" size="large">
                  <a-statistic
                    title="核心数"
                    :value="server.cpu.cpuNum"
                    :value-style="{ color: '#3f8600' }"
                    style="margin-right: 50px"
                  >
                    <template #prefix>
                      <a-icon type="setting" />
                    </template>
                  </a-statistic>
                  <a-statistic
                    title="用户使用率"
                    :value="server.cpu.used"
                    :precision="2"
                    suffix="%"
                    :value-style="{ color: '#3f8600' }"
                    style="margin-right: 50px"
                  >
                    <template #prefix>
                      <a-icon type="team" />
                    </template>
                  </a-statistic>
                </a-space>
              </a-col>
              <a-col :span="12">
                <a-space direction="vertical" size="large">
                  <a-statistic
                    title="系统使用率"
                    :value="server.cpu.sys"
                    :precision="2"
                    suffix="%"
                    :value-style="{ color: '#3f8600' }"
                    style="margin-right: 50px"
                  >
                    <template #prefix>
                      <a-icon type="cloud-server" />
                    </template>
                  </a-statistic>
                  <a-statistic
                    title="当前空闲率"
                    :value="server.cpu.free"
                    :precision="2"
                    suffix="%"
                    :value-style="{ color: '#3f8600' }"
                    style="margin-right: 50px"
                  >
                    <template #prefix>
                      <a-icon type="inbox" />
                    </template>
                  </a-statistic>
                </a-space>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card :loading="loading" title="内存" :bordered="false">
            <a-table
              :loading="loading"
              size="small"
              rowKey="name"
              :columns="memColumns"
              :data-source="memData"
              :pagination="false">
              <span slot="mem" slot-scope="text, record">
                <div :style="{color: record.name == '使用率' && text > 80 ? 'red' : ''}">
                  <a-icon type="warning" style="color:#FFCC00" v-if="record.name == '使用率' && text > 80" />
                  {{ text }} &nbsp;
                  <code v-if="record.name == '使用率'">%</code>
                  <code v-if="record.name != '使用率'">G</code>
                </div>
              </span>
              <span slot="jvm" slot-scope="text, record">
                <div :style="{color: record.name == '使用率' && text > 80 ? 'red' : ''}">
                  <a-icon type="warning" style="color:#FFCC00" v-if="record.name == '使用率' && text > 80" />
                  {{ text }} &nbsp;
                  <code v-if="record.name == '使用率'">%</code>
                  <code v-if="record.name != '使用率'">M</code>
                </div>
              </span>
            </a-table>
          </a-card>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="24">
          <a-card :loading="loading" title="服务器信息" :bordered="false">
            <a-descriptions :column="2" v-if="server.sys">
              <a-descriptions-item label="服务器名称">
                {{ server.sys.computerName }}
              </a-descriptions-item>
              <a-descriptions-item label="操作系统">
                {{ server.sys.osName }}
              </a-descriptions-item>
              <a-descriptions-item label="服务器IP">
                {{ server.sys.computerIp }}
              </a-descriptions-item>
              <a-descriptions-item label="系统架构">
                {{ server.sys.osArch }}
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="24">
          <a-card :loading="loading" title="Java虚拟机信息" :bordered="false">
            <a-descriptions :column="2" v-if="server.jvm">
              <a-descriptions-item label="Java名称">
                {{ server.jvm.name }}
              </a-descriptions-item>
              <a-descriptions-item label="Java版本">
                {{ server.jvm.version }}
              </a-descriptions-item>
              <a-descriptions-item label="启动时间">
                {{ server.jvm.startTime }}
              </a-descriptions-item>
              <a-descriptions-item label="运行时长">
                {{ server.jvm.runTime }}
              </a-descriptions-item>
              <a-descriptions-item label="安装路径">
                {{ server.jvm.home }}
              </a-descriptions-item>
              <a-descriptions-item label="项目路径">
                {{ server.sys.userDir }}
              </a-descriptions-item>
              <a-descriptions-item label="运行参数">
                {{ server.jvm.inputArgs }}
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="24">
          <a-card :loading="loading" title="磁盘状态" :bordered="false">
            <a-table
              :loading="loading"
              :size="tableSize"
              rowKey="dirName"
              :columns="sysColumns"
              :data-source="sysData"
              :pagination="false"
              :bordered="tableBordered"
            >
              <span slot="total" slot-scope="text">
                {{ text }}
              </span>
              <span slot="free" slot-scope="text">
                {{ text }}
              </span>
              <span slot="used" slot-scope="text">
                {{ text }}
              </span>
              <span slot="usage" slot-scope="text">
                <div :style="{color: text > 80 ? 'red' : ''}">
                  <a-icon type="warning" style="color:#FFCC00" v-if="text > 80" />
                  {{ text }}<code>%</code>
                </div>
              </span>
            </a-table>
          </a-card>
        </a-col>
      </a-row>
    </a-space>
  </page-header-wrapper>
</template>

<script>

import { getServer } from '@/api/monitor/server'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Server',
  mixins: [tableMixin],
  data () {
    return {
      server: [],
      loading: true,
      memColumns: [
        {
          title: '属性',
          dataIndex: 'name'
        },
        {
          title: '内存',
          dataIndex: 'mem',
          scopedSlots: { customRender: 'mem' }
        },
        {
          title: 'JVM',
          dataIndex: 'jvm',
          scopedSlots: { customRender: 'jvm' }
        }
      ],
      memData: [],
      sysColumns: [
        {
          title: '盘符路径',
          dataIndex: 'dirName',
          ellipsis: true
        },
        {
          title: '文件系统',
          dataIndex: 'sysTypeName'
        },
        {
          title: '盘符类型',
          dataIndex: 'typeName',
          ellipsis: true
        },
        {
          title: '总大小',
          dataIndex: 'total',
          scopedSlots: { customRender: 'total' }
        },
        {
          title: '可用大小',
          dataIndex: 'free',
          scopedSlots: { customRender: 'free' }
        },
        {
          title: '已用大小',
          dataIndex: 'used',
          scopedSlots: { customRender: 'used' }
        },
        {
          title: '已用百分比',
          dataIndex: 'usage',
          scopedSlots: { customRender: 'usage' }
        }
      ],
      sysData: []
    }
  },
  filters: {
  },
  created () {
    this.getList()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询服务信息 */
    getList () {
      // this.loading = true
      getServer().then(response => {
          const serverData = response.data
          this.server = serverData
          this.memData = [
            {
              name: '总内存',
              mem: serverData.mem.total,
              jvm: serverData.jvm.total
            },
            {
              name: '已用内存',
              mem: serverData.mem.used,
              jvm: serverData.jvm.used
            },
            {
              name: '剩余内存',
              mem: serverData.mem.free,
              jvm: serverData.jvm.free
            },
            {
              name: '使用率',
              mem: serverData.mem.usage,
              jvm: serverData.jvm.usage
            }
          ]
          this.sysData = serverData.sysFiles
          setTimeout(() => {
            this.loading = false
          }, 500)
        }
      )
    }
  }
}
</script>

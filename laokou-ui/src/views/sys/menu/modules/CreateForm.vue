<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="上级菜单" prop="parentId">
        <a-tree-select
          v-model="form.parentId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="menuOptions"
          placeholder="请选择"
          :replaceFields="{children:'children', title:'menuName', key:'menuId', value: 'menuId' }"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item>
      <a-form-model-item label="菜单类型" prop="menuType">
        <a-radio-group v-model="form.menuType" button-style="solid">
          <a-radio-button value="M">目录</a-radio-button>
          <a-radio-button value="C">菜单</a-radio-button>
          <a-radio-button value="F">按钮</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="图标" prop="icon" v-if="form.menuType != 'F'">
        <a-space size="large">
          <a-icon :component="allIcon[form.icon + 'Icon']" v-if="form.icon && allIcon[form.icon + 'Icon']"/>
          <a-icon :type="form.icon" v-if="form.icon && !allIcon[form.icon + 'Icon']"/>
          <a-button type="dashed" @click="selectIcon">
            选择图标
          </a-button>
          <a @click="cancelSelectIcon" v-if="iconVisible" style="margin-left: 8px">
            收起
            <a-icon type="up"/>
          </a>
        </a-space>
      </a-form-model-item>
      <a-card :body-style="{padding: '10px 20px'}" :bordered="false" v-if="iconVisible">
        <icon-selector v-model="form.icon" @change="handleIconChange" :svgIcons="iconList" :allIcon="allIcon"/>
      </a-card>
      <a-form-model-item label="菜单名称" prop="menuName">
        <a-input v-model="form.menuName" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="排序" prop="orderNum">
        <a-input-number v-model="form.orderNum" :min="0" :max="9999" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item prop="isFrame" v-if="form.menuType != 'F'">
        <span slot="label">
          是否外链
          <a-tooltip>
            <template slot="title">
              选择是外链则路由地址需要以`http(s)://`开头
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.isFrame" button-style="solid">
          <a-radio-button value="0">是</a-radio-button>
          <a-radio-button value="1">否</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="path" v-if="form.menuType != 'F'">
        <span slot="label">
          路由地址
          <a-tooltip>
            <template slot="title">
              访问的路由地址，如：`user`，如外网地址需内链访问则以`http(s)://`开头
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.path" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item prop="component" v-if="form.menuType == 'C'">
        <span slot="label">
          组件路径
          <a-tooltip>
            <template slot="title">
              访问的组件路径，如：`system/user/index`，默认在`views`目录下
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.component" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item prop="perms" v-if="form.menuType != 'M'">
        <span slot="label">
          权限标识
          <a-tooltip>
            <template slot="title">
              控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.perms" placeholder="请输入" :maxLength="100" />
      </a-form-model-item>
      <a-form-model-item prop="visible" v-if="form.menuType != 'F'">
        <span slot="label">
          是否显示
          <a-tooltip>
            <template slot="title">
              选择隐藏则路由将不会出现在侧边栏，但仍然可以访问
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.visible" button-style="solid">
          <a-radio-button v-for="(d, index) in visibleOptions" :key="index" :value="d.value">{{ d.label }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="status" v-if="form.menuType != 'F'">
        <span slot="label">
          状态
          <a-tooltip>
            <template slot="title">
              选择停用则路由将不会出现在侧边栏，也不能被访问
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.value">{{ d.label }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="isCache" v-if="form.menuType == 'C'">
        <span slot="label">
          是否缓存
          <a-tooltip>
            <template slot="title">
              选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致，仅在多页签下有效。
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.isCache" button-style="solid">
          <a-radio-button value="0">缓存</a-radio-button>
          <a-radio-button value="1">不缓存</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>

import { getMenu, addMenu, updateMenu } from '@/api/system/menu'
import allIcon from '@/core/icons'
import icons from '@/utils/requireIcons'
import IconSelector from '@/components/IconSelector'

export default {
  name: 'CreateForm',
  props: {
    statusOptions: {
      type: Array,
      required: true
    },
    visibleOptions: {
      type: Array,
      required: true
    },
    menuOptions: {
      type: Array,
      required: true
    }
  },
  components: {
    IconSelector
  },
  data () {
    return {
      allIcon,
      iconVisible: false,
      iconList: icons,
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      },
      open: false,
      rules: {
        menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
        orderNum: [{ required: true, message: '菜单顺序不能为空', trigger: 'blur' }],
        path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }]
      }
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
    filterIcons () {
      this.iconList = icons
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.includes(this.name))
      }
    },
    onClose () {
      this.open = false
      this.iconVisible = false
    },
    hideIconSelect () {
      this.iconList = icons
      this.iconVisible = false
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.iconVisible = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      }
    },
     /** 新增按钮操作 */
    handleAdd (row) {
      this.reset()
      this.$emit('select-tree')
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.formTitle = '添加菜单'
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset()
      this.$emit('select-tree')
      getMenu(row.menuId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改菜单'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.menuId !== undefined) {
            updateMenu(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.open = false
              this.iconVisible = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            addMenu(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.open = false
              this.iconVisible = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },
    handleIconChange (icon) {
      this.iconVisible = false
      this.form.icon = icon
    },
    changeIcon (type) {
      this.currentSelectedIcon = type
    },
    selectIcon () {
      this.iconVisible = !this.iconVisible
    },
    cancelSelectIcon () {
      this.iconVisible = false
    }
  }
}
</script>

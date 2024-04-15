/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// 日期格式化
export function parseTime (time, pattern) {
	if (arguments.length === 0 || !time) {
		return null
	}
	const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
	let date
	if (typeof time === 'object') {
		date = time
	} else {
		if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
			time = parseInt(time)
		} else if (typeof time === 'string') {
			time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '')
		}
		if ((typeof time === 'number') && (time.toString().length === 10)) {
			time = time * 1000
		}
		date = new Date(time)
	}
	const formatObj = {
		y: date.getFullYear(),
		m: date.getMonth() + 1,
		d: date.getDate(),
		h: date.getHours(),
		i: date.getMinutes(),
		s: date.getSeconds(),
		a: date.getDay()
	}
	const timeStr = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
		let value = formatObj[key]
		// Note: getDay() returns 0 on Sunday
		if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
		if (result.length > 0 && value < 10) {
			value = '0' + value
		}
		return value || 0
	})
	return timeStr
}

// 表单重置
export function resetForm (refName) {
	if (this[refName]) {
		this[refName].resetFields()
	}
}

// 添加日期范围
export function addDateRange (params, dateRange, propName) {
	var search = params
	search.params = {}
	if (dateRange !== null && dateRange !== '' && dateRange.length === 2) {
		if (typeof (propName) === 'undefined') {
			search.params['beginTime'] = dateRange[0]
			search.params['endTime'] = dateRange[1]
		} else {
			search.params[propName + 'BeginTime'] = dateRange[0]
			search.params[propName + 'EndTime'] = dateRange[1]
		}
	}
	return search
}

// 回显数据字典
export function selectDictLabel (datas, value) {
	var actions = []
	Object.keys(datas).some((key) => {
		if (datas[key].value === ('' + value)) {
			actions.push(datas[key].label)
			return true
		}
	})
	return actions.join('')
}

// 回显数据字典（字符串数组）
export function selectDictLabels (datas, value, separator) {
	var actions = []
	var currentSeparator = undefined === separator ? ',' : separator
	var temp = value.split(currentSeparator)
	Object.keys(value.split(currentSeparator)).some((val) => {
		Object.keys(datas).some((key) => {
			if (datas[key].value === ('' + temp[val])) {
				actions.push(datas[key].label + currentSeparator)
			}
		})
	})
	return actions.join('').substring(0, actions.join('').length - 1)
}

// 字符串格式化(%s )
export function sprintf (str) {
	var args = arguments
	var flag = true
	var i = 1
	str = str.replace(/%s/g, function () {
		var arg = args[i++]
		if (typeof arg === 'undefined') {
			flag = false
			return ''
		}
		return arg
	})
	return flag ? str : ''
}

// 转换字符串，undefined,null等转化为''
export function parseStrEmpty (str) {
	if (!str || str === 'undefined' || str === 'null') {
		return ''
	}
	return str
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree (data, id, parentId, children, rootId) {
	id = id || 'id'
	parentId = parentId || 'parentId'
	children = children || 'children'
	rootId = rootId || Math.min.apply(Math, data.map(item => { return item[parentId] })) || 0
	// 对源数据深度克隆
	const cloneData = JSON.parse(JSON.stringify(data))
	// 循环所有项
	const treeData = cloneData.filter(father => {
		var branchArr = cloneData.filter(child => {
			// 返回每一项的子级数组
			return father[id] === child[parentId]
		})
		if (branchArr.length > 0) {
			father.children = branchArr
		} else {
			father.children = ''
		}
		// 返回第一层
		return father[parentId] === rootId
	})
	return treeData !== '' ? treeData : data
}

// 验证是否为blob格式
export async function blobValidate (data) {
	try {
		const text = await data.text()
		JSON.parse(text)
		return false
	} catch (error) {
		return true
	}
}

/**
 * 数据合并
 * @param {*} source
 * @param {*} target
 * @returns
 */
export function mergeRecursive (source, target) {
  for (var p in target) {
    try {
      if (target[p].constructor === Object) {
        source[p] = mergeRecursive(source[p], target[p])
      } else {
        source[p] = target[p]
      }
    } catch (e) {
      source[p] = target[p]
    }
  }
  return source
};

/**
 * 表格排序转换
 *
 * @param {*} sorter 排序
 * @returns 转换结果
 */
export function tableSorter (sorter) {
	let orderByColumn = null
	let isAsc = null
	if (sorter && sorter.order) {
		orderByColumn = sorter.field
		isAsc = sorter.order === 'descend' ? 'desc' : 'asc'
	}
	return {
		orderByColumn,
		isAsc
	}
}

/**
 * 根据子节点id获取父节点id
 *
 * @param {*} nodes
 * @param {*} nodeId 子节点id
 * @param {*} parentIds 父节点ids
 */
export function treeFindParentIds (nodes, nodeId, parentIds = []) {
	if (!nodes) {
		return []
	}
	for (const node of nodes) {
		parentIds.push(node.id)
		if (node.id === nodeId) {
			return parentIds
		}
		if (node.children) {
			const findChildren = treeFindParentIds(node.children, nodeId, parentIds)
			if (findChildren.length) return findChildren
		}
		parentIds.pop()
	}
	return []
}

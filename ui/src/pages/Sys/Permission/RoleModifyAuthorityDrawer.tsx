import {DrawerForm, ProFormSelect, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyAuthorityV3} from '@/services/admin/role';
import React from "react";

interface RoleAuthorityProps {
	modalModifyAuthorityVisit: boolean;
	setModalModifyAuthorityVisit: (visible: boolean) => void;
	title: string;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[];
	deptTreeList: any[];
	typeValue: string;
	setTypeValue: (value: string) => void;
}

type TableColumns = {
	id: number;
	dataScope: string | undefined;
	menuIds: string[];
	deptIds: string[];
};



export const RoleModifyAuthorityDrawer: React.FC<RoleAuthorityProps> = ({ modalModifyAuthorityVisit, setModalModifyAuthorityVisit, title, dataSource, onComponent, menuTreeList, deptTreeList, typeValue, setTypeValue }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalModifyAuthorityVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			initialValues={dataSource}
			onOpenChange={setModalModifyAuthorityVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					style: {
						display: 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				let menuIds: any[] = []
				let deptIds: any[] = []
				if (value?.menuIds) {
					menuIds = value?.menuIds.map((item: any) => item?.value ? item?.value : item)
				}
				if (value?.deptIds) {
					deptIds = value?.deptIds.map((item: any) => item?.value ? item?.value : item)
				}
				const co = {
					id: value?.id,
					dataScope: value?.dataScope,
					deptIds: deptIds,
					menuIds: menuIds
				}
				modifyAuthorityV3({co: co}).then(res => {
					if (res.code === 'OK') {
						message.success("分配权限成功").then()
						setModalModifyAuthorityVisit(false)
						onComponent();
					}
				})
			}}>

			<ProFormText
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="name"
				label="角色名称"
				disabled={true}
				placeholder={'请输入角色名称'}
				rules={[{ required: true, message: '请输入角色名称' }]}
			/>

			<ProFormTreeSelect
				name="menuIds"
				label="菜单权限"
				allowClear={true}
				placeholder={'请选择菜单权限'}
				rules={[{ required: true, message: '请选择菜单权限' }]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					},
					// 最多显示多少个 tag，响应式模式会对性能产生损耗
					maxTagCount: 6,
					// 多选
					multiple: true,
					// 显示复选框
					treeCheckable: true,
					// 展示策略
					showCheckedStrategy: 'SHOW_ALL',
					// 取消父子节点联动
					treeCheckStrictly: true,
					// 默认展示所有节点
					treeDefaultExpandAll: true,
					// 高度
					dropdownStyle: { maxHeight: 660 },
					// 不显示搜索
					showSearch: false,
					// 高度
					listHeight: 640
				}}
				request={async () => {
					return menuTreeList
				}}
			/>

			<ProFormSelect
				name="dataScope"
				label="数据范围"
				placeholder={'请选择数据范围'}
				rules={[{ required: true, message: '请选择数据范围' }]}
				onChange={(value: string) => {
					setTypeValue(value)
				}}
				options={[
					{value: 'all', label: '全部'},
					{value: 'custom', label: '自定义'},
					{value: 'dept_self', label: '仅本部门'},
					{value: 'dept', label: '部门及以下'},
					{value: 'self', label: '仅本人'},
				]}
			/>

			{typeValue === 'custom' && (
				<ProFormTreeSelect
					name="deptIds"
					label="部门权限"
					allowClear={true}
					placeholder={'请选择部门权限'}
					rules={[{ required: true, message: '请选择部门权限' }]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
							children: 'children'
						},
						// 最多显示多少个 tag，响应式模式会对性能产生损耗
						maxTagCount: 6,
						// 多选
						multiple: true,
						// 显示复选框
						treeCheckable: true,
						// 展示策略
						showCheckedStrategy: 'SHOW_ALL',
						// 取消父子节点联动
						treeCheckStrictly: true,
						// 默认展示所有节点
						treeDefaultExpandAll: true,
						// 高度
						dropdownStyle: { maxHeight: 460 },
						// 不显示搜索
						showSearch: false,
						// 高度
						listHeight: 450
					}}
					request={async () => {
						return deptTreeList
					}}
				/>
			)}

		</DrawerForm>
	);
};

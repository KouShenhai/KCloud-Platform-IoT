import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from '@/services/admin/role';
import {v7 as uuidV7} from "uuid";
import React from "react";

interface RoleDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[]
}

type TableColumns = {
	id: number;
	name: string | undefined;
	createTime: string | undefined;
	sort: number | undefined;
	dataScope: string | undefined;
	menuIds: string[];
	deptIds: string[];
};



export const RoleDrawer: React.FC<RoleDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, menuTreeList }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			initialValues={dataSource}
			onOpenChange={setModalVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				const co = {
					id: value.id,
					sort: value.sort,
					name: value.name,
					menuIds: [],
					deptIds: []
				}
				if (value.id === undefined) {
					saveV3({co: co}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				} else {
					modifyV3({co: co}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				}
			}}>

			<ProFormText
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="name"
				label="名称"
				readonly={readOnly}
				placeholder={'请输入名称'}
				rules={[{ required: true, message: '请输入名称' }]}
			/>

			<ProFormDigit
				name="sort"
				label="排序"
				readonly={readOnly}
				placeholder={'请输入排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入排序' }]}
			/>

			{ readOnly && (
			<ProFormSelect
				name="dataScope"
				label="数据范围"
				readonly={readOnly}
				placeholder={'请选择数据范围'}
				rules={[{ required: true, message: '请选择数据范围' }]}
				options={[
					{value: 'all', label: '全部'},
					{value: 'custom', label: '自定义'},
					{value: 'dept_self', label: '仅本部门'},
					{value: 'dept', label: '部门及以下'},
					{value: 'self', label: '仅本人'},
				]}
			/>
			)}

			{ readOnly && (
			<ProFormTreeSelect
				name="menuIds"
				label="菜单权限"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择菜单权限'}
				rules={[{ required: true, message: '请选择菜单权限' }]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					}
				}}
				request={async () => {
					return menuTreeList
				}}
			/>
			)}

			{ readOnly && (
				<ProFormText
					readonly={true}
					name="createTime"
					rules={[{ required: true, message: '请输入创建时间' }]}
					label="创建时间"
				/>
			)}

		</DrawerForm>
	);
};

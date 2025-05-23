import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyRole, saveRole} from '@/services/admin/role';
import {v7 as uuidV7} from "uuid";
import React, {useState} from "react";

interface RoleDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[]
	requestId: string
	setRequestId: (requestId: string) => void
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



export const RoleDrawer: React.FC<RoleDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, menuTreeList, requestId, setRequestId }) => {

	const [loading, setLoading] = useState(false)

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
					disabled: loading,
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				setLoading(true)
				const co = {
					id: value.id,
					sort: value.sort,
					name: value.name,
					menuIds: [],
					deptIds: []
				}
				if (value.id === undefined) {
					saveRole({co: co}, requestId).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					}).finally(() => {
						setRequestId(uuidV7())
						setLoading(false)
					})
				} else {
					modifyRole({co: co}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
						}
					}).finally(() => {
						setLoading(false)
					})
				}
			}}>

			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				disabled={loading}
				name="name"
				label="角色名称"
				readonly={readOnly}
				placeholder={'请输入角色名称'}
				rules={[{ required: true, message: '请输入角色名称' }]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label="角色排序"
				readonly={readOnly}
				placeholder={'请输入角色排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入角色排序' }]}
			/>

			{ readOnly && (
			<ProFormSelect
				disabled={loading}
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
				disabled={loading}
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
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[{ required: true, message: '请输入创建时间' }]}
					label="创建时间"
				/>
			)}

		</DrawerForm>
	);
};

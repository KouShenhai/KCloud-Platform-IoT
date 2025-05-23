import {
	DrawerForm,
	ProFormDigit,
	ProFormRadio,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyMenu, saveMenu} from "@/services/admin/menu";
import {v7 as uuidV7} from "uuid";
import React, {useState} from "react";

interface MenuDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	typeValue: number;
	setTypeValue: (value: number) => void;
	treeList: any[]
	requestId: string
	setRequestId: (requestId: string) => void
}

type TableColumns = {
	id: number;
	pid: number;
	name: string | undefined;
	path: string | undefined;
	status: number | undefined;
	type: number | undefined;
	permission: string | undefined;
	sort: number | undefined;
	createTime: string | undefined;
};

export const MenuDrawer: React.FC<MenuDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, typeValue, setTypeValue, treeList, requestId, setRequestId }) => {

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
				if (value.id === undefined) {
					saveMenu({co: value}, requestId).then(res => {
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
					modifyMenu({co: value}).then(res => {
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

			<ProFormTreeSelect
				disabled={loading}
				name="pid"
				label="父级菜单"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择父级菜单'}
				rules={[{ required: true, message: '请选择父级菜单' }]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children'
					},
				}}
				request={async () => {
					return treeList
				}}
			/>

			<ProFormText
				disabled={loading}
				name="name"
				label="菜单名称"
				readonly={readOnly}
				placeholder={'请输入菜单名称'}
				rules={[{ required: true, message: '请输入菜单名称' }]}
			/>

			<ProFormSelect
				disabled={loading}
				name="type"
				label="菜单类型"
				readonly={readOnly}
				placeholder={'请选择菜单类型'}
				rules={[{ required: true, message: '请选择菜单类型' }]}
				onChange={(value: number) => {
					setTypeValue(value)
				}}
				options={[
					{value: 0, label: '菜单'},
					{value: 1, label: '按钮'}
				]}
			/>

			{typeValue === 0 && (
				<ProFormText
					disabled={loading}
					name="path"
					label="菜单路径"
					tooltip={'只对菜单有效【不允许重复】'}
					readonly={readOnly}
					placeholder={'请输入菜单路径'}
					rules={[{ required: true, message: '请输入菜单路径' }]}
				/>
			)}

			{typeValue === 1 && (
				<ProFormText
					disabled={loading}
					name="permission"
					label="菜单权限标识"
					tooltip={'只对按钮有效【不允许重复】'}
					readonly={readOnly}
					placeholder={'请输入菜单权限标识'}
					rules={[{ required: true, message: '请输入菜单权限标识' }]}
				/>
			)}

			{typeValue === 0 && (
				<ProFormText
					disabled={loading}
					name="icon"
					label="菜单图标"
					tooltip={'只支持目录菜单显示图标'}
					readonly={readOnly}
					placeholder={'请输入菜单图标'}
				/>
			)}

			<ProFormDigit
				disabled={loading}
				name="sort"
				label="菜单排序"
				readonly={readOnly}
				placeholder={'请输入菜单排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入菜单排序' }]}
			/>

			{typeValue === 0 && (
				<ProFormRadio.Group
					disabled={loading}
					name="status"
					label="菜单状态"
					readonly={readOnly}
					rules={[{required: true, message: '请选择菜单状态',}]}
					options={[
						{label:"启用",value: 0 },
						{label:"禁用",value: 1 }
					]}
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

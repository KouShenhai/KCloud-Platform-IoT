import {
	DrawerForm,
	ProFormDigit,
	ProFormRadio,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect
} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/admin/menu";
import {v7 as uuidV7} from "uuid";
import React from "react";

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

export const MenuDrawer: React.FC<MenuDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, typeValue, setTypeValue, treeList }) => {

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
				if (value.id === undefined) {
					saveV3({co: value}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				} else {
					modifyV3({co: value}).then(res => {
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

			<ProFormTreeSelect
				name="pid"
				label="父级"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择父级'}
				rules={[{ required: true, message: '请选择父级' }]}
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
				name="name"
				label="名称"
				readonly={readOnly}
				placeholder={'请输入名称'}
				rules={[{ required: true, message: '请输入名称' }]}
			/>

			<ProFormSelect
				name="type"
				label="类型"
				readonly={readOnly}
				placeholder={'请选择类型'}
				rules={[{ required: true, message: '请选择类型' }]}
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
					name="path"
					label="路径"
					tooltip={'只对菜单有效【不允许重复】'}
					readonly={readOnly}
					placeholder={'请输入路径'}
					rules={[{ required: true, message: '请输入路径' }]}
				/>
			)}

			{typeValue === 1 && (
				<ProFormText
					name="permission"
					label="权限标识"
					tooltip={'只对按钮有效【不允许重复】'}
					readonly={readOnly}
					placeholder={'请输入权限标识'}
					rules={[{ required: true, message: '请输入权限标识' }]}
				/>
			)}

			{typeValue === 0 && (
				<ProFormText
					name="icon"
					label="图标"
					tooltip={'只支持目录菜单显示图标'}
					readonly={readOnly}
					placeholder={'请输入图标'}
				/>
			)}

			<ProFormDigit
				name="sort"
				label="排序"
				readonly={readOnly}
				placeholder={'请输入排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入排序' }]}
			/>

			{typeValue === 0 && (
				<ProFormRadio.Group
					name="status"
					label="状态"
					readonly={readOnly}
					rules={[{required: true, message: '请选择状态',}]}
					options={[
						{label:"启用",value: 0 },
						{label:"禁用",value: 1 }
					]}
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

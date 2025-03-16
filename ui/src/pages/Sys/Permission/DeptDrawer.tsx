import {DrawerForm, ProFormDigit, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/admin/dept";
import {v7 as uuidV7} from "uuid";
import React from "react";

interface DeptDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	treeList: any[]
}

type TableColumns = {
	id: number;
	pid: number;
	name: string | undefined;
	path: string | undefined;
	sort: number | undefined;
	createTime: string | undefined;
};

export const DeptDrawer: React.FC<DeptDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, treeList }) => {

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
			width={1200}
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

			{ readOnly && (
			<ProFormText
				name="path"
				label="路径"
				readonly={readOnly}
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

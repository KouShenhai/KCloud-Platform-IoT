import {DrawerForm, ProFormDigit, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/admin/dept";
import {v7 as uuidV7} from "uuid";
import React, {useState} from "react";

interface DeptDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	treeList: any[]
	requestId: string
	setRequestId: (requestId: string) => void
}

type TableColumns = {
	id: number;
	pid: number;
	name: string | undefined;
	path: string | undefined;
	sort: number | undefined;
	createTime: string | undefined;
};

export const DeptDrawer: React.FC<DeptDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, treeList, requestId, setRequestId }) => {

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
			width={1200}
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
					saveV3({co: value}, requestId).then(res => {
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
					modifyV3({co: value}).then(res => {
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
				label="父级部门"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择父级部门'}
				rules={[{ required: true, message: '请选择父级部门' }]}
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
				label="部门名称"
				readonly={readOnly}
				placeholder={'请输入部门名称'}
				rules={[{ required: true, message: '请输入部门名称' }]}
			/>

			{ readOnly && (
			<ProFormText
				disabled={loading}
				name="path"
				label="部门路径"
				readonly={readOnly}
			/>
			)}

			<ProFormDigit
				disabled={loading}
				name="sort"
				label="部门排序"
				readonly={readOnly}
				placeholder={'请输入部门排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入部门排序' }]}
			/>

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

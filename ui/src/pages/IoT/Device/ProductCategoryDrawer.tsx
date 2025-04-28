import {DrawerForm, ProFormDigit, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/iot/productCategory";
import {v7 as uuidV7} from "uuid";
import React, {useState} from "react";
import {ProFormTextArea} from "@ant-design/pro-form";

interface ProductCategoryDrawerProps {
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
	code: string | undefined;
	name: string | undefined;
	sort: number | undefined;
	pid: number | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ProductCategoryDrawer: React.FC<ProductCategoryDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, treeList, requestId, setRequestId }) => {

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
					// @ts-ignore
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
				label="父级产品类别"
				readonly={readOnly}
				allowClear={true}
				placeholder={'请选择父级产品类别'}
				rules={[{ required: true, message: '请选择父级产品类别' }]}
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
				readonly={readOnly}
				name="name"
				label="产品类别名称"
				rules={[{ required: true, message: '请输入产品类别名称' }]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label="产品类别排序"
				readonly={readOnly}
				placeholder={'请输入产品类别排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入产品类别排序' }]}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label="产品类别备注"
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

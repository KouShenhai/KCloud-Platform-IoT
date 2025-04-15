import {DrawerForm, ProFormDigit, ProFormText, ProFormTreeSelect} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/iot/productCategory";
import {v7 as uuidV7} from "uuid";
import React from "react";
import {ProFormTextArea} from "@ant-design/pro-form";

interface ProductCategoryDrawerProps {
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
	code: string | undefined;
	name: string | undefined;
	sort: number | undefined;
	pid: number | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ProductCategoryDrawer: React.FC<ProductCategoryDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent, treeList }) => {

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
				console.log(value)
				if (value.id === undefined) {
					// @ts-ignore
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
				readonly={readOnly}
				name="code"
				label="产品类别编码"
				rules={[{ required: true, message: '请输入产品类别编码' }]}
			/>

			<ProFormText
				readonly={readOnly}
				name="name"
				label="产品类别名称"
				rules={[{ required: true, message: '请输入产品类别名称' }]}
			/>

			<ProFormDigit
				name="sort"
				label="产品类别排序"
				readonly={readOnly}
				placeholder={'请输入产品类别排序'}
				min={1}
				max={99999}
				rules={[{ required: true, message: '请输入产品类别排序' }]}
			/>

			<ProFormTextArea
				readonly={readOnly}
				name="remark"
				label="产品类别备注"
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

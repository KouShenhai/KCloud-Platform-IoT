import {
	modifyProductCategory,
	saveProductCategory,
} from '@/services/iot/productCategory';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface ProductCategoryDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	treeList: any[];
	requestId: string;
	setRequestId: (requestId: string) => void;
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

export const ProductCategoryDrawer: React.FC<ProductCategoryDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	treeList,
	requestId,
	setRequestId,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
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
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				if (value.id === undefined) {
					// @ts-ignore
					saveProductCategory({ co: value }, requestId)
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.saveSuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setRequestId(uuidV7());
							setLoading(false);
						});
				} else {
					modifyProductCategory({ co: value })
						.then((res) => {
							if (res.code === 'OK') {
								message.success(t('toast.modifySuccess')).then();
								setModalVisit(false);
								onComponent();
							}
						})
						.finally(() => {
							setLoading(false);
						});
				}
			}}
		>
			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormTreeSelect
				disabled={loading}
				name="pid"
				label={t('iot.productCategory.pid')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('iot.productCategory.placeholder.pid')}
				rules={[
					{
						required: true,
						message: t('iot.productCategory.required.pid'),
					},
				]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children',
					},
				}}
				request={async () => {
					return treeList;
				}}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.productCategory.name')}
				rules={[
					{
						required: true,
						message: t('iot.productCategory.required.name'),
					},
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('iot.productCategory.sort')}
				readonly={readOnly}
				placeholder={t('iot.productCategory.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{
						required: true,
						message: t('iot.productCategory.required.sort'),
					},
				]}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.productCategory.remark')}
			/>

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{
							required: true,
							message: t('sys.role.validate.createTimeRequired'),
						},
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

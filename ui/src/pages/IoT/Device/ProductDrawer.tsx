import { pageProductCategory } from '@/services/iot/productCategory';
import { modifyProduct, saveProduct } from '@/services/iot/product';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface ProductDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	requestId: string;
	setRequestId: (requestId: string) => void;
}

type TableColumns = {
	id: number;
	name: string | undefined;
	categoryId: number | undefined;
	deviceType: number | undefined;
	imgUrl: string | undefined;
	cpId: number | undefined;
	tpId: number | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ProductDrawer: React.FC<ProductDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
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
					saveProduct({ co: value }, requestId)
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
					modifyProduct({ co: value })
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

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.product.name')}
				placeholder={t('iot.product.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('iot.product.required.name'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="categoryId"
				label={t('iot.product.categoryId')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('iot.product.placeholder.categoryId')}
				rules={[
					{
						required: true,
						message: t('iot.product.required.categoryId'),
					},
				]}
				request={async () => {
					return pageProductCategory({
						pageNum: 1,
						pageSize: 1000,
						pageIndex: 0,
					}).then((res) => {
						return (res?.data?.records || []).map((item: any) => ({
							label: item?.name,
							value: item?.id,
						}));
					});
				}}
			/>

			<ProFormSelect
				disabled={loading}
				name="deviceType"
				label={t('iot.product.deviceType')}
				readonly={readOnly}
				placeholder={t('iot.product.placeholder.deviceType')}
				rules={[
					{
						required: true,
						message: t('iot.product.required.deviceType'),
					},
				]}
				options={[
					{
						value: 1,
						label: t('iot.product.deviceType.direct'),
					},
					{
						value: 2,
						label: t('iot.product.deviceType.gateway'),
					},
					{
						value: 3,
						label: t('iot.product.deviceType.monitor'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="imgUrl"
				label={t('iot.product.imgUrl')}
				placeholder={t('iot.product.placeholder.imgUrl')}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="cpId"
				label={t('iot.product.cpId')}
				placeholder={t('iot.product.placeholder.cpId')}
				min={1}
				fieldProps={{ precision: 0 }}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="tpId"
				label={t('iot.product.tpId')}
				placeholder={t('iot.product.placeholder.tpId')}
				min={1}
				fieldProps={{ precision: 0 }}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.product.remark')}
			/>

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

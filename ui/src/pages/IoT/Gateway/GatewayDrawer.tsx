import { modifyGateway, saveGateway } from '@/services/iot/gateway';
import { pageProduct } from '@/services/iot/product';
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

interface GatewayDrawerProps {
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
	gatewayKey: string | undefined;
	name: string | undefined;
	status: number | undefined;
	productId: number | undefined;
	address: string | undefined;
	longitude: number | undefined;
	latitude: number | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const GatewayDrawer: React.FC<GatewayDrawerProps> = ({
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
					saveGateway({ co: value }, requestId)
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
					modifyGateway({ co: value })
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
				name="gatewayKey"
				label={t('iot.gateway.gatewayKey')}
				placeholder={t('iot.gateway.placeholder.gatewayKey')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.gatewayKey'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.gateway.name')}
				placeholder={t('iot.gateway.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.name'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="status"
				label={t('iot.gateway.status')}
				readonly={readOnly}
				placeholder={t('iot.gateway.placeholder.status')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.status'),
					},
				]}
				options={[
					{
						value: 0,
						label: t('iot.gateway.status.online'),
					},
					{
						value: 1,
						label: t('iot.gateway.status.offline'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="productId"
				label={t('iot.gateway.productId')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('iot.gateway.placeholder.productId')}
				rules={[
					{
						required: true,
						message: t('iot.gateway.required.productId'),
					},
				]}
				request={async () => {
					return pageProduct({
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

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="address"
				label={t('iot.gateway.address')}
				placeholder={t('iot.gateway.placeholder.address')}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="longitude"
				label={t('iot.gateway.longitude')}
				placeholder={t('iot.gateway.placeholder.longitude')}
				fieldProps={{ precision: 6 }}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="latitude"
				label={t('iot.gateway.latitude')}
				placeholder={t('iot.gateway.placeholder.latitude')}
				fieldProps={{ precision: 6 }}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.gateway.remark')}
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

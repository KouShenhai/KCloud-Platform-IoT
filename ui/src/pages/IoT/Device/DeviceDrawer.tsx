import { modifyDevice, saveDevice } from '@/services/iot/device';
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

interface DeviceDrawerProps {
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
	sn: string | undefined;
	name: string | undefined;
	status: number | undefined;
	productId: number | undefined;
	longitude: number | undefined;
	latitude: number | undefined;
	imgUrl: string | undefined;
	address: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const DeviceDrawer: React.FC<DeviceDrawerProps> = ({
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
					saveDevice({ co: value }, requestId)
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
					modifyDevice({ co: value })
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
				name="sn"
				label={t('iot.device.sn')}
				placeholder={t('iot.device.placeholder.sn')}
				rules={[
					{
						required: true,
						message: t('iot.device.required.sn'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('iot.device.name')}
				placeholder={t('iot.device.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('iot.device.required.name'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="status"
				label={t('iot.device.status')}
				readonly={readOnly}
				placeholder={t('iot.device.placeholder.status')}
				rules={[
					{
						required: true,
						message: t('iot.device.required.status'),
					},
				]}
				options={[
					{
						value: 0,
						label: t('iot.device.status.online'),
					},
					{
						value: 1,
						label: t('iot.device.status.offline'),
					},
				]}
			/>

			<ProFormSelect
				disabled={loading}
				name="productId"
				label={t('iot.device.productId')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('iot.device.placeholder.productId')}
				rules={[
					{
						required: true,
						message: t('iot.device.required.productId'),
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

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="longitude"
				label={t('iot.device.longitude')}
				placeholder={t('iot.device.placeholder.longitude')}
				fieldProps={{ precision: 6 }}
			/>

			<ProFormDigit
				disabled={loading}
				readonly={readOnly}
				name="latitude"
				label={t('iot.device.latitude')}
				placeholder={t('iot.device.placeholder.latitude')}
				fieldProps={{ precision: 6 }}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="imgUrl"
				label={t('iot.device.imgUrl')}
				placeholder={t('iot.device.placeholder.imgUrl')}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="address"
				label={t('iot.device.address')}
				placeholder={t('iot.device.placeholder.address')}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.device.remark')}
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

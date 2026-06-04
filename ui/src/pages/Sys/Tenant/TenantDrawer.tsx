import { modifyTenant, saveTenant } from '@/services/admin/tenant';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormText,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface TenantDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: API.TenantCO;
	title: string;
	readOnly: boolean;
	requestId: string;
	setRequestId: (requestId: string) => void;
	onComponent: () => void;
}

export const TenantDrawer: React.FC<TenantDrawerProps> = ({
	modalVisit,
	setModalVisit,
	dataSource,
	title,
	readOnly,
	requestId,
	setRequestId,
	onComponent,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm<API.TenantCO>
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
					saveTenant({ co: value }, requestId)
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
					modifyTenant({ co: value })
						.then((res) => {
							if (res.code === 'OK') {
								message
									.success(t('toast.modifySuccess'))
									.then();
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
				name="name"
				label={t('sys.tenant.name')}
				readonly={readOnly}
				placeholder={t('sys.tenant.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.tenant.required.name') },
					{ max: 100, message: t('sys.tenant.validate.nameMax') },
				]}
			/>

			<ProFormText
				disabled={loading}
				name="code"
				label={t('sys.tenant.code')}
				readonly={readOnly}
				placeholder={t('sys.tenant.placeholder.code')}
				rules={[
					{ required: true, message: t('sys.tenant.required.code') },
					{ max: 30, message: t('sys.tenant.validate.codeMax') },
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sourceId"
				label={t('sys.tenant.sourceId')}
				readonly={readOnly}
				placeholder={t('sys.tenant.placeholder.sourceId')}
				min={0}
				max={Number.MAX_SAFE_INTEGER}
				rules={[
					{
						required: true,
						message: t('sys.tenant.required.sourceId'),
					},
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="packageId"
				label={t('sys.tenant.packageId')}
				readonly={readOnly}
				placeholder={t('sys.tenant.placeholder.packageId')}
				min={0}
				max={Number.MAX_SAFE_INTEGER}
				rules={[
					{
						required: true,
						message: t('sys.tenant.required.packageId'),
					},
				]}
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

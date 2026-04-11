import { useIntl } from '@@/exports';
import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import React from 'react';

interface OperateLogDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
	getStatus: (status: string) => string;
}

type TableColumns = {
	id: number;
	name: string | undefined;
	status: string | undefined;
	param: string | undefined;
	errorMessage: string | undefined;
	moduleName: string | undefined;
	uri: string | undefined;
	methodName: string | undefined;
	requestType: string | undefined;
	requestParams: string | undefined;
	userAgent: string | undefined;
	ip: string | undefined;
	address: string | undefined;
	operator: string | undefined;
	costTime: number | string;
	profile: string | undefined;
	serviceId: string | undefined;
	serviceAddress: string | undefined;
	stackTrace: string | undefined;
	createTime: string | undefined;
};

export const OperateLogDrawer: React.FC<OperateLogDrawerProps> = ({
	modalVisit,
	setModalVisit,
	dataSource,
	getStatus,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={t('sys.operateLog.drawer.title')}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			width={1200}
			initialValues={dataSource}
			onOpenChange={setModalVisit}
			submitter={{
				submitButtonProps: {
					style: {
						display: 'none',
					},
				},
			}}
		>
			<ProFormText
				readonly={true}
				name="name"
				label={t('sys.operateLog.name')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.name'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="moduleName"
				label={t('sys.operateLog.moduleName')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.moduleName'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="operator"
				label={t('sys.operateLog.operator')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.operator'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="uri"
				label={t('sys.operateLog.uri')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.uri'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="requestType"
				label={t('sys.operateLog.requestType')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.requestType'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="requestParams"
				label={t('sys.operateLog.requestParams')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.requestParams'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="methodName"
				label={t('sys.operateLog.methodName')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.methodName'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="userAgent"
				label={t('sys.operateLog.userAgent')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.userAgent'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="ip"
				label={t('sys.operateLog.ip')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.ip'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="address"
				label={t('sys.operateLog.address')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.address'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="status"
				label={t('sys.operateLog.status')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.status'),
					},
				]}
				convertValue={(value) => {
					return getStatus(value as string);
				}}
			/>

			<ProFormText
				readonly={true}
				name="profile"
				label={t('sys.operateLog.profile')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.profile'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="serviceId"
				label={t('sys.operateLog.serviceId')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.serviceId'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="serviceAddress"
				label={t('sys.operateLog.serviceAddress')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.serviceAddress'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="errorMessage"
				label={t('sys.operateLog.errorMessage')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.errorMessage'),
					},
				]}
			/>

			<ProFormTextArea
				readonly={true}
				name="stackTrace"
				label={t('sys.operateLog.stackTrace')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.stackTrace'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="createTime"
				label={t('common.createTime')}
				rules={[
					{
						required: true,
						message: t('sys.operateLog.required.createTime'),
					},
				]}
			/>
		</DrawerForm>
	);
};

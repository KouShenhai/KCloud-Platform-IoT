import { useIntl } from '@@/exports';
import { DrawerForm, ProFormText } from '@ant-design/pro-components';
import React from 'react';

interface NoticeLogDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
	getStatus: (status: string) => string;
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	status: string | undefined;
	param: string | undefined;
	errorMessage: string | undefined;
	createTime: string | undefined;
};

export const NoticeLogDrawer: React.FC<NoticeLogDrawerProps> = ({
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
			title={t('sys.log.notice.drawer.title')}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
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
				name="code"
				label={t('sys.log.notice.code')}
				rules={[
					{
						required: true,
						message: t('sys.log.notice.required.code'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="name"
				label={t('sys.log.notice.name')}
				rules={[
					{
						required: true,
						message: t('sys.log.notice.required.name'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="status"
				label={t('sys.log.notice.status')}
				rules={[
					{
						required: true,
						message: t('sys.log.notice.required.status'),
					},
				]}
				convertValue={(value) => {
					return getStatus(value as string);
				}}
			/>

			<ProFormText
				readonly={true}
				name="param"
				label={t('sys.log.notice.param')}
				rules={[
					{
						required: true,
						message: t('sys.log.notice.required.param'),
					},
				]}
			/>

			<ProFormText
				readonly={true}
				name="errorMessage"
				label={t('sys.log.notice.errorMessage')}
				rules={[
					{
						required: true,
						message: t('sys.log.notice.required.errorMessage'),
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
						message: t('sys.log.notice.required.createTime'),
					},
				]}
			/>
		</DrawerForm>
	);
};

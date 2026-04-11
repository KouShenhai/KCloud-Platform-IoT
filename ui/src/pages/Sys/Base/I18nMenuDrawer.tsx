import { useIntl } from '@@/exports';
import {DrawerForm, ProFormText} from '@ant-design/pro-components';
import React, {useState} from 'react';
import {message} from "antd";
import {v7 as uuidV7} from "uuid";
import {modifyI18nMenu, saveI18nMenu} from "@/services/admin/i18nMenu";

interface I18nMenuDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
	title: string;
	readOnly: boolean;
	requestId: string;
	setRequestId: (requestId: string) => void;
	onComponent: () => void;
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	createTime: string | undefined;
};

export const I18nMenuDrawer: React.FC<I18nMenuDrawerProps> = ({
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
					saveI18nMenu({ co: value }, requestId)
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
					// @ts-ignore
					modifyI18nMenu({ co: value })
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
				name="code"
				readonly={readOnly}
				label={t('sys.i18nMenu.code')}
				rules={[
					{
						required: true,
						message: t('sys.i18nMenu.placeholder.code'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				readonly={readOnly}
				name="name"
				label={t('sys.i18nMenu.name')}
				rules={[
					{
						required: true,
						message: t('sys.i18nMenu.placeholder.name'),
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

import { useIntl } from '@@/exports';
import {DrawerForm, ProFormText} from '@ant-design/pro-components';
import React, {useState} from 'react';

interface I18nMenuDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
	title: string;
	readOnly: boolean;
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
	readOnly
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
		>
			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{ required: true, message: t('sys.role.validate.createTimeRequired') },
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

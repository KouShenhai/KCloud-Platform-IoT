import { modifyDict, saveDict } from '@/services/admin/dict';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormRadio,
	ProFormText,
	ProFormTextArea,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface DictDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: API.DictCO;
	title: string;
	readOnly: boolean;
	requestId: string;
	setRequestId: (requestId: string) => void;
	onComponent: () => void;
}

export const DictDrawer: React.FC<DictDrawerProps> = ({
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
		<DrawerForm<API.DictCO>
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
					saveDict({ co: value }, requestId)
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
					modifyDict({ co: value })
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
				label={t('sys.dict.name')}
				readonly={readOnly}
				placeholder={t('sys.dict.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.dict.required.name') },
					{ max: 100, message: t('sys.dict.validate.nameMax') },
				]}
			/>

			<ProFormText
				disabled={loading}
				name="type"
				label={t('sys.dict.type')}
				readonly={readOnly}
				placeholder={t('sys.dict.placeholder.type')}
				rules={[
					{ required: true, message: t('sys.dict.required.type') },
					{ max: 100, message: t('sys.dict.validate.typeMax') },
				]}
			/>

			<ProFormRadio.Group
				disabled={loading}
				name="status"
				label={t('sys.dict.status')}
				readonly={readOnly}
				rules={[
					{ required: true, message: t('sys.dict.required.status') },
				]}
				options={[
					{ label: t('common.enable'), value: 0 },
					{ label: t('common.disable'), value: 1 },
				]}
			/>

			<ProFormTextArea
				disabled={loading}
				name="remark"
				label={t('sys.dict.remark')}
				readonly={readOnly}
				placeholder={t('sys.dict.placeholder.remark')}
				fieldProps={{
					rows: 4,
					maxLength: 500,
					showCount: true,
				}}
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

import { modifyDictItem, saveDictItem } from '@/services/admin/dictItem';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormRadio,
	ProFormText,
	ProFormTextArea,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface DictItemDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: API.DictItemCO;
	title: string;
	readOnly: boolean;
	requestId: string;
	setRequestId: (requestId: string) => void;
	onComponent: () => void;
}

export const DictItemDrawer: React.FC<DictItemDrawerProps> = ({
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
		<DrawerForm<API.DictItemCO>
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
					saveDictItem({ co: value }, requestId)
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
					modifyDictItem({ co: value })
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
				name="dictId"
				label={t('sys.dictItem.dictId')}
				hidden={true}
			/>

			<ProFormText
				disabled={loading}
				name="name"
				label={t('sys.dictItem.name')}
				readonly={readOnly}
				placeholder={t('sys.dictItem.placeholder.name')}
				rules={[
					{
						required: true,
						message: t('sys.dictItem.required.name'),
					},
					{
						max: 100,
						message: t('sys.dictItem.validate.nameMax'),
					},
				]}
			/>

			<ProFormText
				disabled={loading}
				name="code"
				label={t('sys.dictItem.code')}
				readonly={readOnly}
				placeholder={t('sys.dictItem.placeholder.code')}
				rules={[
					{
						required: true,
						message: t('sys.dictItem.required.code'),
					},
					{
						max: 100,
						message: t('sys.dictItem.validate.codeMax'),
					},
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('sys.dictItem.sort')}
				readonly={readOnly}
				placeholder={t('sys.dictItem.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{
						required: true,
						message: t('sys.dictItem.required.sort'),
					},
				]}
			/>

			<ProFormRadio.Group
				disabled={loading}
				name="status"
				label={t('sys.dictItem.status')}
				readonly={readOnly}
				rules={[
					{
						required: true,
						message: t('sys.dictItem.required.status'),
					},
				]}
				options={[
					{ label: t('common.enable'), value: 0 },
					{ label: t('common.disable'), value: 1 },
				]}
			/>

			<ProFormTextArea
				disabled={loading}
				name="remark"
				label={t('sys.dictItem.remark')}
				readonly={readOnly}
				placeholder={t('sys.dictItem.placeholder.remark')}
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

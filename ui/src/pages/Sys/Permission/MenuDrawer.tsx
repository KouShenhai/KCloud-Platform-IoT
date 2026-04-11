import { modifyMenu, saveMenu } from '@/services/admin/menu';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormRadio,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface MenuDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	typeValue: number;
	setTypeValue: (value: number) => void;
	treeList: any[];
	requestId: string;
	setRequestId: (requestId: string) => void;
}

type TableColumns = {
	id: number;
	pid: number;
	name: string | undefined;
	path: string | undefined;
	status: number | undefined;
	type: number | undefined;
	permission: string | undefined;
	sort: number | undefined;
	createTime: string | undefined;
};

export const MenuDrawer: React.FC<MenuDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	typeValue,
	setTypeValue,
	treeList,
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
					saveMenu({ co: value }, requestId)
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
					modifyMenu({ co: value })
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

			<ProFormSelect
				disabled={loading}
				name="type"
				label={t('sys.menu.type')}
				readonly={readOnly}
				placeholder={t('sys.menu.placeholder.type')}
				rules={[
					{ required: true, message: t('sys.menu.required.type') },
				]}
				onChange={(value: number) => {
					setTypeValue(value);
				}}
				options={[
					{ value: 0, label: t('sys.menu.type.menu') },
					{ value: 1, label: t('sys.menu.type.button') },
				]}
			/>

			<ProFormTreeSelect
				disabled={loading}
				name="pid"
				label={t('sys.menu.pid')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('sys.menu.placeholder.pid')}
				rules={[
					{ required: true, message: t('sys.menu.required.pid') },
				]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children',
					},
				}}
				request={async () => {
					return treeList;
				}}
			/>

			<ProFormText
				disabled={loading}
				name="name"
				label={t('sys.menu.name')}
				readonly={readOnly}
				placeholder={t('sys.menu.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.menu.required.name') },
				]}
			/>

			{typeValue === 0 && (
				<ProFormText
					disabled={loading}
					name="path"
					label={t('sys.menu.path')}
					tooltip={t('sys.menu.tooltip.path')}
					readonly={readOnly}
					placeholder={t('sys.menu.placeholder.path')}
					rules={[
						{ required: true, message: t('sys.menu.required.path') },
					]}
				/>
			)}

			{typeValue === 1 && (
				<ProFormText
					disabled={loading}
					name="permission"
					label={t('sys.menu.permission')}
					tooltip={t('sys.menu.tooltip.permission')}
					readonly={readOnly}
					placeholder={t('sys.menu.placeholder.permission')}
					rules={[
						{
							required: true,
							message: t('sys.menu.required.permission'),
						},
					]}
				/>
			)}

			{typeValue === 0 && (
				<ProFormText
					disabled={loading}
					name="icon"
					label={t('sys.menu.icon')}
					tooltip={t('sys.menu.tooltip.icon')}
					readonly={readOnly}
					placeholder={t('sys.menu.placeholder.icon')}
				/>
			)}

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('sys.menu.sort')}
				readonly={readOnly}
				placeholder={t('sys.menu.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('sys.menu.required.sort') },
				]}
			/>

			{typeValue === 0 && (
				<ProFormRadio.Group
					disabled={loading}
					name="status"
					label={t('sys.menu.status')}
					readonly={readOnly}
					rules={[
						{
							required: true,
							message: t('sys.menu.required.status'),
						},
					]}
					options={[
						{ label: t('common.enable'), value: 0 },
						{ label: t('common.disable'), value: 1 },
					]}
				/>
			)}

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{
							required: true,
							message: t('sys.role.validate.createTimeRequired'),
						},
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

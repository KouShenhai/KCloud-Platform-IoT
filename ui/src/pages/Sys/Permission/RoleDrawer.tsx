import { modifyRole, saveRole } from '@/services/admin/role';
import {
	DrawerForm,
	ProFormDigit,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { useIntl } from '@@/exports';
import { v7 as uuidV7 } from 'uuid';

interface RoleDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[];
	deptTreeList: any[];
	requestId: string;
	setRequestId: (requestId: string) => void;
}

type TableColumns = {
	id: number;
	name: string | undefined;
	createTime: string | undefined;
	sort: number | undefined;
	dataScope: string | undefined;
	menuIds: string[];
	deptIds: string[];
};

export const RoleDrawer: React.FC<RoleDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
	menuTreeList,
	deptTreeList,
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
				const co = {
					id: value.id,
					sort: value.sort,
					name: value.name,
					menuIds: [],
					deptIds: [],
				};
				if (value.id === undefined) {
					saveRole({ co: co }, requestId)
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
					modifyRole({ co: co })
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
				name="name"
				label={t('role.name')}
				readonly={readOnly}
				placeholder={t('role.placeholder.name')}
				rules={[
					{ required: true, message: t('role.validate.nameRequired') },
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('role.sort')}
				readonly={readOnly}
				placeholder={t('role.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('role.validate.sortRequired') },
				]}
			/>

			{readOnly && (
				<ProFormSelect
					disabled={loading}
					name="dataScope"
					label={t('role.dataScope')}
					readonly={readOnly}
					placeholder={t('role.placeholder.dataScope')}
					rules={[
						{
							required: true,
							message: t('role.validate.dataScopeRequired'),
						},
					]}
					options={[
						{ value: 'all', label: t('role.dataScope.all') },
						{ value: 'custom', label: t('role.dataScope.custom') },
						{ value: 'dept_self', label: t('role.dataScope.selfDept') },
						{ value: 'dept', label: t('role.dataScope.belowDept') },
						{ value: 'self', label: t('role.dataScope.self') },
					]}
				/>
			)}

			{readOnly && (
				<ProFormTreeSelect
					disabled={loading}
					name="menuIds"
					label={t('role.menuAuthority')}
					readonly={readOnly}
					allowClear={true}
					placeholder={t('role.placeholder.menuAuthority')}
					rules={[
						{
							required: true,
							message: t('role.validate.menuAuthorityRequired'),
						},
					]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
							children: 'children',
						},
					}}
					request={async () => {
						return menuTreeList;
					}}
				/>
			)}

			{readOnly && dataSource.dataScope === 'custom' && (
				<ProFormTreeSelect
					disabled={loading}
					name="deptIds"
					label={t('role.deptAuthority')}
					readonly={readOnly}
					allowClear={true}
					placeholder={t('role.placeholder.deptAuthority')}
					rules={[
						{
							required: true,
							message: t('role.validate.deptAuthorityRequired'),
						},
					]}
					fieldProps={{
						fieldNames: {
							label: 'name',
							value: 'id',
							children: 'children',
						},
					}}
					request={async () => {
						return deptTreeList;
					}}
				/>
			)}

			{readOnly && (
				<ProFormText
					disabled={loading}
					readonly={true}
					name="createTime"
					rules={[
						{ required: true, message: t('role.validate.createTimeRequired') },
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

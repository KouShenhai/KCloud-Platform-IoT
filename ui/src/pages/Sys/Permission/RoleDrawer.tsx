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
				label={t('sys.role.name')}
				readonly={readOnly}
				placeholder={t('sys.role.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.role.validate.nameRequired') },
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('sys.role.sort')}
				readonly={readOnly}
				placeholder={t('sys.role.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('sys.role.validate.sortRequired') },
				]}
			/>

			{readOnly && (
				<ProFormSelect
					disabled={loading}
					name="dataScope"
					label={t('sys.role.dataScope')}
					readonly={readOnly}
					placeholder={t('sys.role.placeholder.dataScope')}
					rules={[
						{
							required: true,
							message: t('sys.role.validate.dataScopeRequired'),
						},
					]}
					options={[
						{ value: 'all', label: t('sys.role.dataScope.all') },
						{ value: 'custom', label: t('sys.role.dataScope.custom') },
						{ value: 'dept_self', label: t('sys.role.dataScope.selfDept') },
						{ value: 'dept', label: t('sys.role.dataScope.belowDept') },
						{ value: 'self', label: t('sys.role.dataScope.self') },
					]}
				/>
			)}

			{readOnly && (
				<ProFormTreeSelect
					disabled={loading}
					name="menuIds"
					label={t('sys.role.menuAuthority')}
					readonly={readOnly}
					allowClear={true}
					placeholder={t('sys.role.placeholder.menuAuthority')}
					rules={[
						{
							required: true,
							message: t('sys.role.validate.menuAuthorityRequired'),
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
					label={t('sys.role.deptAuthority')}
					readonly={readOnly}
					allowClear={true}
					placeholder={t('sys.role.placeholder.deptAuthority')}
					rules={[
						{
							required: true,
							message: t('sys.role.validate.deptAuthorityRequired'),
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
						{ required: true, message: t('sys.role.validate.createTimeRequired') },
					]}
					label={t('common.createTime')}
				/>
			)}
		</DrawerForm>
	);
};

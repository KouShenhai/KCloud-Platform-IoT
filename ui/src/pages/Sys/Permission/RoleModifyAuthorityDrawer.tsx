import { modifyRoleAuthority } from '@/services/admin/role';
import {
	DrawerForm,
	ProFormSelect,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { useIntl } from '@@/exports';

interface RoleAuthorityProps {
	modalModifyAuthorityVisit: boolean;
	setModalModifyAuthorityVisit: (visible: boolean) => void;
	title: string;
	dataSource: TableColumns;
	onComponent: () => void;
	menuTreeList: any[];
	deptTreeList: any[];
	typeValue: string;
	setTypeValue: (value: string) => void;
}

type TableColumns = {
	id: number;
	dataScope: string | undefined;
	menuIds: string[];
	deptIds: string[];
};

export const RoleModifyAuthorityDrawer: React.FC<RoleAuthorityProps> = ({
	modalModifyAuthorityVisit,
	setModalModifyAuthorityVisit,
	title,
	dataSource,
	onComponent,
	menuTreeList,
	deptTreeList,
	typeValue,
	setTypeValue,
}) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	const [loading, setLoading] = useState(false);

	return (
		<DrawerForm<TableColumns>
			open={modalModifyAuthorityVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={dataSource}
			onOpenChange={setModalModifyAuthorityVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					disabled: loading,
					style: {
						display: 'inline-block',
					},
				},
			}}
			onFinish={async (value) => {
				setLoading(true);
				let menuIds: any[] = [];
				let deptIds: any[] = [];
				if (value?.menuIds) {
					menuIds = value?.menuIds.map((item: any) =>
						item?.value ? item?.value : item,
					);
				}
				if (value?.deptIds) {
					deptIds = value?.deptIds.map((item: any) =>
						item?.value ? item?.value : item,
					);
				}
				const co = {
					id: value?.id,
					dataScope: value?.dataScope,
					deptIds: deptIds,
					menuIds: menuIds,
				};
				modifyRoleAuthority({ co: co })
					.then((res) => {
						if (res.code === 'OK') {
							message.success(t('toast.assignSuccess')).then();
							setModalModifyAuthorityVisit(false);
							onComponent();
						}
					})
					.finally(() => {
						setLoading(false);
					});
			}}
		>
			<ProFormText
				disabled={loading}
				name="id"
				label="ID"
				hidden={true}
			/>

			<ProFormText
				name="name"
				label={t('sys.role.name')}
				disabled={true}
				placeholder={t('sys.role.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.role.validate.nameRequired') },
				]}
			/>

			<ProFormTreeSelect
				disabled={loading}
				name="menuIds"
				label={t('sys.role.menuAuthority')}
				allowClear={true}
				placeholder={t('sys.role.placeholder.menuAuthority')}
				rules={[
					{ required: true, message: t('sys.role.validate.menuAuthorityRequired') },
				]}
				fieldProps={{
					fieldNames: {
						label: 'name',
						value: 'id',
						children: 'children',
					},
					// 最多显示多少个 tag，响应式模式会对性能产生损耗
					maxTagCount: 6,
					// 多选
					multiple: true,
					// 显示复选框
					treeCheckable: true,
					// 展示策略
					showCheckedStrategy: 'SHOW_ALL',
					// 取消父子节点联动
					treeCheckStrictly: true,
					// 默认展示所有节点
					treeDefaultExpandAll: true,
					// 高度
					dropdownStyle: { maxHeight: 660 },
					// 不显示搜索
					showSearch: false,
					// 高度
					listHeight: 640,
				}}
				request={async () => {
					return menuTreeList;
				}}
			/>

			<ProFormSelect
				disabled={loading}
				name="dataScope"
				label={t('sys.role.dataScope')}
				placeholder={t('sys.role.placeholder.dataScope')}
				rules={[
					{
						required: true,
						message: t('sys.role.validate.dataScopeRequired'),
					},
				]}
				onChange={(value: string) => {
					setTypeValue(value);
				}}
				options={[
					{ value: 'all', label: t('sys.role.dataScope.all') },
					{ value: 'custom', label: t('sys.role.dataScope.custom') },
					{ value: 'self_dept', label: t('sys.role.dataScope.selfDept') },
					{ value: 'below_dept', label: t('sys.role.dataScope.belowDept') },
					{ value: 'self', label: t('sys.role.dataScope.self') },
				]}
			/>

			{typeValue === 'custom' && (
				<ProFormTreeSelect
					disabled={loading}
					name="deptIds"
					label={t('sys.role.deptAuthority')}
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
						// 最多显示多少个 tag，响应式模式会对性能产生损耗
						maxTagCount: 6,
						// 多选
						multiple: true,
						// 显示复选框
						treeCheckable: true,
						// 展示策略
						showCheckedStrategy: 'SHOW_ALL',
						// 取消父子节点联动
						treeCheckStrictly: true,
						// 默认展示所有节点
						treeDefaultExpandAll: true,
						// 高度
						dropdownStyle: { maxHeight: 460 },
						// 不显示搜索
						showSearch: false,
						// 高度
						listHeight: 450,
					}}
					request={async () => {
						return deptTreeList;
					}}
				/>
			)}
		</DrawerForm>
	);
};

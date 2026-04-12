import { modifyDept, saveDept } from '@/services/admin/dept';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormText,
	ProFormTreeSelect,
} from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';

interface DeptDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	treeList: any[];
	requestId: string;
	setRequestId: (requestId: string) => void;
}

type TableColumns = {
	id: number;
	pid: number;
	name: string | undefined;
	path: string | undefined;
	sort: number | undefined;
	createTime: string | undefined;
};

export const DeptDrawer: React.FC<DeptDrawerProps> = ({
	modalVisit,
	setModalVisit,
	title,
	readOnly,
	dataSource,
	onComponent,
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
			width={1200}
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
					saveDept({ co: value }, requestId)
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
					modifyDept({ co: value })
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

			<ProFormTreeSelect
				disabled={loading}
				name="pid"
				label={t('sys.dept.pid')}
				readonly={readOnly}
				allowClear={true}
				placeholder={t('sys.dept.placeholder.pid')}
				rules={[
					{ required: true, message: t('sys.dept.required.pid') },
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
				label={t('sys.dept.name')}
				readonly={readOnly}
				placeholder={t('sys.dept.placeholder.name')}
				rules={[
					{ required: true, message: t('sys.dept.required.name') },
				]}
			/>

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('sys.dept.sort')}
				readonly={readOnly}
				placeholder={t('sys.dept.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('sys.dept.required.sort') },
				]}
			/>

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

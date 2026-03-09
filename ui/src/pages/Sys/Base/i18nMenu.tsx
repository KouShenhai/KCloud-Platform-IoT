import {
	getI18nMenuById,
	modifyI18nMenu,
	pageI18nMenu,
	removeI18nMenu,
	saveI18nMenu,
} from '@/services/admin/i18nMenu';
import { trim } from '@/utils/format';
import { useIntl } from '@@/exports';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ModalForm, ProFormText, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Space } from 'antd';
import { useRef, useState } from 'react';

export default () => {
	type TableColumns = {
		id: number | undefined;
		code: string | undefined;
		name: string | undefined;
	};

	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);

	const actionRef = useRef<ActionType | null>(null);

	const [modalVisit, setModalVisit] = useState(false);
	const [readOnly, setReadOnly] = useState(false);
	const [title, setTitle] = useState('');
	const [dataSource, setDataSource] = useState<any>({});

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			code: trim(params?.code),
			name: trim(params?.name),
		};
	};

	const columns: ProColumns<TableColumns>[] = [
		{
			title: t('common.number'),
			dataIndex: 'index',
			valueType: 'indexBorder',
			width: 110,
		},
		{
			title: t('sys.i18nMenu.code'),
			dataIndex: 'code',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.i18nMenu.placeholder.code'),
			},
		},
		{
			title: t('sys.i18nMenu.name'),
			dataIndex: 'name',
			ellipsis: true,
			valueType: 'text',
			fieldProps: {
				placeholder: t('sys.i18nMenu.placeholder.name'),
			},
		},
		{
			title: t('common.operation'),
			valueType: 'option',
			key: 'option',
			render: (_, record) => [
				<a
					key="get"
					onClick={() => {
						getI18nMenuById({ id: record?.id as number }).then((res) => {
							if (res.code === 'OK') {
								setTitle(t('sys.i18nMenu.view'));
								setModalVisit(true);
								setReadOnly(true);
								setDataSource(res?.data);
							}
						});
					}}
				>
					{t('common.view')}
				</a>,
				<a
					key="modify"
					onClick={() => {
						getI18nMenuById({ id: record?.id as number }).then((res) => {
							if (res.code === 'OK') {
								setTitle(t('sys.i18nMenu.modify'));
								setModalVisit(true);
								setReadOnly(false);
								setDataSource(res?.data);
							}
						});
					}}
				>
					{t('common.modify')}
				</a>,
				<a
					key="remove"
					onClick={() => {
						Modal.confirm({
							title: t('confirm.deleteTitle'),
							content: t('confirm.deleteContent'),
							okText: t('common.ok'),
							cancelText: t('common.cancel'),
							onOk: () => {
								removeI18nMenu([record?.id as number]).then((res) => {
									if (res.code === 'OK') {
										message.success(t('toast.deleteSuccess')).then();
										// @ts-ignore
										actionRef?.current?.reload();
									}
								});
							},
						});
					}}
				>
					{t('common.delete')}
				</a>,
			],
			width: 190,
		},
	];

	return (
		<>
			<ModalForm
				title={title}
				open={modalVisit}
				modalProps={{
					destroyOnClose: true,
					onCancel: () => setModalVisit(false),
				}}
				submitTimeout={2000}
				readonly={readOnly}
				initialValues={dataSource}
				onFinish={async (values: any) => {
					const api = values?.id ? modifyI18nMenu : saveI18nMenu;
					return api({
						...values,
						id: values?.id,
						code: trim(values?.code),
						name: trim(values?.name),
					}).then((res: any) => {
						if (res.code === 'OK') {
							message.success(t('toast.saveSuccess')).then();
							setModalVisit(false);
							// @ts-ignore
							actionRef?.current?.reload();
							return true;
						}
						return false;
					});
				}}
			>
				<ProFormText name="id" hidden />
				<ProFormText
					name="code"
					label={t('sys.i18nMenu.code')}
					placeholder={t('sys.i18nMenu.placeholder.code')}
					disabled={readOnly}
					rules={[{ required: true, message: t('toast.required') }]}
				/>
				<ProFormText
					name="name"
					label={t('sys.i18nMenu.name')}
					placeholder={t('sys.i18nMenu.placeholder.name')}
					disabled={readOnly}
					rules={[{ required: true, message: t('toast.required') }]}
				/>
			</ModalForm>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={async (params) => {
					return pageI18nMenu(getPageQueryParam(params)).then((res) => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res?.data?.total || 0),
							success: true,
						});
					});
				}}
				rowKey="id"
				pagination={{
					showQuickJumper: true,
					showSizeChanger: false,
					pageSize: 10,
				}}
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={() => [
					<Button
						key="save"
						type="primary"
						icon={<PlusOutlined />}
						onClick={() => {
							setTitle(t('sys.i18nMenu.insert'));
							setReadOnly(false);
							setModalVisit(true);
							setDataSource({
								id: undefined,
								code: '',
								name: '',
							});
						}}
					>
						{t('common.insert')}
					</Button>,
				]}
				dateFormatter="string"
				toolbar={{
					title: t('menu.sys.base.i18n-menu'),
					tooltip: t('menu.sys.base.i18n-menu'),
				}}
			/>
		</>
	);
};

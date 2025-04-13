import {
	ProColumns
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import { pageV3, removeV3, getByIdV3 } from '@/services/admin/user';
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal, Space, Switch, Tag, UploadFile} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {trim} from "@/utils/format";
import { ResetPwdDrawer } from '@/pages/Sys/Permission/ResetPwdDrawer';
import {UserDrawer} from "@/pages/Sys/Permission/UserDrawer";
import {listTreeV3} from "@/services/admin/dept";
import {pageV3 as rolePageV3} from "@/services/admin/role";
import {UserModifyAuthorityDrawer} from "@/pages/Sys/Permission/UserModifyAuthorityDrawer";
import {useAccess} from "@@/exports";

export default () => {

	type TableColumns = {
		id: number;
		username: string | undefined;
		status: number | undefined;
		mail: string | undefined;
		mobile: string | undefined;
		createTime: string | undefined;
		superAdmin: number | undefined;
	};

	const access = useAccess()
	const [readOnly, setReadOnly] = useState(false)
	const [modalVisit, setModalVisit] = useState(false);
	const [modalModifyAuthorityVisit, setModalModifyAuthorityVisit] = useState(false);
	const [modalRestPwdVisit, setModalRestPwdVisit] = useState(false);
	const actionRef = useRef();
	const [dataSource, setDataSource] = useState<any>({})
	const [ids, setIds] = useState<number[]>([])
	const [title, setTitle] = useState("")
	const [edit, setEdit] = useState(false)
	const [deptTreeList, setDeptTreeList] = useState<any[]>([])
	const [roleList, setRoleList] = useState<any[]>([])
	const [fileList, setFileList] = useState<UploadFile[]>([])

	const getDeptTreeList = async () => {
		listTreeV3({}).then(res => {
			setDeptTreeList(res?.data)
		})
	}

	const getRoleList = async () => {
		rolePageV3({pageSize: 10000, pageNum: 1, pageIndex: 0}).then(res => {
			setRoleList(res?.data?.records)
		})
	}

	const getPageQueryParam = (params: any) => {
		return {
			pageSize: params?.pageSize,
			pageNum: params?.current,
			pageIndex: params?.pageSize * (params?.current - 1),
			username: trim(params?.username),
			mail: trim(params?.mail),
			mobile: trim(params?.mobile),
			superAdmin: params?.superAdmin,
			status: params?.status,
			params: {
				startTime: params?.startDate ? `${params.startDate} 00:00:00` : undefined,
				endTime: params?.endDate ? `${params.endDate} 23:59:59` : undefined
			}
		}
	}

	const rowSelection: TableRowSelection<TableColumns> = {
		onChange: (selectedRowKeys) => {
			const ids: number[] = []
			selectedRowKeys.forEach(item => {
				ids.push(item as number)
			})
			setIds(ids)
		}
	};

	useEffect(() => {
		getDeptTreeList().catch(console.log)
		getRoleList().catch(console.log)
	}, []);

	const columns: ProColumns<TableColumns>[] = [
		{
			title: '用户名',
			dataIndex: 'username',
			tooltip: "仅支持四个字符的模糊查询",
			valueType: 'text',
			ellipsis: true,
			fieldProps: {
				placeholder: '请输入用户名',
			}
		},
		{
			title: '用户邮箱',
			dataIndex: 'mail',
			tooltip: "仅支持四个字符的模糊查询",
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入用户邮箱',
			},
			ellipsis: true,
		},
		{
			title: '用户手机号',
			dataIndex: 'mobile',
			tooltip: "仅支持三个或四个字符的模糊查询",
			valueType: 'text',
			fieldProps: {
				placeholder: '请输入用户手机号',
			},
			ellipsis: true,
		},
		{
			title: '超级管理员',
			dataIndex: 'superAdmin',
			valueType: 'select',
			hideInTable: true,
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择超级管理员',
				options: [
					{
						label: '否',
						value: 0,
					},
					{
						label: '是',
						value: 1,
					},
				],
			},
			ellipsis: true
		},
		{
			disable: true,
			title: '超级管理员',
			dataIndex: 'superAdmin',
			hideInSearch: true,
			renderFormItem: (_, { defaultRender }) => {
				return defaultRender(_);
			},
			render: (_, record) => (
				<Space>
					{record?.superAdmin === 0 && (
						<Tag color={'rgb(51 114 253)'} key={'menu'}>
							否
						</Tag>
					)}
					{record?.superAdmin === 1 && (
						<Tag color={'#fd5251'} key={'button'}>
							是
						</Tag>
					)}
				</Space>
			),
		},
		{
			title: '用户状态',
			dataIndex: 'status',
			hideInTable: true,
			valueType: 'select',
			fieldProps: {
				valueType: 'select',
				mode: 'single',
				placeholder: '请选择用户状态',
				options: [
					{
						label: '启用',
						value: 0,
					},
					{
						label: '禁用',
						value: 1,
					},
				],
			},
			ellipsis: true
		},
		{
			title: '用户状态',
			dataIndex: 'status',
			hideInSearch: true,
			render: (_, record) => (
				<Switch checkedChildren="启用" unCheckedChildren="禁用" disabled={true} checked={record?.status === 0} />
			),
		},
		{
			title: '创建时间',
			key: 'createTime',
			dataIndex: 'createTime',
			valueType: 'dateTime',
			hideInSearch: true,
			width: 160,
			ellipsis: true
		},
		{
			title: '创建时间',
			dataIndex: 'createTimeValue',
			valueType: 'dateRange',
			hideInTable: true,
			fieldProps: {
				placeholder: ['请选择开始时间', '请选择结束时间'],
			},
			search: {
				transform: (value) => {
					return {
						startDate: value[0],
						endDate: value[1],
					};
				},
			}
		},
		{
			title: '操作',
			valueType: 'option',
			key: 'option',
			width: 250,
			render: (_, record) => [
				( access.canUserGetDetail && <a key="get"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('查看用户')
							   setModalVisit(true)
							   setReadOnly(true)
							   setDataSource(res?.data)
							   const avatar = res?.data?.avatar;
							   if (avatar) {
								   setFileList([{uid: '-1', name: '用户头像.png', status: 'done', url: avatar}])
							   } else {
								   setFileList([])
							   }
						   }
					   })
				   }}
				>
					查看
				</a>),
				( access.canUserModify && <a key="modify"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   if (res.code === 'OK') {
							   setTitle('修改用户')
							   setModalVisit(true)
							   setReadOnly(false)
							   setEdit(true)
							   setDataSource(res?.data)
							   const avatar = res?.data?.avatar;
							   if (avatar) {
								   setFileList([{uid: '-1', name: '用户头像.png', status: 'done', url: avatar}])
							   } else {
								   setFileList([])
							   }
						   }
					   })
				   }}
				>
					修改
				</a>),
				( access.canUserModify && <a key={'modifyAuthority'} onClick={() => {
					getByIdV3({id: record?.id}).then(res => {
						setTitle('分配权限')
						setModalModifyAuthorityVisit(true)
						setDataSource(res?.data)
					})
				}}>
					分配权限
				</a>),
				( access.canUserModify && <a key={'resetPwd'} onClick={() => {
					getByIdV3({id: record?.id}).then(res => {
						setModalRestPwdVisit(true)
						setDataSource(res?.data)
					})
				}}>
					重置密码
				</a>),
				( access.canUserRemove && <a key="remove" onClick={() => {
					Modal.confirm({
						title: '确认删除?',
						content: '您确定要删除吗?',
						okText: '确认',
						cancelText: '取消',
						onOk: () => {
							removeV3([record?.id]).then(res => {
								if (res.code === 'OK') {
									message.success("删除成功").then()
									// @ts-ignore
									actionRef?.current?.reload();
								}
							})
						}
					})
				}}>
					删除
				</a>)
			],
		},
	];

	return (
		<>

			<ResetPwdDrawer visible={modalRestPwdVisit}
				setVisible={setModalRestPwdVisit}
				dataSource={dataSource}
			/>

			<UserDrawer
				onComponent={async () => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				deptTreeList={deptTreeList}
				roleList={roleList}
				edit={edit}
				modalVisit={modalVisit}
				setModalVisit={setModalVisit}
				title={title}
				readOnly={readOnly}
				dataSource={dataSource}
				fileList={fileList}
				setFileList={setFileList}
			/>

			<UserModifyAuthorityDrawer
				modalModifyAuthorityVisit={modalModifyAuthorityVisit}
				setModalModifyAuthorityVisit={setModalModifyAuthorityVisit}
				title={title}
				dataSource={dataSource}
				onComponent={() => {
					// @ts-ignore
					actionRef?.current?.reload();
				}}
				deptTreeList={deptTreeList}
				roleList={roleList}
			/>

			<ProTable<TableColumns>
				actionRef={actionRef}
				columns={columns}
				request={ async (params) => {
					// 表单搜索项会从 params 传入，传递给后端接口。
					return pageV3(getPageQueryParam(params)).then(res => {
						return Promise.resolve({
							data: res?.data?.records,
							total: parseInt(res.data.total),
							success: true,
						});
					})
				}}
				rowSelection={{ ...rowSelection }}
				rowKey="id"
				search={{
					layout: 'vertical',
					defaultCollapsed: true,
				}}
				toolBarRender={
					() => [
						( access.canUserSave && <Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
							setTitle('新增用户')
							setReadOnly(false)
							setModalVisit(true)
							setEdit(false)
							setDataSource({
								id: undefined,
								username: '',
								superAdmin: 0,
								status: 0,
							})
							setFileList([])
						}}>
							新增
						</Button>),
						( access.canUserRemove && <Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
							Modal.confirm({
								title: '确认删除?',
								content: '您确定要删除吗?',
								okText: '确认',
								cancelText: '取消',
								onOk: async () => {
									if (ids.length === 0) {
										message.warning("请至少选择一条数据").then()
										return;
									}
									removeV3(ids).then(res => {
										if (res.code === 'OK') {
											message.success("删除成功").then()
											// @ts-ignore
											actionRef?.current?.reload();
										}
									})
								},
							});
						}}>
							删除
						</Button>)
					]
				}
				dateFormatter="string"
				toolbar={{
					title: '用户',
					tooltip: '用户【默认密码：laokou123】',
				}}
			/>
		</>
	);
};

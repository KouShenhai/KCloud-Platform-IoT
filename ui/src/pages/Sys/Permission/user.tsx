import {
	ProColumns
} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import { pageV3, removeV3, getByIdV3 } from '@/services/admin/user';
import {useEffect, useRef, useState} from "react";
import {TableRowSelection} from "antd/es/table/interface";
import {Button, message, Modal, Space, Switch, Tag} from 'antd';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import {trim} from "@/utils/format";
import { ResetPwdDrawer } from '@/pages/Sys/Permission/ResetPwdDrawer';
import {UserDrawer} from "@/pages/Sys/Permission/UserDrawer";
import {treeListV3} from "@/services/admin/dept";
import {pageV3 as rolePageV3} from "@/services/admin/role";
import {UserModifyAuthorityDrawer} from "@/pages/Sys/Permission/UserModifyAuthorityDrawer";

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

	const getDeptTreeList = async () => {
		treeListV3({}).then(res => {
			setDeptTreeList(res?.data)
		})
	}

	const getRoleList = async () => {
		rolePageV3({pageSize: 10000, pageNum: 1, pageIndex: 0}).then(res => {
			setRoleList(res?.data?.records)
		})
	}

	const getPageQuery = (params: any) => {
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
		},
		{
			title: '邮箱',
			dataIndex: 'mail',
			tooltip: "仅支持四个字符的模糊查询",
		},
		{
			title: '手机号',
			dataIndex: 'mobile',
			tooltip: "仅支持三个或四个字符的模糊查询",
		},
		{
			title: '超级管理员',
			dataIndex: 'superAdmin',
			hideInTable: true,
			valueEnum: {
				0: {text: '否', status: 'Processing'},
				1: {text: '是', status: 'Default'},
			},
			ellipsis: true
		},
		{
			disable: true,
			title: '超级管理员',
			dataIndex: 'superAdmin',
			search: false,
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
			title: '状态',
			dataIndex: 'status',
			hideInTable: true,
			valueEnum: {
				0: {text: '启用', status: 'Success'},
				1: {text: '禁用', status: 'Error'},
			},
			ellipsis: true
		},
		{
			title: '状态',
			dataIndex: 'status',
			search: false,
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
			dataIndex: 'createTime',
			valueType: 'dateRange',
			hideInTable: true,
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
				<a key="get"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('查看用户')
						   setModalVisit(true)
						   setReadOnly(true)
						   setDataSource(res?.data)
					   })
				   }}
				>
					查看
				</a>,
				<a key="modify"
				   onClick={() => {
					   getByIdV3({id: record?.id}).then(res => {
						   setTitle('修改用户')
						   setModalVisit(true)
						   setReadOnly(false)
						   setEdit(true)
						   setDataSource(res?.data)
					   })
				   }}
				>
					修改
				</a>,
				<a key={'modifyAuthority'} onClick={() => {
					getByIdV3({id: record?.id}).then(res => {
						setTitle('分配权限')
						setModalModifyAuthorityVisit(true)
						setDataSource(res?.data)
					})
				}}>
					分配权限
				</a>,
				<a key={'resetPwd'} onClick={() => {
					getByIdV3({id: record?.id}).then(res => {
						setModalRestPwdVisit(true)
						setDataSource(res?.data)
					})
				}}>
					重置密码
				</a>,
				<a key="remove" onClick={() => {
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
				</a>
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
					return pageV3(getPageQuery(params)).then(res => {
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
						<Button key="save" type="primary" icon={<PlusOutlined />} onClick={() => {
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
						}}>
							新增
						</Button>,
						<Button key="remove" type="primary" danger icon={<DeleteOutlined />} onClick={() => {
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
									// @ts-ignore
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
						</Button>
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

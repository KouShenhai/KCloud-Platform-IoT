import {DrawerForm, ProFormText} from '@ant-design/pro-components';
import {getStatus} from "@/services/constant";

interface DeptDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	treeList: any[]
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	status: string | undefined;
	param: string | undefined;
	errorMessage: string | undefined;
	moduleName: string | undefined;
	uri: string | undefined;
	methodName: string | undefined;
	requestType: string | undefined;
	requestParams: string | undefined;
	userAgent: string | undefined;
	ip: string | undefined;
	address: string | undefined;
	operator: string | undefined;
	costTime: number | string;
	profile: string | undefined;
	serviceAddress: string | undefined;
	stackTrace: string | undefined;
};

export const DeptDrawer: React.FC<DeptDrawerProps> = ({ modalVisit, setModalVisit, dataSource }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title="查看操作日志"
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			initialValues={dataSource}
			onOpenChange={setModalVisit}
			submitter={{
				submitButtonProps: {
					style: {
						display: 'none',
					},
				}
			}}>

			<ProFormText
				readonly={true}
				name="code"
				label="标识"
				rules={[{ required: true, message: '请输入标识' }]}
			/>

			<ProFormText
				readonly={true}
				name="name"
				label="名称"
				rules={[{ required: true, message: '请输入名称' }]}
			/>

			<ProFormText
				readonly={true}
				name="status"
				label="状态"
				rules={[{ required: true, message: '请输入状态' }]}
				// @ts-ignore
				convertValue={(value) => {
					return getStatus(value as '0')?.text
				}}
			/>

			<ProFormText
				readonly={true}
				name="param"
				label="参数"
				rules={[{ required: true, message: '请输入参数' }]}
			/>

			<ProFormText
				readonly={true}
				name="errorMessage"
				label="错误信息"
				rules={[{ required: true, message: '请输入错误信息' }]}
			/>

			<ProFormText
				readonly={true}
				name="createTime"
				label="创建时间"
				rules={[{ required: true, message: '请输入创建时间' }]}
			/>

		</DrawerForm>
	);
};

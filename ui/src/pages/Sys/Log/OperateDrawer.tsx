import {DrawerForm, ProFormText} from '@ant-design/pro-components';
import {getStatus} from "@/services/constant";
import {ProFormTextArea} from "@ant-design/pro-form";

interface OperateLogDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
}

type TableColumns = {
	id: number;
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
	serviceId: string | undefined;
	serviceAddress: string | undefined;
	stackTrace: string | undefined;
	createTime: string | undefined;
};

export const OperateLogDrawer: React.FC<OperateLogDrawerProps> = ({ modalVisit, setModalVisit, dataSource }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title="查看操作日志"
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			width={1200}
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
				name="name"
				label="操作名称"
				rules={[{ required: true, message: '请输入操作名称' }]}
			/>

			<ProFormText
				readonly={true}
				name="moduleName"
				label="模块名称"
				rules={[{ required: true, message: '请输入模块名称' }]}
			/>

			<ProFormText
				readonly={true}
				name="operator"
				label="操作人"
				rules={[{ required: true, message: '请输入操作人' }]}
			/>

			<ProFormText
				readonly={true}
				name="uri"
				label="请求路径"
				rules={[{ required: true, message: '请输入URI' }]}
			/>

			<ProFormText
				readonly={true}
				name="requestType"
				label="请求类型"
				rules={[{ required: true, message: '请输入请求类型' }]}
			/>

			<ProFormText
				readonly={true}
				name="requestParams"
				label="请求参数"
				rules={[{ required: true, message: '请输入请求参数' }]}
			/>

			<ProFormText
				readonly={true}
				name="methodName"
				label="方法名"
				rules={[{ required: true, message: '请输入方法名' }]}
			/>

			<ProFormText
				readonly={true}
				name="userAgent"
				label="浏览器"
				rules={[{ required: true, message: '请输入浏览器' }]}
			/>

			<ProFormText
				readonly={true}
				name="ip"
				label="IP地址"
				rules={[{ required: true, message: '请输入IP地址' }]}
			/>

			<ProFormText
				readonly={true}
				name="address"
				label="IP地址"
				rules={[{ required: true, message: '请输入IP地址' }]}
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
				name="profile"
				label="服务环境"
				rules={[{ required: true, message: '请输入服务环境' }]}
			/>

			<ProFormText
				readonly={true}
				name="serviceId"
				label="服务ID"
				rules={[{ required: true, message: '请输入服务ID' }]}
			/>

			<ProFormText
				readonly={true}
				name="serviceAddress"
				label="服务地址"
				rules={[{ required: true, message: '请输入服务地址' }]}
			/>

			<ProFormText
				readonly={true}
				name="errorMessage"
				label="错误信息"
				rules={[{ required: true, message: '请输入错误信息' }]}
			/>

			<ProFormTextArea
				readonly={true}
				name="stackTrace"
				label="堆栈信息"
				rules={[{ required: true, message: '请输入堆栈信息' }]}
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

import {DrawerForm, ProFormText} from '@ant-design/pro-components';
import {getStatus} from "@/services/constant";
import React from "react";

interface NoticeLogDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	dataSource: TableColumns;
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	status: string | undefined;
	param: string | undefined;
	errorMessage: string | undefined;
	createTime: string | undefined;
};

export const NoticeLogDrawer: React.FC<NoticeLogDrawerProps> = ({ modalVisit, setModalVisit, dataSource }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title="查看通知日志"
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
			}}
		>
			<ProFormText
				readonly={true}
				name="code"
				label="编码"
				rules={[{ required: true, message: '请输入编码' }]}
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

import {DrawerForm} from '@ant-design/pro-components';
import { message } from 'antd';
import {modifyV3, saveV3} from "@/services/admin/dept";
import {v7 as uuidV7} from "uuid";
import React from "react";

interface ThingModelDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	path: string | undefined;
	sort: number | undefined;
	dataType: string | undefined;
	category: number | undefined;
	type: string | undefined;
	expression: string | undefined;
	specs: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({ modalVisit, setModalVisit, title, readOnly, dataSource, onComponent }) => {

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true
			}}
			initialValues={dataSource}
			onOpenChange={setModalVisit}
			autoFocusFirstInput
			submitter={{
				submitButtonProps: {
					style: {
						display: readOnly ? 'none' : 'inline-block',
					},
				}
			}}
			onFinish={ async (value) => {
				if (value.id === undefined) {
					saveV3({co: value}, uuidV7()).then(res => {
						if (res.code === 'OK') {
							message.success("新增成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				} else {
					modifyV3({co: value}).then(res => {
						if (res.code === 'OK') {
							message.success("修改成功").then()
							setModalVisit(false)
							onComponent()
						}
					})
				}
			}}>

		</DrawerForm>
	);
};

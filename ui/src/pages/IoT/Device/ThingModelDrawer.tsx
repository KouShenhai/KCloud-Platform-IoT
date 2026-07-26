import { modifyThingModel, saveThingModel } from '@/services/iot/thingModel';
import { useIntl } from '@@/exports';
import {
	DrawerForm,
	ProFormDigit,
	ProFormList,
	ProFormSelect,
	ProFormText,
} from '@ant-design/pro-components';
import { ProFormTextArea } from '@ant-design/pro-form';
import { Col, message, Row } from 'antd';
import React, { useState } from 'react';
import { v7 as uuidV7 } from 'uuid';
import ProFormNumber from "@/pages/IoT/Device/ProFormNumber";

interface ThingModelDrawerProps {
	modalVisit: boolean;
	setModalVisit: (visible: boolean) => void;
	title: string;
	readOnly: boolean;
	dataSource: TableColumns;
	onComponent: () => void;
	dataType: string;
	setDataType: (type: string) => void;
	requestId: string;
	setRequestId: (requestId: string) => void;
	dataTypeOptions: any[]
	dataUnitOptions: any[]
}

type TableColumns = {
	id: number;
	code: string | undefined;
	name: string | undefined;
	sort: number | undefined;
	dataType: string | undefined;
	category: number | undefined;
	type: string | undefined;
	spec: string | undefined;
	remark: string | undefined;
	createTime: string | undefined;
};

export const ThingModelDrawer: React.FC<ThingModelDrawerProps> = ({
	  modalVisit,
	  setModalVisit,
	  title,
	  readOnly,
	  dataSource,
	  onComponent,
	  dataType,
	  setDataType,
	  requestId,
	  setRequestId,
	  dataTypeOptions,
	  dataUnitOptions,
  }) => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [loading, setLoading] = useState(false);
	const initialValues = React.useMemo(() => {
		if (
			dataSource?.dataType !== 'enum' ||
			Array.isArray((dataSource as any)?.enumItems)
		) {
			return dataSource;
		}
		try {
			const spec = dataSource?.spec ? JSON.parse(dataSource.spec) : {};
			return {
				...dataSource,
				enumItems: Array.isArray(spec)
					? spec
					: Array.isArray(spec?.list)
						? spec.list
						: undefined,
			};
		} catch {
			return dataSource;
		}
	}, [dataSource]);

	const getSpec = (value: any) => {
		switch (value.dataType) {
			case 'boolean':
				return {
					trueText: value?.trueText,
					falseText: value?.falseText,
				};
			case 'text':
				return {
					length: value?.length,
				};
			case 'int':
			case 'long':
			case 'float':
			case 'double':
				return {
					min: value?.min,
					max: value?.max,
					unit: value?.unit
				};
			case 'enum': {
				return {
					list: value?.enumItems?.map((item: any) => ({
						code: item?.code?.trim(),
						desc: item?.desc?.trim(),
					})),
				};
			}
			default:
				return {};
		}
	};

	return (
		<DrawerForm<TableColumns>
			open={modalVisit}
			title={title}
			drawerProps={{
				destroyOnClose: true,
				closable: true,
				maskClosable: true,
			}}
			initialValues={initialValues}
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
				value.spec = JSON.stringify(getSpec(value));
				if (value.id === undefined) {
					saveThingModel({ co: value }, requestId)
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
					modifyThingModel({ co: value })
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
				disabled={loading || dataSource.id !== undefined}
				readonly={readOnly}
				name="code"
				label={t('iot.thingModel.code')}
				rules={[
					{ required: true, message: t('iot.thingModel.required.code') },
				]}
			/>

			<ProFormText
				disabled={loading || dataSource.id !== undefined}
				readonly={readOnly}
				name="name"
				label={t('iot.thingModel.name')}
				rules={[
					{ required: true, message: t('iot.thingModel.required.name') },
				]}
			/>

			<ProFormSelect
				disabled={loading || dataSource.id !== undefined}
				name="dataType"
				label={t('iot.thingModel.dataType')}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.dataType')}
				rules={[
					{
						required: true,
						message: t('iot.thingModel.required.dataType'),
					},
				]}
				options={dataTypeOptions}
				onChange={setDataType}
			/>

			{dataType === 'enum' && (
				<ProFormList
					name="enumItems"
					label={t('iot.thingModel.enumItems')}
					readonly={readOnly}
					initialValue={
						(initialValues as any)?.enumItems === undefined
							? [{ code: '', desc: '' }]
							: undefined
					}
					rules={[
						{
							validator: async (_, value) => {
								if (!value?.length) {
									throw new Error(
										t('iot.thingModel.required.enumItems'),
									);
								}
							},
						},
					]}
					copyIconProps={false}
					deleteIconProps={
						readOnly || loading || dataSource.id !== undefined
							? false
							: {
									tooltipText: t('iot.thingModel.enum.delete'),
							  }
					}
					creatorButtonProps={
						readOnly
							? false
							: {
									creatorButtonText: t(
										'iot.thingModel.enum.add',
									),
									disabled: loading || dataSource.id !== undefined,
							  }
					}
				>
					<Row gutter={24}>
						<Col span={12}>
							<ProFormText
								disabled={loading || dataSource.id !== undefined}
								name="code"
								label={t('iot.thingModel.enum.code')}
								placeholder={t('iot.thingModel.required.enum.code')}
								rules={[
									{
										required: true,
										message: t(
											'iot.thingModel.required.enum.code',
										),
									},
								]}
							/>
						</Col>
						<Col span={12}>
							<ProFormText
								disabled={loading || dataSource.id !== undefined}
								name="desc"
								label={t('iot.thingModel.enum.description')}
								placeholder={t('iot.thingModel.required.enum.description')}
								rules={[
									{
										required: true,
										message: t(
											'iot.thingModel.required.enum.description',
										),
									},
								]}
							/>
						</Col>
					</Row>
				</ProFormList>
			)}

			{dataType === 'int' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="min"
							label="最小值"
							type="int"
							precision={0} />
					</Col>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="max"
							label="最大值"
							type="int"
							precision={0} />
					</Col>
					<Col span={8}>
						<ProFormSelect
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="unit"
							label="单位"
							options={dataUnitOptions}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'long' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="min"
							label={"最小值"}
							type="long"
							precision={0} />
					</Col>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="max"
							label={"最大值"}
							type="long"
							precision={0} />
					</Col>
					<Col span={8}>
						<ProFormSelect
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="unit"
							label="单位"
							options={dataUnitOptions}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'float' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="min"
							label={"最小值"}
							type="float"
							precision={3} />
					</Col>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="max"
							label={"最大值"}
							type="float"
							precision={3} />
					</Col>
					<Col span={8}>
						<ProFormSelect
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="unit"
							label="单位"
							options={dataUnitOptions}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'double' && (
				<Row gutter={24}>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="min"
							label="最小值"
							type="double"
							precision={6} />
					</Col>
					<Col span={8}>
						<ProFormNumber
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="max"
							label={"最大值"}
							type="double"
							precision={6} />
					</Col>
					<Col span={8}>
						<ProFormSelect
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="unit"
							label="单位"
							options={dataUnitOptions}
						/>
					</Col>
				</Row>
			)}

			{dataType === 'text' && (
				<ProFormDigit
					disabled={loading || dataSource.id !== undefined}
					readonly={readOnly}
					name="length"
					label={t('iot.thingModel.length')}
					min={1}
					max={10000}
					rules={[
						{
							required: true,
							message: t('iot.thingModel.required.length'),
						}
					]}
				/>
			)}

			{dataType === 'boolean' && (
				<Row gutter={24}>
					<Col span={12}>
						<ProFormText
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="trueText"
							label={t('iot.thingModel.trueText')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.trueText'),
								},
							]}
						/>
					</Col>
					<Col span={12}>
						<ProFormText
							disabled={loading || dataSource.id !== undefined}
							readonly={readOnly}
							name="falseText"
							label={t('iot.thingModel.falseText')}
							rules={[
								{
									required: true,
									message: t('iot.thingModel.required.falseText'),
								},
							]}
						/>
					</Col>
				</Row>
			)}

			<ProFormDigit
				disabled={loading}
				name="sort"
				label={t('iot.thingModel.sort')}
				readonly={readOnly}
				placeholder={t('iot.thingModel.placeholder.sort')}
				min={1}
				max={99999}
				rules={[
					{ required: true, message: t('iot.thingModel.required.sort') },
				]}
			/>

			<ProFormTextArea
				disabled={loading}
				readonly={readOnly}
				name="remark"
				label={t('iot.thingModel.remark')}
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

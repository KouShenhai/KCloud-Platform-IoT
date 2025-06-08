import React from 'react';
import {Area} from '@ant-design/plots';

const DeviceMessageArea: React.FC = () => {
	const data = [
		{date: '20250601', value: 6000},
		{date: '20250602', value: 7200},
		{date: '20250603', value: 20000},
		{date: '20250604', value: 7300},
		{date: '20250605', value: 23000},
		{date: '20250606', value: 6500},
		{date: '20250607', value: 5000},
		{date: '20250608', value: 2000},
		{date: '20250610', value: 2000},
		{date: '20250609', value: 0},
		{date: '20250611', value: 0},
		{date: '20250612', value: 0},
		{date: '20250613', value: 0},
		{date: '20250614', value: 0},
		{date: '20250615', value: 0},
		{date: '20250616', value: 0},
		{date: '20250617', value: 0},
		{date: '20250618', value: 0},
		{date: '20250619', value: 0},
		{date: '20250620', value: 0},
		{date: '20250621', value: 0},
		{date: '20250622', value: 0},
		{date: '20250623', value: 0},
		{date: '20250624', value: 0},
		{date: '20250625', value: 0},
		{date: '20250626', value: 0},
		{date: '20250627', value: 0},
		{date: '20250628', value: 0},
		{date: '20250629', value: 0},
		{date: '20250630', value: 0},
		{date: '20250631', value: 0},
	];

	const config = {
		data,
		xField: 'date',
		yField: 'value',
		style: {
			opacity: 0.6,
		},
		axis: {
			x: {
				line: true,
				arrow: false,
				labelFontSize: 12,
				labelAutoRotate: false,
				labelAutoHide: true,
			},
			y: {
				line: true,
				arrow: false,
			}
		},
		tooltip: {
			items: [{name: '设备消息数量', channel: 'y'}]
		}
	};
	return <Area {...config} />;
};
export default DeviceMessageArea;

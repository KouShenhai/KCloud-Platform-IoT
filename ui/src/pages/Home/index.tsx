import React from "react";
import {ProCard, ProFormDateRangePicker} from '@ant-design/pro-components';
import {Badge, Col, Row, Space} from "antd";
import './index.less'
import DeviceMessageArea from "@/pages/Home/DeviceMessage";
import { Map } from 'react-amap';
const HomePage: React.FC = () => {
	return (
		<>
			<Row gutter={24}>
				<Col span={6}>
					<ProCard hoverable layout="default" bordered className={'pro-card-1'}>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}><Space>产品数量</Space></div>
							<div className={'pro-card-1-content'}><Space>10</Space></div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={7}>
									<Space><Badge status="success"/> 启用</Space>
								</Col>
								<Col span={5}>
									8
								</Col>
								<Col span={7}>
									<Space><Badge status="error"/> 禁用</Space>
								</Col>
								<Col span={5}>
									2
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard hoverable layout="default" bordered className={'pro-card-1'}>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}><Space>设备数量</Space></div>
							<div className={'pro-card-1-content'}><Space>5</Space></div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={6}>
									<Space><Badge status="success"/> 在线</Space>
								</Col>
								<Col span={6}>
									3
								</Col>
								<Col span={6}>
									<Space><Badge status="error"/> 离线</Space>
								</Col>
								<Col span={6}>
									2
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard hoverable layout="default" bordered className={'pro-card-1'}>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}><Space>今日设备消息数量</Space></div>
							<div className={'pro-card-1-content'}><Space>10000</Space></div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={17}>
									<Space>本月设备消息数量</Space>
								</Col>
								<Col span={7}>
									1000000
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard hoverable layout="default" bordered className={'pro-card-1'}>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}><Space>今日设备告警数量</Space></div>
							<div className={'pro-card-1-content'}><Space>200</Space></div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={17}>
									<Space>本月设备告警数量</Space>
								</Col>
								<Col span={7}>
									50000
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
			</Row>
			<Row gutter={24}>
				<Col span={24}>
					<ProCard layout="center" hoverable bordered className={'pro-card-2'} headerBordered
							 title={'设备消息'} extra={
						<>
							<ProFormDateRangePicker allowClear={false}/>
						</>
					}>
						<DeviceMessageArea/>
					</ProCard>
				</Col>
			</Row>
			<Row gutter={24}>
				<Col span={24}>
					<ProCard layout="center" hoverable bordered className={'pro-card-3'} headerBordered
							 title={'设备分布'}>
						<Map amapkey={'1cab232de3d4891f17d3ce3cadf99dcc'}/>
					</ProCard>
				</Col>
			</Row>
		</>
	);
};

export default HomePage;

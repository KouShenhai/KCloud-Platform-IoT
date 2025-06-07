import React from "react";
import {ProCard} from '@ant-design/pro-components';
import {Badge, Col, Row, Space} from "antd";
import './index.less'

const HomePage: React.FC = () => {
	return (
		<>
			<Row gutter={24}>
				<Col span={6}>
					<ProCard layout="default" bordered className={'pro-card'}>
						<div className={'pro-card-header'}>
							<div className={'pro-card-title'}><Space>产品数量</Space></div>
							<div className={'pro-card-content'}><Space>10</Space></div>
						</div>
						<div className={'pro-card-footer'}>
							<Row gutter={24}>
								<Col span={7}>
									<Space><Badge status="success" /> 启用</Space>
								</Col>
								<Col span={5}>
									8
								</Col>
								<Col span={7}>
									<Space><Badge status="error" /> 禁用</Space>
								</Col>
								<Col span={5}>
									2
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard layout="default" bordered className={'pro-card'}>
						<div className={'pro-card-header'}>
							<div className={'pro-card-title'}><Space>设备数量</Space></div>
							<div className={'pro-card-content'}><Space>5</Space></div>
						</div>
						<div className={'pro-card-footer'}>
							<Row gutter={24}>
								<Col span={6}>
									<Space><Badge status="success" /> 在线</Space>
								</Col>
								<Col span={6}>
									3
								</Col>
								<Col span={6}>
									<Space><Badge status="error" /> 离线</Space>
								</Col>
								<Col span={6}>
									2
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard layout="default" bordered className={'pro-card'}>
						<div className={'pro-card-header'}>
							<div className={'pro-card-title'}><Space>今日设备消息数量</Space></div>
							<div className={'pro-card-content'}><Space>10000</Space></div>
						</div>
						<div className={'pro-card-footer'}>
							<Row gutter={24}>
								<Col span={18}>
									<Space>本月设备消息数量</Space>
								</Col>
								<Col span={6}>
									3
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard layout="default" bordered className={'pro-card'}>
						<div className={'pro-card-header'}>
							<div className={'pro-card-title'}><Space>今日设备告警数量</Space></div>
							<div className={'pro-card-content'}><Space>200</Space></div>
						</div>
						<div className={'pro-card-footer'}>
							<Row gutter={24}>
								<Col span={18}>
									<Space>本月设备告警数量</Space>
								</Col>
								<Col span={6}>
									3
								</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
			</Row>
		</>
	);
};

export default HomePage;

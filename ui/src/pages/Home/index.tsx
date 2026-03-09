import DeviceMessageArea from '@/pages/Home/DeviceMessage';
import { ProCard, ProFormDateRangePicker } from '@ant-design/pro-components';
import { getIntl } from '@@/exports';
import { Badge, Col, Row, Space } from 'antd';
import React from 'react';
import { Map } from 'react-amap';
import './index.less';

const t = (id: string, values?: Record<string, any>) => {
	return getIntl().formatMessage({ id }, values);
};

const HomePage: React.FC = () => {
	return (
		<>
			<Row gutter={24}>
				<Col span={6}>
					<ProCard
						hoverable
						layout="default"
						bordered
						className={'pro-card-1'}
					>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}>
								<Space>{t('home.productCount')}</Space>
							</div>
							<div className={'pro-card-1-content'}>
								<Space>10</Space>
							</div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={7}>
									<Space>
										<Badge status="success" /> {t('common.enable')}
									</Space>
								</Col>
								<Col span={5}>8</Col>
								<Col span={7}>
									<Space>
										<Badge status="error" /> {t('common.disable')}
									</Space>
								</Col>
								<Col span={5}>2</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard
						hoverable
						layout="default"
						bordered
						className={'pro-card-1'}
					>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}>
								<Space>{t('home.deviceCount')}</Space>
							</div>
							<div className={'pro-card-1-content'}>
								<Space>5</Space>
							</div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={7}>
									<Space>
										<Badge status="success" /> {t('home.online')}
									</Space>
								</Col>
								<Col span={5}>3</Col>
								<Col span={7}>
									<Space>
										<Badge status="error" /> {t('home.offline')}
									</Space>
								</Col>
								<Col span={5}>2</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard
						hoverable
						layout="default"
						bordered
						className={'pro-card-1'}
					>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}>
								<Space>{t('home.todayDeviceMessageCount')}</Space>
							</div>
							<div className={'pro-card-1-content'}>
								<Space>10000</Space>
							</div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={17}>
									<Space>{t('home.monthDeviceMessageCount')}</Space>
								</Col>
								<Col span={7}>1000000</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
				<Col span={6}>
					<ProCard
						hoverable
						layout="default"
						bordered
						className={'pro-card-1'}
					>
						<div className={'pro-card-1-header'}>
							<div className={'pro-card-1-title'}>
								<Space>{t('home.todayDeviceAlarmCount')}</Space>
							</div>
							<div className={'pro-card-1-content'}>
								<Space>200</Space>
							</div>
						</div>
						<div className={'pro-card-1-footer'}>
							<Row gutter={24}>
								<Col span={17}>
									<Space>{t('home.monthDeviceAlarmCount')}</Space>
								</Col>
								<Col span={7}>50000</Col>
							</Row>
						</div>
					</ProCard>
				</Col>
			</Row>
			<Row gutter={24}>
				<Col span={24}>
					<ProCard
						layout="center"
						hoverable
						bordered
						className={'pro-card-2'}
						headerBordered
						title={t('home.deviceMessage')}
						extra={
							<>
								<ProFormDateRangePicker allowClear={false} />
							</>
						}
					>
						<DeviceMessageArea />
					</ProCard>
				</Col>
			</Row>
			<Row gutter={24}>
				<Col span={24}>
					<ProCard
						layout="center"
						hoverable
						bordered
						className={'pro-card-3'}
						headerBordered
						title={t('home.deviceDistribution')}
					>
						<Map amapkey={'1cab232de3d4891f17d3ce3cadf99dcc'} />
					</ProCard>
				</Col>
			</Row>
		</>
	);
};

export default HomePage;

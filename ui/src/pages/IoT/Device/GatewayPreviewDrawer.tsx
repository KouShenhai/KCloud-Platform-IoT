import { useIntl } from '@@/exports';
import {
	Button,
	Descriptions,
	Drawer,
} from 'antd';
import { useEffect } from 'react';

type GatewayPreviewProps = {
	id: number;
	canReadProperties: boolean;
	canSendCommand: boolean;
	onClose: () => void;
};

export const GatewayPreviewDrawer = ({
	id,
	onClose,
}: GatewayPreviewProps) => {
	const intl = useIntl();
	const t = (id: string) => intl.formatMessage({ id });

	useEffect(() => {

	});

	return (
		<Drawer
			open
			title={t('iot.gateway.preview.title')}
			size={800}
			closable={false}
			keyboard={false}
			styles={{ footer: { textAlign: 'right' } }}
			footer={<Button onClick={onClose}>{t('common.close')}</Button>}
		>
			<Descriptions
				size="small"
				column={{ xs: 1, sm: 3 }}
				items={[
					{
						key: 'sn',
						label: t('iot.gateway.sn'),
						children: "22" || '—',
					},
					{
						key: 'name',
						label: t('iot.gateway.name'),
						children: "333" || '—',
					},
					{
						key: 'state',
						label: t('iot.gateway.state'),
						children: "333" || '—',
					},
				]}
			/>
{/*
			<Tabs items={tabs} />
*/}
		</Drawer>
	);
};

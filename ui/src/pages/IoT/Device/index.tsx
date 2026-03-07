import { useIntl } from '@@/exports';
import { useEffect, useState } from 'react';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	const [inputMessage, setInputMessage] = useState('');
	let ws: WebSocket;

	// 初始化 WebSocket
	useEffect(() => {
		// 创建 WebSocket 连接（替换为你的后端地址）
		ws = new WebSocket('ws://gateway:5555/iot-websocket/ws');

		// 监听连接打开
		ws.onopen = () => {
			console.log(t('iot.device.ws.connected'));
		};

		// 接收消息
		ws.onmessage = (event) => {
			const newMessage = event.data;
			console.log(newMessage);
		};

		// 监听错误
		ws.onerror = (error) => {
			console.error(t('iot.device.ws.error'), error);
		};

		// 清理函数：组件卸载时关闭连接
		return () => {
			if (ws.readyState === WebSocket.OPEN) {
				ws.close();
			}
		};
	}, []);

	// 发送消息
	const sendMessage = () => {
		if (inputMessage.trim() && ws.readyState === WebSocket.OPEN) {
			ws.send(inputMessage);
			setInputMessage('');
		}
	};

	return (
		<div>
			<div>
				<input
					type="text"
					value={inputMessage}
					onChange={(e) => setInputMessage(e.target.value)}
				/>
				{/* eslint-disable-next-line react/button-has-type */}
				<button onClick={sendMessage}>{t('iot.device.send')}</button>
			</div>
		</div>
	);
};

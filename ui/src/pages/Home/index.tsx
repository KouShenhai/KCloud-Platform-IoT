import Guide from '@/components/Guide';
import {trim} from '@/utils/format';
import {PageContainer} from '@ant-design/pro-components';
import {useModel} from '@umijs/max';
import styles from './index.less';
import React from "react";
import {getAccessToken} from "@/access";

const HomePage: React.FC = () => {
	// 创建websocket连接
	const initSocket = () => {
		const socket = new WebSocket("wss://gateway:10001/ws")
		// 建立连接
		socket.onopen = () => {
			console.log("websocket建立连接")
			const accessToken = getAccessToken();
			if (accessToken) {
				socket.send(`Bearer ${accessToken}`)
			}
		}
		// 接收数据
		socket.onmessage = evt => {
			console.log(evt)
		}
		// 连接错误
		socket.onerror = evt => {
			console.log("websocket连接错误",evt)
		}
		// 关闭连接
		socket.onclose = evt => {
			console.log("websocket关闭连接", evt)
		}
	}
	initSocket()
	const {name} = useModel('global');
	return (
		<PageContainer ghost>
			<div className={styles.container}>
				<Guide name={trim(name)}/>
			</div>
		</PageContainer>
	);
};

export default HomePage;

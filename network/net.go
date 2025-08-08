package network

/*
 * Netplan 配置结构体
 * 用于描述网络接口的配置，包括 DHCP、静态 IP、网关、DNS等信息
 * 适用于 Ubuntu 等使用 Netplan 的 Linux 发行版

 * 字段说明

	| 字段          	| 说明                                         			|
	| ------------- | ----------------------------------------------------- |
	| `version`   	| 必须为 `2`（Netplan 配置版本）                      		|
	| `renderer`  	| 指定使用哪个后端，常见有 `networkd` 或 `NetworkManager` 	|
	| `ethernets` 	| 配置物理网卡                                     		|
	| `vlans`     	| 配置 VLAN 接口                                 		|
	| `bonds`     	| 配置绑定（Bonding）                              		|
	| `bridges`   	| 配置桥接接口                                     		|
	| `wifis`     	| 配置无线接口（需 `NetworkManager` 支持）              	|

 * Ethernet 参数配置示例：

	| 参数                       | 类型   	| 示例                   		| 说明              												|
	| ------------------------- | --------- | ----------------------------- | ------------------------------------------------------------- |
	| `dhcp4`                 	| bool 		| `true` / `false`     			| 是否启用 IPv4 DHCP  											|
	| `dhcp6`                 	| bool 		| `true` / `false`     			| 是否启用 IPv6 DHCP  											|
	| `addresses`             	| 列表   	| `[192.168.1.100/24]` 			| 分配静态 IP 地址和子网掩码 										|
	| `gateway4`              	| 字符串  	| `192.168.1.1`        			| IPv4 默认网关       											|
	| `gateway6`              	| 字符串  	| `fe80::1`            			| IPv6 默认网关       											|
	| `nameservers.addresses` 	| 列表   	| `[8.8.8.8, 1.1.1.1]` 			| DNS 服务器列表       											|
	| `nameservers.search`    	| 列表  		| `[example.com]`      			| DNS 搜索域列表       											|
	| `optional`              	| bool 		| `true`                		| 设置为可选（不影响系统启动）										|
	| `macaddress`            	| 字符串  	| `aa:bb:cc:dd:ee:ff` 			| 指定网卡的 MAC 地址    											|
	| `mtu`                   	| 整数   	| `1500`               			| 设置 MTU 值        											|
	| `routes.to`               | 列表   	| `10.0.0.0/8`                  | 目标网段     		        									|
	| `routes.via`              | 列表   	| `10.0.0.0/8`                  | 网关     		            									|
	| `routes.to`               | 列表   	| `10.0.0.0/8`                  | 路由优先级，数字越小优先级越高。多个匹配路径中会优先使用 metric 较低的	|

 * Wifi 参数配置示例：

*/

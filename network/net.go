/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package main

import (
	"errors"
	"os"
	"os/exec"
	"runtime"
	"strings"

	"gopkg.in/yaml.v3"
)

/*

 * @author laokou
 * @date 2025/08/08 12:30

 * Netplan 配置结构体
 * 用于描述网络接口的配置，包括 DHCP、静态 IP、网关、DNS等信息
 * 适用于 Ubuntu 等使用 Netplan 的 Linux 发行版

 * 字段说明

	| 字段          	| 说明                                         			|
	| ------------- | ----------------------------------------------------- |
	| `version`   	| 必须为 `2`（Netplan 配置版本）                      		|
	| `renderer`  	| 指定使用哪个后端，常见有 `networkd` 或 `NetworkManager` 	|
	| `ethernets` 	| 配置物理网卡                                     		|
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
	| `routes.via`              | 列表   	| `192.168.1.1`                 | 下一跳地址（gateway），数据包将被发送到这个网关去转发  				|
	| `routes.metric`           | 列表   	| `100`                  		| 路由优先级，数字越小优先级越高。多个匹配路径中会优先使用 metric 较低的	|

 * Wifi 参数配置示例：

	| 参数                     			| 类型    	| 示例            					| 说明                        									|
	| --------------------------------- | --------- | --------------------------------- | ------------------------------------------------------------- |
	| `dhcp4`                 			| boolean 	| `true` / `false`     				| 是否启用 IPv4 的 DHCP          									|
	| `dhcp6`                 			| boolean 	| `true` / `false`     				| 是否启用 IPv6 的 DHCP          									|
	| `optional`              			| boolean 	| `true`               				| 将接口标记为“非关键”，启动时不阻塞									|
	| `addresses`             			| list    	| `[192.168.1.100/24]` 				| 分配静态 IP 地址              									|
	| `gateway4`              			| string  	| `192.168.1.1`        				| 设置 IPv4 默认网关              								|
	| `gateway6`              			| string  	| `fe80::1`            				| 设置 IPv6 默认网关              								|
	| `nameservers.addresses` 			| list    	| `[8.8.8.8, 1.1.1.1]` 				| 设置 DNS 服务器             									|
	| `macaddress`            			| string  	| `aa:bb:cc:dd:ee:ff`  				| 设置 MAC 地址（MAC Spoofing）   								|
	| `routes.to`               		| 列表   	| `10.0.0.0/8`          			| 目标网段     		        									|
	| `routes.via`              		| 列表   	| `192.168.1.1`         			| 下一跳地址（gateway），数据包将被发送到这个网关去转发  				|
	| `routes.metric`           		| 列表   	| `100`                 			| 路由优先级，数字越小优先级越高。多个匹配路径中会优先使用 metric 较低的	|
	| `access-points.<SSID>.password` 	| string  	| `"12345678"`                   	| Wi-Fi 密码，支持 WPA/WPA2         								|
	| `access-points.<SSID>.mode`     	| string  	| `"infrastructure"` / `"adhoc"` 	| 连接模式（基础设施或点对点）              							|
	| `access-points.<SSID>.bssid`    	| string  	| `aa:bb:cc:dd:ee:ff`            	| 连接特定 AP 的 MAC 地址（仅在多 AP 场景用） 						|
	| `access-points.<SSID>.band`     	| string  	| `"2.4GHz"` / `"5GHz"`          	| 频段（可选）                       								|
	| `access-points.<SSID>.channel` 	| int     	| `6`                            	| 频道（仅用于 Ad-Hoc）               								|
	| `access-points.<SSID>.hidden`   	| boolean 	| `true`                         	| SSID 是否为隐藏 Wi-Fi             								|

*/

const (
	// 默认网络配置文件路径
	DEFAULT_NETPLAN_CONFIG_PATH = "/etc/netplan/01-network-manager-all.yaml"
	// LAN1 网口1
	LAN1 = "eth0"
	// LAN2 网口2
	LAN2 = "eth1"
)

type NetworkConfig struct {
	Address    string `json:"address"`
	Gateway    string `json:"gateway"`
	Dns        string `json:"dns"`
	MacAddress string `json:"macAddress"`
}

type NetPlanConfig struct {
	Network struct {
		// Version 必须为 2，表示 Netplan 配置的版本
		Version int8 `yaml:"version,omitempty"`
		// Renderer 指定使用哪个后端，常见有 networkd 或 NetworkManager
		// NetworkManager 适用于桌面环境
		// networkd 适用于服务器环境
		Renderer string `yaml:"renderer,omitempty"`
		// Ethernets 是一个 map，键为网卡名称，值为 Ethernet 配置
		// 例如：`"eth0": { ... }`
		// 这里的网卡名称可以是物理网卡名或虚拟网卡名
		// 例如：`"eth0"`、`"eth1"`、`"wlan0"` 等
		Ethernets map[string]*Ethernet `yaml:"ethernets,omitempty"`
		// 如果需要配置无线网卡，请使用 `wifis` 字段
		Wifis map[string]*Wifi `yaml:"wifis,omitempty"`
	} `yaml:"network,omitempty"`
}

// Wifi 无线网络接口配置结构体【Wi-Fi】
type Wifi struct {
	// DHCP4 和 DHCP6 分别表示是否启用 IPv4 和 IPv6 的 DHCP
	Dhcp4 bool `yaml:"dhcp4"`
	Dhcp6 bool `yaml:"dhcp6,omitempty"`
	// Optional 表示该接口是否为可选的，设置为 true 时启动时
	Optional bool `yaml:"optional,omitempty"`
	// Address 配置 IP 地址
	Addresses []string `yaml:"addresses,omitempty"`
	// Gateway4 和 Gateway6 分别表示 IPv4 和 IPv6 的默认网关
	Gateway4 string `yaml:"gateway4,omitempty"`
	Gateway6 string `yaml:"gateway6,omitempty"`
	// Nameservers 包含 DNS 服务器地址
	Nameservers struct {
		// Addresses 是 DNS 服务器地址列表
		Addresses []string `yaml:"addresses,omitempty"`
	} `yaml:"nameservers,omitempty"`
	// MacAddress 是网卡的 MAC 地址
	MacAddress string `yaml:"macaddress,omitempty"`
	// Routes 是一个路由列表，每个路由包含目标地址、网关和优先级
	Routes Route `yaml:"routes,omitempty"`
	// AccessPoints 是一个 map，键为 SSID，值为 AccessPoint 配置
	AccessPoints map[string]AccessPoint `yaml:"access-points,omitempty"`
}

// Ethernet 网卡结构体【以太网】
type Ethernet struct {
	// DHCP4 和 DHCP6 分别表示是否启用 IPv4 和 IPv6 的 DHCP
	DHCP4 bool `yaml:"dhcp4"`
	DHCP6 bool `yaml:"dhcp6,omitempty"`
	// Addresses 是一个字符串数组，表示分配的静态 IP 地址和子网掩码
	Addresses []string `yaml:"addresses,omitempty"`
	// Gateway4 和 Gateway6 分别表示 IPv4 和 IPv6 的默认网关
	Gateway4 string `yaml:"gateway4,omitempty"`
	Gateway6 string `yaml:"gateway6,omitempty"`
	// Nameservers 包含 DNS 服务器地址和搜索域
	Nameservers struct {
		// Addresses 是 DNS 服务器地址列表
		Addresses []string `yaml:"addresses,omitempty"`
		// Search 是 DNS 搜索域列表
		Search []string `yaml:"search,omitempty"`
	} `yaml:"nameservers,omitempty"`
	// Routes 是一个路由列表，每个路由包含目标地址、网关和优先级
	Routes Route `yaml:"routes,omitempty"`
	// Optional 表示该接口是否为可选的，设置为 true 时启动时不会阻塞
	Optional bool `yaml:"optional,omitempty"`
	// MacAddress 是网卡的 MAC 地址
	MacAddress string `yaml:"macaddress,omitempty"`
	// Mtu 是网卡的 MTU
	Mtu int `yaml:"mtu,omitempty"`
}

// Route 路由
type Route struct {
	// To 是目标地址或网段
	To string `yaml:"to,omitempty"`
	// Via 是路由的网关地址
	Via string `yaml:"via,omitempty"`
	// Metric 是路由的优先级，数字越小优先级越高
	Metric int `yaml:"metric,omitempty"`
}

// AccessPoint 无线接入点配置结构体
type AccessPoint struct {
	// Mode 是连接模式，可能的值有 "infrastructure"（基础设施模式）或 "adhoc"（点对点模式）
	Mode string `yaml:"mode,omitempty"`
	// Password 是 Wi-Fi 密码，支持 WPA/WPA2
	Password string `yaml:"password,omitempty"`
	// Bssid 是接入点的 MAC 地址，通常用于连接特定的 AP
	Bssid string `yaml:"bssid,omitempty"`
	// Band 是无线频段，可能的值有 "2.4GHz" 或 "5GHz"
	Band string `yaml:"band,omitempty"`
	// Channel 是无线频道，通常用于 Ad-Hoc 模式
	Channel int `yaml:"channel,omitempty"`
	// Hidden 是是否隐藏 SSID，通常用于 Ad-Hoc 模式
	Hidden bool `yaml:"hidden,omitempty"`
}

func NewDefaultNetPlanConfig() *NetPlanConfig {
	netConfig := &NetPlanConfig{}
	netConfig.Network.Version = 2
	netConfig.Network.Renderer = "NetworkManager"
	// 两个网口
	netConfig.Network.Ethernets = make(map[string]*Ethernet, 2)
	netConfig.Network.Ethernets[LAN1] = &Ethernet{}
	netConfig.Network.Ethernets[LAN2] = &Ethernet{}
	return netConfig
}

func SaveNetPlanConfig(buf []byte, path string) error {
	err := os.WriteFile(path, buf, 0644)
	if err != nil {
		return errors.New("写入网络配置文件失败，错误信息：" + err.Error())
	}
	return nil
}

func ApplyNetPlanConfig() error {
	err := validateOS()
	if err != nil {
		return err
	}
	err = exec.Command("netplan", "apply").Run()
	if err != nil {
		return errors.New("应用网络配置失败，错误信息：" + err.Error())
	}
	return nil
}

func ReadNetPlanConfig(path string) (*NetPlanConfig, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, errors.New("读取网络配置文件失败，错误信息：" + err.Error())
	}
	netConfig := &NetPlanConfig{}
	err = yaml.Unmarshal(data, netConfig)
	if err != nil {
		return nil, errors.New("网络配置反序列化失败，错误信息：" + err.Error())
	}
	return netConfig, nil
}

func GetMacAddress() (string, error) {
	err := validateOS()
	if err != nil {
		return "", err
	}
	output, err := exec.Command("sh", "-c", `ip link show eth0 | awk '/ether/ {print $2}'`).Output()
	if err != nil {
		return "", errors.New("获取MAC地址失败，错误信息：" + err.Error())
	}
	return strings.TrimSpace(string(output)), nil
}

func GetIpAddress() (string, error) {
	err := validateOS()
	if err != nil {
		return "", err
	}
	output, err := exec.Command("sh", "-c", `ip addr show eth0 | grep "inet " | awk '{print $2}' | cut -d'/' -f1`).Output()
	if err != nil {
		return "", errors.New("获取IP地址失败，错误信息：" + err.Error())
	}
	return strings.TrimSpace(string(output)), nil
}

func GetGateway() (string, error) {
	err := validateOS()
	if err != nil {
		return "", err
	}
	output, err := exec.Command("sh", "-c", `ip route | grep default | grep eth0 | awk '{print $3}'`).Output()
	if err != nil {
		return "", errors.New("获取网关失败，错误信息：" + err.Error())
	}
	return strings.TrimSpace(string(output)), nil
}

func GetNetmask() (string, error) {
	err := validateOS()
	if err != nil {
		return "", err
	}
	output, err := exec.Command("sh", "-c", `ip addr show eth0 | grep "inet " | awk '{print $2}' | cut -d'/' -f2`).Output()
	if err != nil {
		return "", errors.New("获取子网掩码长度失败，错误信息：" + err.Error())
	}
	return strings.TrimSpace(string(output)), nil
}

func GetNetworkConfig(path string) (*NetworkConfig, error) {
	config, err := getNetPlanConfig(path)
	if err != nil {
		return nil, err
	}
	ethernet := config.Network.Ethernets[LAN1]
	return &NetworkConfig{
		Address:    ethernet.Addresses[0],
		Gateway:    ethernet.Gateway4,
		Dns:        strings.Join(ethernet.Nameservers.Addresses, ","),
		MacAddress: ethernet.MacAddress,
	}, nil
}

func getNetPlanConfig(path string) (*NetPlanConfig, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, errors.New("获取网络配置失败，错误信息：" + err.Error())
	}
	netConfig := &NetPlanConfig{}
	err = yaml.Unmarshal(data, netConfig)
	return netConfig, nil
}

func validateOS() error {
	if runtime.GOOS != "linux" {
		return errors.New("网关在 " + runtime.GOOS + " 操作系统上不支持该操作")
	}
	return nil
}

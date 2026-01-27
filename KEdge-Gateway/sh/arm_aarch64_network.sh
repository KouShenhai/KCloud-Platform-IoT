#!/bin/bash

#/*
# * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
# *
# * Licensed under the Apache License, Version 2.0 (the "License");
# * you may not use this file except in compliance with the License.
# * You may obtain a copy of the License at
# *
# *   http://www.apache.org/licenses/LICENSE-2.0
# *
# * Unless required by applicable law or agreed to in writing, software
# * distributed under the License is distributed on an "AS IS" BASIS,
# * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# * See the License for the specific language governing permissions and
# * limitations under the License.
# *
# */

# author: laokou

ETH0="eth0"
USB0="usb0"
WLAN0="wlan0"

# 启用连接【以太网】
enable_eth0() {
sudo ip link set $ETH0 up
}

# 启用连接【4G】
enable_usb0() {
sudo ip link set $USB0 up
}

# 启用W连接【WIFI】
enable_wlan0() {
sudo ip link set $WLAN0 up
}

# 禁用以太网连接【以太网】
disable_eth0() {
sudo ip link set $ETH0 down
}

# 禁用连接【4G】
disable_usb0() {
sudo ip link set $USB0 down
}

# 禁用连接【WIFI】
disable_wlan0() {
sudo ip link set $WLAN0 down
}

# 连接以太网【DHCP】
connect_eth0_dhcp() {
nohup edge-gateway-network $ETH0 dhcp >/dev/null 2>&1 &
}

# 连接以太网【STATIC】
connect_eth0_static() {
local ipAddr=$2
local ipGateway=$3
local ipDns=$4
nohup edge-gateway-network $ETH0 static "$ipAddr" "$ipGateway" "$ipDns" >/dev/null 2>&1 &
}

# 连接以太网
connect_eth0() {
sudo nmcli device connect $ETH0
}

# 断开以太网连接
disconnect_eth0() {
sudo nmcli device disconnect $ETH0
}

# 连接4G
connect_usb0() {
sudo nmcli device connect $USB0
}

# 断开4G
disconnect_usb0() {
sudo nmcli device disconnect $USB0
}

# 连接WIFI并保存配置
connect_wlan0_to_save() {
# /etc/NetworkManager/system-connections/default-wlan0.nmconnection
# 删除配置文件
sudo rm -f /etc/NetworkManager/system-connections/*wlan*
local ssid=$2
local pwd=$3
sudo nmcli con add type wifi con-name "default-wlan0" ssid "$ssid" wifi-sec.key-mgmt wpa-psk wifi-sec.psk "$pwd"
# sudo nmcli con mod "default-wlan0" ipv4.route-metric 80
# sudo nmcli connection up "default-wlan0"
# sudo nmcli connection reload
}

# 连接WIFI
connect_wlan0() {
sudo nmcli device connect $WLAN0
}

# 断开WIFI连接
disconnect_wlan0() {
sudo nmcli device disconnect $WLAN0
}

# 查看MAC地址【以太网】
show_eth0_mac_addr() {
sudo ip link show $ETH0 | awk '/ether/ {print $2}'
}

# 查看MAC地址【4G】
show_usb0_mac_addr() {
sudo ip link show $USB0 | awk '/ether/ {print $2}'
}

# 查看MAC地址【WIFI】
show_wlan0_mac_addr() {
sudo ip link show $WLAN0 | awk '/ether/ {print $2}'
}

# 查看IP地址【以太网】
show_eth0_ip_addr() {
sudo ip addr show $ETH0 | grep "inet " | awk '{print $2}'
}

# 查看IP地址【4G】
show_usb0_ip_addr() {
sudo ip addr show $USB0 | grep "inet " | awk '{print $2}'
}

# 查看IP地址【WIFI】
show_wlan0_ip_addr() {
sudo ip addr show $WLAN0 | grep "inet " | awk '{print $2}'
}

# 查看IP网关【以太网】
show_eth0_ip_gateway() {
sudo ip route | grep default | grep $ETH0 | awk '{print $3}'
}

# 查看IP网关【4G】
show_usb0_ip_gateway() {
sudo ip route | grep default | grep $USB0 | awk '{print $3}'
}

# 查看IP网关【WIFI】
show_wlan0_ip_gateway() {
sudo ip route | grep default | grep $WLAN0 | awk '{print $3}'
}

# 扫描可用的WIFI网络
scan_wlan0() {
sudo nmcli -f SSID,SIGNAL device wifi list 2>/dev/null | grep -v "^--" | head -20
}

# 查看连接状态【以太网】
show_eth0_connect_status() {
sudo nmcli -t -f DEVICE,STATE device | grep -q "^$ETH0:connected" && echo true || echo false
}

# 查看连接状态【4G】
show_usb0_connect_status() {
sudo nmcli -t -f DEVICE,STATE device | grep -q "^$USB0:connected" && echo true || echo false
}

# 查看连接状态【WIFI】
show_wlan0_connect_status() {
sudo nmcli -t -f DEVICE,STATE device | grep -q "^$WLAN0:connected" && echo true || echo false
}

# 获取以太网类型【DHCP/STATIC】
show_eth0_type() {
if pgrep -a dhclient | grep -q "$ETH0"; then
  echo "dhcp"
else
  echo "static"
fi
}

# 使用路由【以太网】
use_route_eth0() {
sudo nmcli con mod "default-wlan0" ipv4.route-metric 110
sudo nmcli connection up "default-wlan0"
sudo nmcli connection modify "Wired connection 2" ipv4.route-metric 120
sudo nmcli connection up "Wired connection 2"
sudo nmcli connection reload
}

# 使用路由【WIFI】
use_route_wlan0() {
sudo nmcli con mod "default-wlan0" ipv4.route-metric 50
sudo nmcli connection up "default-wlan0"
sudo nmcli connection modify "Wired connection 2" ipv4.route-metric 120
sudo nmcli connection up "Wired connection 2"
sudo nmcli connection reload
}

# 使用路由【4G】
use_route_usb0() {
sudo nmcli con mod "default-wlan0" ipv4.route-metric 110
sudo nmcli connection up "default-wlan0"
sudo nmcli connection modify "Wired connection 2" ipv4.route-metric 50
sudo nmcli connection up "Wired connection 2"
sudo nmcli connection reload
}

case "$1" in
  connect_eth0_dhcp)
    connect_eth0_dhcp
    ;;
  connect_eth0_static)
    connect_eth0_static "$@"
    ;;
  connect_eth0)
  	connect_eth0
  	;;
  disconnect_eth0)
  	disconnect_eth0
  	;;
  connect_usb0)
  	connect_usb0
  	;;
  disconnect_usb0)
  	disconnect_usb0
  	;;
  connect_wlan0_to_save)
    connect_wlan0_to_save "$@"
    ;;
  connect_wlan0)
    connect_wlan0
    ;;
  disconnect_wlan0)
    disconnect_wlan0
    ;;
  show_eth0_ip_gateway)
    show_eth0_ip_gateway
    ;;
  show_usb0_ip_gateway)
    show_usb0_ip_gateway
    ;;
  show_wlan0_ip_gateway)
    show_wlan0_ip_gateway
    ;;
  show_eth0_ip_addr)
    show_eth0_ip_addr
    ;;
  show_usb0_ip_addr)
    show_usb0_ip_addr
    ;;
  show_wlan0_ip_addr)
    show_wlan0_ip_addr
    ;;
  show_eth0_mac_addr)
    show_eth0_mac_addr
    ;;
  show_usb0_mac_addr)
    show_usb0_mac_addr
    ;;
  show_wlan0_mac_addr)
    show_wlan0_mac_addr
    ;;
  show_eth0_connect_status)
    show_eth0_connect_status
    ;;
  show_usb0_connect_status)
    show_usb0_connect_status
    ;;
  show_wlan0_connect_status)
    show_wlan0_connect_status
    ;;
  enable_eth0)
    enable_eth0
    ;;
  enable_usb0)
    enable_usb0
    ;;
  enable_wlan0)
    enable_wlan0
    ;;
  disable_eth0)
    disable_eth0
    ;;
  disable_usb0)
    disable_usb0
    ;;
  disable_wlan0)
    disable_wlan0
    ;;
  scan_wlan0)
    scan_wlan0
    ;;
  show_eth0_type)
    show_eth0_type
    ;;
  use_route_eth0)
  	use_route_eth0
  	;;
  use_route_usb0)
  	use_route_usb0
  	;;
  use_route_wlan0)
  	use_route_wlan0
  	;;
  *)
    exit 1
    ;;
esac

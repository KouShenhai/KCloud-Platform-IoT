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
  nmcli device connect $ETH0
}

# 启用连接【4G】
enable_usb0() {
  nmcli device connect $USB0
}

# 启用W连接【WIFI】
enable_wlan0() {
  nmcli device connect $WLAN0
}

# 禁用以太网连接【以太网】
disable_eth0() {
  nmcli device disconnect $ETH0
}

# 禁用连接【4G】
disable_usb0() {
  nmcli device disconnect $USB0
}

# 禁用连接【WIFI】
disable_wlan0() {
  nmcli device disconnect $WLAN0
}

# 连接以太网【DHCP】
connect_eth0_dhcp() {
  nohup edge-gateway-network $ETH0 dhcp >/dev/null 2>&1 &
  sleep 2
  echo true
}

# 连接以太网【STATIC】
connect_eth0_static() {
  local ipAddr=$2
  local ipGateway=$3
  local ipDns=$4
  nohup edge-gateway-network $ETH0 static "$ipAddr" "$ipGateway" "$ipDns" >/dev/null 2>&1 &
  sleep 2
  echo true
}

# 连接WIFI
connect_wlan0() {

}

# 查看MAC地址【以太网】
show_eth0_mac_addr() {
  ip link show $ETH0 | awk '/ether/ {print $2}'
}

# 查看MAC地址【4G】
show_usb0_mac_addr() {
  ip link show $USB0 | awk '/ether/ {print $2}'
}

# 查看MAC地址【WIFI】
show_wlan0_mac_addr() {
  ip link show $WLAN0 | awk '/ether/ {print $2}'
}

# 查看IP地址【以太网】
show_eth0_ip_addr() {
  ip addr show $ETH0 | grep "inet " | awk '{print $2}'
}

# 查看IP地址【4G】
show_usb0_ip_addr() {
  ip addr show $USB0 | grep "inet " | awk '{print $2}'
}

# 查看IP地址【WIFI】
show_wlan0_ip_addr() {
  ip addr show $WLAN0 | grep "inet " | awk '{print $2}'
}

# 查看IP网关【以太网】
show_eth0_ip_gateway() {
  ip route | grep default | grep $ETH0 | awk '{print $3}'
}

# 查看IP网关【4G】
show_usb0_ip_gateway() {
  ip route | grep default | grep $USB0 | awk '{print $3}'
}

# 查看IP网关【WIFI】
show_wlan0_ip_gateway() {
  ip route | grep default | grep $WLAN0 | awk '{print $3}'
}

# 扫描可用的WIFI网络
scan_wlan0() {

}

# 查看连接状态【以太网】
show_eth0_connect_status() {
  nmcli -t -f DEVICE,STATE device | grep -q "^$ETH0:connected" && echo true || echo false
}

# 查看连接状态【4G】
show_usb0_connect_status() {
  nmcli -t -f DEVICE,STATE device | grep -q "^$USB0:connected" && echo true || echo false
}

# 查看连接状态【WIFI】
show_wlan0_connect_status() {
  nmcli -t -f DEVICE,STATE device | grep -q "^$WLAN0:connected" && echo true || echo false
}

case "$1" in
  connect_eth0_dhcp)
    connect_eth0_dhcp
    ;;
  connect_eth0_static)
    connect_eth0_static "$@"
    ;;
  connect_wlan0)
    connect_wlan0
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
  *)
    exit 1
    ;;
esac

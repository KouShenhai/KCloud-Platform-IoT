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
	"fmt"
	"gopkg.in/yaml.v3"
	"testing"
)

func TestNetPlanConfig(t *testing.T) {
	netConfig := NewDefaultNetPlanConfig()
	netConfig.Network.Ethernets[LAN1].DHCP4 = true
	netConfig.Network.Ethernets[LAN1].MacAddress = "22:03:xz:2f:a2:1a"
	netConfig.Network.Ethernets[LAN2].DHCP4 = false
	netConfig.Network.Ethernets[LAN2].MacAddress = "22:03:xz:2f:a2:1a"
	netConfig.Network.Ethernets[LAN2].Addresses = []string{"100.100.1.10/24"}
	netConfig.Network.Ethernets[LAN2].Gateway4 = "100.100.1.1"
	netConfig.Network.Ethernets[LAN2].Nameservers.Addresses = []string{"8.8.8.8", "114.114.114.114"}
	out, err := yaml.Marshal(netConfig)
	if err != nil {
		fmt.Println(err.Error())
	}
	fmt.Println(string(out))
	err = SaveNetPlanConfig(out, "ethernets.yaml")
	if err != nil {
		fmt.Println(err.Error())
	}
	err = ApplyNetPlanConfig()
	if err != nil {
		fmt.Println(err.Error())
	}
	config, err := ReadNetPlanConfig("ethernets.yaml")
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	fmt.Println(config.Network.Ethernets[LAN1].DHCP4)
	config.Network.Ethernets[LAN1].DHCP4 = false
	config.Network.Ethernets[LAN1].Addresses = []string{"192.168.1.10/24"}
	config.Network.Ethernets[LAN1].Gateway4 = "192.168.1.1"
	config.Network.Ethernets[LAN1].Nameservers.Addresses = []string{"8.8.8.8", "114.114.114.114"}
	out, err = yaml.Marshal(config)
	err = SaveNetPlanConfig(out, "ethernets.yaml")
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	fmt.Println(GetNetmask())
	fmt.Println(GetMacAddress())
	fmt.Println(GetIpAddress())
	fmt.Println(GetGateway())
	networkConfig, err := GetNetworkConfig("ethernets.yaml")
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	fmt.Println(networkConfig.Dns)
	fmt.Println(networkConfig.Gateway)
	fmt.Println(networkConfig.Address)
	fmt.Println(networkConfig.MacAddress)
}

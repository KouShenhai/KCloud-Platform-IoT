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

package core

import (
	"bytes"
	"crypto/tls"
	"encoding/json"
	"io"
	"net/http"
	"strings"
)

func SendRequest(method, url string, param map[string]string, header map[string]string, str string) (*http.Response, error) {
	var request *http.Request
	if http.MethodGet == method {
		if param != nil {
			var s string
			for k, v := range param {
				s += k + "=" + v + "&"
			}
			s, _ = strings.CutSuffix(s, "&")
			url += "?" + s
		}
		request, _ = http.NewRequest(method, url, nil)
	} else if len(param) > 0 {
		marshal, _ := json.Marshal(param)
		request, _ = http.NewRequest(method, url, bytes.NewReader(marshal))
	} else if str != "" {
		request, _ = http.NewRequest(method, url, strings.NewReader(str))
	} else {
		request, _ = http.NewRequest(method, url, nil)
	}
	if header != nil {
		for k, v := range header {
			request.Header.Set(k, v)
		}
	}
	return sendHttpRequest(request)
}

func SendRequestAndGetBody(method, url string, param map[string]string, header map[string]string, str string) ([]byte, error) {
	response, err := SendRequest(method, url, param, header, str)
	if err != nil {
		return nil, err
	}
	return io.ReadAll(response.Body)
}

func sendHttpRequest(request *http.Request) (*http.Response, error) {
	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
		},
	}
	response, err := client.Do(request)
	if err != nil {
		return nil, err
	}
	defer response.Body.Close()
	return response, nil
}

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
	"errors"
	"io"
	"mime/multipart"
	"net/http"
)

func SendRequest(method, url string, param io.Reader, header map[string]string) (*http.Response, error) {
	request, err := http.NewRequest(method, url, param)
	if err != nil {
		return nil, errors.New("创建 Request 失败，错误信息：" + err.Error())
	}
	if header != nil {
		for k, v := range header {
			request.Header.Set(k, v)
		}
	}
	return sendHttpRequest(request)
}

func GetFormFile(fileName string, buf []byte) (io.Reader, string, error) {
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile("file", fileName)
	if err != nil {
		return nil, "", errors.New("创建 FormFile 失败，错误信息：" + err.Error())
	}
	_, err = io.Copy(part, bytes.NewReader(buf))
	if err != nil {
		return nil, "", errors.New("复制字节数组失败，错误信息：" + err.Error())
	}
	err = writer.Close()
	if err != nil {
		return nil, "", errors.New("关闭 Writer 失败，错误信息：" + err.Error())
	}
	return body, writer.FormDataContentType(), nil
}

func SendRequestAndGetBody(method, url string, param io.Reader, header map[string]string) ([]byte, error) {
	response, err := SendRequest(method, url, param, header)
	if err != nil {
		return nil, err
	}
	body, err := io.ReadAll(response.Body)
	if err != nil {
		return nil, errors.New("读取响应失败，错误信息：" + err.Error())
	}
	defer response.Body.Close()
	return body, nil
}

func sendHttpRequest(request *http.Request) (*http.Response, error) {
	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
		},
	}
	response, err := client.Do(request)
	if err != nil {
		return nil, errors.New("发送 HTTP 请求失败，错误信息：" + err.Error())
	}
	return response, nil
}

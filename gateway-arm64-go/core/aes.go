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
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"errors"
	"io"
)

func Encrypt(key, data []byte) ([]byte, error) {
	_cipher, err := aes.NewCipher(key)
	if err != nil {
		return nil, errors.New("创建Cipher失败【AES】，错误信息：" + err.Error())
	}
	gcm, err := cipher.NewGCM(_cipher)
	if err != nil {
		return nil, errors.New("创建GCM模式失败【AES】，错误信息：" + err.Error())
	}
	nonce := make([]byte, gcm.NonceSize())
	if _, err = io.ReadFull(rand.Reader, nonce); err != nil {
		return nil, errors.New("创建随机数失败【AES】，错误信息：" + err.Error())
	}
	return gcm.Seal(nonce, nonce, data, nil), nil
}

func Decrypt(key, data []byte) ([]byte, error) {
	_cipher, err := aes.NewCipher(key)
	if err != nil {
		return nil, errors.New("创建Cipher失败【AES】，错误信息：" + err.Error())
	}
	gcm, err := cipher.NewGCM(_cipher)
	if err != nil {
		return nil, errors.New("创建GCM模式失败【AES】，错误信息：" + err.Error())
	}
	nonceSize := gcm.NonceSize()
	if len(data) < nonceSize {
		return nil, errors.New("数据长度小于NonceSize【AES】")
	}
	nonce, _data := data[:nonceSize], data[nonceSize:]
	__data, err := gcm.Open(nil, nonce, _data, nil)
	if err != nil {
		return nil, errors.New("解密失败【AES】，错误信息：" + err.Error())
	}
	return __data, nil
}

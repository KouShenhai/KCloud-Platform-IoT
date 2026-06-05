/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package model

import "time"

// Role constants for user roles
const (
	RoleAdmin    = "admin"
	RoleOperator = "operator"
)

// Status constants for user account status
const (
	StatusActive   = "active"
	StatusDisabled = "disabled"
)

// User represents an edge gateway user account
type User struct {
	ID           string    `json:"id"            yaml:"id"`
	Username     string    `json:"username"      yaml:"username"`
	PasswordHash string    `json:"-"             yaml:"password_hash"`
	Role         string    `json:"role"          yaml:"role"`
	Status       string    `json:"status"        yaml:"status"`
	CreatedAt    time.Time `json:"created_at"    yaml:"created_at"`
	UpdatedAt    time.Time `json:"updated_at"    yaml:"updated_at"`
}

// SafeUser is a User without sensitive fields for API responses
type SafeUser struct {
	ID        string    `json:"id"`
	Username  string    `json:"username"`
	Role      string    `json:"role"`
	Status    string    `json:"status"`
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
}

// ToSafe converts a User to a SafeUser (strips password hash)
func (u *User) ToSafe() *SafeUser {
	return &SafeUser{
		ID:        u.ID,
		Username:  u.Username,
		Role:      u.Role,
		Status:    u.Status,
		CreatedAt: u.CreatedAt,
		UpdatedAt: u.UpdatedAt,
	}
}

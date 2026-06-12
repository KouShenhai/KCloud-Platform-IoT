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

const (
	MenuStatusEnabled  = "enabled"
	MenuStatusDisabled = "disabled"
)

type Menu struct {
	ID        string    `json:"id"        yaml:"id"`
	Name      string    `json:"name"      yaml:"name"`
	Path      string    `json:"path"      yaml:"path"`
	Icon      string    `json:"icon"      yaml:"icon"`
	ParentID  string    `json:"parentId"  yaml:"parent_id"`
	Sort      int       `json:"sort"      yaml:"sort"`
	Status    string    `json:"status"    yaml:"status"`
	CreatedAt time.Time `json:"createdAt" yaml:"created_at"`
	UpdatedAt time.Time `json:"updatedAt" yaml:"updated_at"`
}

type MenuNode struct {
	ID        string      `json:"id"`
	Name      string      `json:"name"`
	Path      string      `json:"path"`
	Icon      string      `json:"icon"`
	ParentID  string      `json:"parentId"`
	Sort      int         `json:"sort"`
	Status    string      `json:"status"`
	CreatedAt time.Time   `json:"createdAt"`
	UpdatedAt time.Time   `json:"updatedAt"`
	Children  []*MenuNode `json:"children,omitempty"`
}

func (m *Menu) ToNode() *MenuNode {
	return &MenuNode{
		ID:        m.ID,
		Name:      m.Name,
		Path:      m.Path,
		Icon:      m.Icon,
		ParentID:  m.ParentID,
		Sort:      m.Sort,
		Status:    m.Status,
		CreatedAt: m.CreatedAt,
		UpdatedAt: m.UpdatedAt,
	}
}

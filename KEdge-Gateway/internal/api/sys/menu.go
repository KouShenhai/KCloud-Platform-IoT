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

package sys

import (
	"context"
	"errors"
	"net/http"
	"strings"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/repository"
	"github.com/cloudwego/hertz/pkg/app"
)

type menuRequest struct {
	Name     string `json:"name"`
	Path     string `json:"path"`
	Icon     string `json:"icon"`
	ParentID string `json:"parentId"`
	Sort     int    `json:"sort"`
	Status   string `json:"status"`
}

type menuStatusRequest struct {
	Status string `json:"status"`
}

func ListMenus(ctx context.Context, c *app.RequestContext) {
	keyword := string(c.Query("keyword"))
	tree := strings.EqualFold(string(c.Query("tree")), "true")

	if tree {
		nodes, total := repository.DefaultMenuRepository.TreeNodes(repository.MenuListParams{Keyword: keyword})
		respondOK(c, map[string]interface{}{
			"list":  nodes,
			"total": total,
		})
		return
	}

	result := repository.DefaultMenuRepository.List(repository.MenuListParams{Keyword: keyword})
	respondOK(c, map[string]interface{}{
		"list":  result.Menus,
		"total": result.Total,
	})
}

func GetMenu(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	menu, err := repository.DefaultMenuRepository.GetByID(id)
	if err != nil {
		if errors.Is(err, repository.ErrMenuNotFound) {
			respondError(c, http.StatusNotFound, "menu not found")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to get menu")
		return
	}
	respondOK(c, menu)
}

func CreateMenu(ctx context.Context, c *app.RequestContext) {
	var req menuRequest
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}
	if !validateMenuRequest(req) {
		respondError(c, http.StatusBadRequest, "invalid menu fields")
		return
	}

	menu, err := repository.DefaultMenuRepository.Create(repository.CreateMenuParams{
		Name:     strings.TrimSpace(req.Name),
		Path:     strings.TrimSpace(req.Path),
		Icon:     strings.TrimSpace(req.Icon),
		ParentID: strings.TrimSpace(req.ParentID),
		Sort:     req.Sort,
		Status:   defaultRequestMenuStatus(req.Status),
	})
	if err != nil {
		respondMenuRepositoryError(c, err)
		return
	}
	respondCreated(c, menu)
}

func UpdateMenu(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	var req menuRequest
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}
	if !validateMenuRequest(req) {
		respondError(c, http.StatusBadRequest, "invalid menu fields")
		return
	}

	menu, err := repository.DefaultMenuRepository.Update(id, repository.UpdateMenuParams{
		Name:     strings.TrimSpace(req.Name),
		Path:     strings.TrimSpace(req.Path),
		Icon:     strings.TrimSpace(req.Icon),
		ParentID: strings.TrimSpace(req.ParentID),
		Sort:     req.Sort,
		Status:   defaultRequestMenuStatus(req.Status),
	})
	if err != nil {
		respondMenuRepositoryError(c, err)
		return
	}
	respondOK(c, menu)
}

func DeleteMenu(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	if err := repository.DefaultMenuRepository.Delete(id); err != nil {
		respondMenuRepositoryError(c, err)
		return
	}
	c.Status(http.StatusNoContent)
}

func UpdateMenuStatus(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	var req menuStatusRequest
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}
	if !validateMenuStatus(req.Status) {
		respondError(c, http.StatusBadRequest, "status must be 'enabled' or 'disabled'")
		return
	}

	menu, err := repository.DefaultMenuRepository.UpdateStatus(id, req.Status)
	if err != nil {
		respondMenuRepositoryError(c, err)
		return
	}
	respondOK(c, menu)
}

func validateMenuRequest(req menuRequest) bool {
	name := strings.TrimSpace(req.Name)
	path := strings.TrimSpace(req.Path)
	icon := strings.TrimSpace(req.Icon)
	if name == "" || len(name) > 64 {
		return false
	}
	if path == "" || len(path) > 128 || !strings.HasPrefix(path, "/") {
		return false
	}
	if len(icon) > 64 {
		return false
	}
	if req.Sort < 0 {
		return false
	}
	return validateMenuStatus(defaultRequestMenuStatus(req.Status))
}

func validateMenuStatus(status string) bool {
	return status == model.MenuStatusEnabled || status == model.MenuStatusDisabled
}

func defaultRequestMenuStatus(status string) string {
	if strings.TrimSpace(status) == "" {
		return model.MenuStatusEnabled
	}
	return strings.TrimSpace(status)
}

func respondMenuRepositoryError(c *app.RequestContext, err error) {
	switch {
	case errors.Is(err, repository.ErrMenuNotFound):
		respondError(c, http.StatusNotFound, "menu not found")
	case errors.Is(err, repository.ErrMenuPathExists):
		respondError(c, http.StatusConflict, "menu path already exists")
	case errors.Is(err, repository.ErrMenuParentNotFound):
		respondError(c, http.StatusBadRequest, "parent menu not found")
	case errors.Is(err, repository.ErrMenuCircularParent):
		respondError(c, http.StatusBadRequest, "menu parent cannot point to itself or a descendant")
	case errors.Is(err, repository.ErrMenuHasChildren):
		respondError(c, http.StatusBadRequest, "menu has child menus")
	default:
		respondError(c, http.StatusInternalServerError, "menu operation failed")
	}
}

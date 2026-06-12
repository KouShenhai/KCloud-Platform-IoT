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
	"regexp"
	"strconv"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/repository"
	"github.com/cloudwego/hertz/pkg/app"
	"golang.org/x/crypto/bcrypt"
)

// ---------------------------------------------------------------------------
// Request / Response helpers
// ---------------------------------------------------------------------------

func respondOK(c *app.RequestContext, data interface{}) {
	c.JSON(http.StatusOK, map[string]interface{}{
		"code":    200,
		"message": "success",
		"data":    data,
	})
}

func respondCreated(c *app.RequestContext, data interface{}) {
	c.JSON(http.StatusCreated, map[string]interface{}{
		"code":    201,
		"message": "created",
		"data":    data,
	})
}

func respondError(c *app.RequestContext, statusCode int, message string) {
	c.JSON(statusCode, map[string]interface{}{
		"code":    statusCode,
		"message": message,
	})
}

// ---------------------------------------------------------------------------
// Validation helpers
// ---------------------------------------------------------------------------

func validateUsername(name string) bool {
	ok, _ := regexp.MatchString(`^[A-Za-z0-9_]{3,32}$`, name)
	return ok
}

func validatePassword(pw string) bool {
	return len(pw) >= 8
}

func validateRole(role string) bool {
	return role == model.RoleAdmin || role == model.RoleOperator
}

func validateStatus(status string) bool {
	return status == model.StatusActive || status == model.StatusDisabled
}

// ---------------------------------------------------------------------------
// Handlers
// ---------------------------------------------------------------------------

// CreateUser handles POST /api/v1/users
func CreateUser(ctx context.Context, c *app.RequestContext) {
	var req struct {
		Username string `json:"username"`
		Password string `json:"password"`
		Role     string `json:"role"`
	}
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}

	if !validateUsername(req.Username) {
		respondError(c, http.StatusBadRequest, "username must be 3-32 characters")
		return
	}
	if !validatePassword(req.Password) {
		respondError(c, http.StatusBadRequest, "password must be at least 8 characters")
		return
	}
	if !validateRole(req.Role) {
		respondError(c, http.StatusBadRequest, "role must be 'admin' or 'operator'")
		return
	}

	hash, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		respondError(c, http.StatusInternalServerError, "failed to hash password")
		return
	}

	user, err := repository.DefaultUserRepository.Create(req.Username, string(hash), req.Role)
	if err != nil {
		if errors.Is(err, repository.ErrUserAlreadyExists) {
			respondError(c, http.StatusConflict, "username already exists")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to create user")
		return
	}

	respondCreated(c, user.ToSafe())
}

// ListUsers handles GET /api/v1/users?keyword=&page=&pageSize=
func ListUsers(ctx context.Context, c *app.RequestContext) {
	keyword := string(c.Query("keyword"))
	page, _ := strconv.Atoi(string(c.Query("page")))
	pageSize, _ := strconv.Atoi(string(c.Query("pageSize")))

	result := repository.DefaultUserRepository.List(repository.ListParams{
		Keyword:  keyword,
		Page:     page,
		PageSize: pageSize,
	})

	safe := make([]*model.SafeUser, 0, len(result.Users))
	for _, u := range result.Users {
		safe = append(safe, u.ToSafe())
	}

	respondOK(c, map[string]interface{}{
		"list":  safe,
		"total": result.Total,
	})
}

// GetUser handles GET /api/v1/users/:id
func GetUser(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	user, err := repository.DefaultUserRepository.GetByID(id)
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			respondError(c, http.StatusNotFound, "user not found")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to get user")
		return
	}
	respondOK(c, user.ToSafe())
}

// UpdateUser handles PUT /api/v1/users/:id
func UpdateUser(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")

	var req struct {
		Username string `json:"username"`
		Role     string `json:"role"`
		Status   string `json:"status"`
	}
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Username != "" && !validateUsername(req.Username) {
		respondError(c, http.StatusBadRequest, "username must be 3-32 characters")
		return
	}
	if req.Role != "" && !validateRole(req.Role) {
		respondError(c, http.StatusBadRequest, "role must be 'admin' or 'operator'")
		return
	}
	if req.Status != "" && !validateStatus(req.Status) {
		respondError(c, http.StatusBadRequest, "status must be 'active' or 'disabled'")
		return
	}

	current, err := repository.DefaultUserRepository.GetByID(id)
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			respondError(c, http.StatusNotFound, "user not found")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to get user")
		return
	}
	if current.Role == model.RoleAdmin && (req.Role == model.RoleOperator || req.Status == model.StatusDisabled) && repository.DefaultUserRepository.AdminCount() <= 1 {
		respondError(c, http.StatusBadRequest, "cannot disable or demote the last admin user")
		return
	}

	user, err := repository.DefaultUserRepository.Update(id, repository.UpdateParams{
		Username: req.Username,
		Role:     req.Role,
		Status:   req.Status,
	})
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			respondError(c, http.StatusNotFound, "user not found")
			return
		}
		if errors.Is(err, repository.ErrUserAlreadyExists) {
			respondError(c, http.StatusConflict, "username already exists")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to update user")
		return
	}

	respondOK(c, user.ToSafe())
}

// DeleteUser handles DELETE /api/v1/users/:id
func DeleteUser(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")

	err := repository.DefaultUserRepository.Delete(id)
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			respondError(c, http.StatusNotFound, "user not found")
			return
		}
		if errors.Is(err, repository.ErrLastAdmin) {
			respondError(c, http.StatusBadRequest, "cannot delete the last admin user")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to delete user")
		return
	}

	c.Status(http.StatusNoContent)
}

// ResetPassword handles PUT /api/v1/users/:id/password
// Admin: body = { "newPassword": "..." }
// Self:  body = { "oldPassword": "...", "newPassword": "..." }
func ResetPassword(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")

	var req struct {
		OldPassword string `json:"oldPassword"`
		NewPassword string `json:"newPassword"`
	}
	if err := c.BindJSON(&req); err != nil {
		respondError(c, http.StatusBadRequest, "invalid request body")
		return
	}
	if !validatePassword(req.NewPassword) {
		respondError(c, http.StatusBadRequest, "new password must be at least 8 characters")
		return
	}

	// Admins can reset another user's password; self-service changes require oldPassword.
	callerRole, _ := c.Get("role")
	callerID, _ := c.Get("user_id")
	isSelf := callerID == id
	isAdmin := callerRole == model.RoleAdmin

	if !isAdmin && !isSelf {
		respondError(c, http.StatusForbidden, "cannot reset another user's password")
		return
	}

	oldPassword := ""
	if isSelf {
		if req.OldPassword == "" {
			respondError(c, http.StatusBadRequest, "old password is required")
			return
		}
		oldPassword = req.OldPassword
	}

	newHash, err := bcrypt.GenerateFromPassword([]byte(req.NewPassword), bcrypt.DefaultCost)
	if err != nil {
		respondError(c, http.StatusInternalServerError, "failed to hash password")
		return
	}

	if err := repository.DefaultUserRepository.UpdatePassword(id, oldPassword, string(newHash)); err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			respondError(c, http.StatusNotFound, "user not found")
			return
		}
		if errors.Is(err, repository.ErrInvalidCredentials) {
			respondError(c, http.StatusBadRequest, "old password is incorrect")
			return
		}
		respondError(c, http.StatusInternalServerError, "failed to update password")
		return
	}

	respondOK(c, map[string]interface{}{"message": "password updated successfully"})
}

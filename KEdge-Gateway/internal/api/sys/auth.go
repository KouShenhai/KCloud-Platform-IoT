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
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/repository"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/util"
	"github.com/cloudwego/hertz/pkg/app"
	"golang.org/x/crypto/bcrypt"
)

type LoginRequest struct {
	Username string `json:"username,required"`
	Password string `json:"password,required"`
}

// Login handles user authentication against the dynamic user store and returns a JWT
func Login(ctx context.Context, c *app.RequestContext) {
	var req LoginRequest
	if err := c.BindAndValidate(&req); err != nil {
		c.JSON(http.StatusBadRequest, map[string]interface{}{
			"code":    400,
			"message": "Invalid request",
			"error":   err.Error(),
		})
		return
	}

	// Look up user in dynamic store
	user, err := repository.DefaultUserRepository.GetByUsername(req.Username)
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			c.JSON(http.StatusUnauthorized, map[string]interface{}{
				"code":    401,
				"message": "Invalid username or password",
			})
			return
		}
		c.JSON(http.StatusInternalServerError, map[string]interface{}{
			"code":    500,
			"message": "Internal server error",
		})
		return
	}

	// Verify bcrypt password
	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(req.Password)); err != nil {
		c.JSON(http.StatusUnauthorized, map[string]interface{}{
			"code":    401,
			"message": "Invalid username or password",
		})
		return
	}

	// Reject disabled accounts
	if user.Status == model.StatusDisabled {
		c.JSON(http.StatusForbidden, map[string]interface{}{
			"code":    403,
			"message": "Account is disabled",
		})
		return
	}

	// Generate JWT with userId, username, and role
	token, err := util.GenerateToken(user.ID, user.Username, user.Role, 24*time.Hour)
	if err != nil {
		c.JSON(http.StatusInternalServerError, map[string]interface{}{
			"code":    500,
			"message": "Failed to generate token",
			"error":   err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{
		"code":    200,
		"message": "Login successful",
		"data": map[string]interface{}{
			"token": token,
		},
	})
}

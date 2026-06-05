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

package middlewares

import (
	"context"
	"net/http"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/cloudwego/hertz/pkg/app"
)

// AdminOnlyMiddleware rejects requests from non-admin users with HTTP 403.
// Must be used after AuthMiddleware so that the "role" context key is set.
func AdminOnlyMiddleware() app.HandlerFunc {
	return func(ctx context.Context, c *app.RequestContext) {
		role, exists := c.Get("role")
		if !exists || role != model.RoleAdmin {
			c.JSON(http.StatusForbidden, map[string]interface{}{
				"code":    403,
				"message": "Access denied: admin role required",
			})
			c.Abort()
			return
		}
		c.Next(ctx)
	}
}

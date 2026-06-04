package middlewares

import (
	"context"
	"net/http"
	"strings"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/utils"
	"github.com/cloudwego/hertz/pkg/app"
)

// AuthMiddleware creates a JWT authentication middleware for Hertz
func AuthMiddleware() app.HandlerFunc {
	return func(ctx context.Context, c *app.RequestContext) {
		authHeader := string(c.GetHeader("Authorization"))
		if authHeader == "" {
			c.JSON(http.StatusUnauthorized, map[string]interface{}{
				"code":    401,
				"message": "Authorization header is required",
			})
			c.Abort()
			return
		}

		parts := strings.SplitN(authHeader, " ", 2)
		if len(parts) != 2 || parts[0] != "Bearer" {
			c.JSON(http.StatusUnauthorized, map[string]interface{}{
				"code":    401,
				"message": "Authorization header format must be Bearer {token}",
			})
			c.Abort()
			return
		}

		tokenString := parts[1]
		claims, err := utils.ParseToken(tokenString)
		if err != nil {
			c.JSON(http.StatusUnauthorized, map[string]interface{}{
				"code":    401,
				"message": "Invalid or expired token",
				"error":   err.Error(),
			})
			c.Abort()
			return
		}

		// Store user info in context for later handlers
		c.Set("user_id", claims.UserID)
		c.Set("username", claims.Username)

		c.Next(ctx)
	}
}

package sys

import (
	"context"
	"net/http"
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/utils"
	"github.com/cloudwego/hertz/pkg/app"
)

type LoginRequest struct {
	Username string `json:"username,required"`
	Password string `json:"password,required"`
}

// Login handles user authentication and returns a JWT
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

	// Validate credentials (hardcoded for simplicity at the edge, in prod load from db or config)
	if req.Username != "admin" || req.Password != "admin123" {
		c.JSON(http.StatusUnauthorized, map[string]interface{}{
			"code":    401,
			"message": "Invalid username or password",
		})
		return
	}

	// Generate JWT
	token, err := utils.GenerateToken("1", req.Username, 24*time.Hour)
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

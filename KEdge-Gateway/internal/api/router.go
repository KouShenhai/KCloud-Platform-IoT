package api

import (
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/controllers"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/middlewares"
	"github.com/cloudwego/hertz/pkg/app/server"
)

func RegisterRouters(h *server.Hertz) {
	// Public routes
	api := h.Group("/api/v1")
	{
		api.POST("/login", controllers.Login)
	}

	// Secured routes
	secureAPI := h.Group("/api/v1")
	secureAPI.Use(middlewares.AuthMiddleware())
	{
		secureAPI.POST("/devices", controllers.RegisterDevice)
		secureAPI.GET("/devices", controllers.ListDevices)
		secureAPI.PUT("/devices/:id", controllers.UpdateDevice)
		secureAPI.DELETE("/devices/:id", controllers.DeleteDevice)
	}
}

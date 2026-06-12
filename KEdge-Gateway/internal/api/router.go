package api

import (
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/api/iot"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/api/middlewares"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/api/sys"
	"github.com/cloudwego/hertz/pkg/app/server"
)

func RegisterRouters(h *server.Hertz) {
	// Public routes
	api := h.Group("/api/v1")
	{
		api.POST("/login", sys.Login)
	}

	// Secured routes
	secureAPI := h.Group("/api/v1")
	secureAPI.Use(middlewares.AuthMiddleware())
	{
		secureAPI.POST("/devices", iot.RegisterDevice)
		secureAPI.GET("/devices", iot.ListDevices)
		secureAPI.PUT("/devices/:id", iot.UpdateDevice)
		secureAPI.DELETE("/devices/:id", iot.DeleteDevice)

		secureAPI.GET("/users", sys.ListUsers)
		secureAPI.GET("/users/:id", sys.GetUser)
		secureAPI.PUT("/users/:id/password", sys.ResetPassword)

		secureAPI.GET("/menus", sys.ListMenus)
		secureAPI.GET("/menus/:id", sys.GetMenu)

		adminAPI := secureAPI.Group("")
		adminAPI.Use(middlewares.AdminOnlyMiddleware())
		{
			adminAPI.POST("/users", sys.CreateUser)
			adminAPI.PUT("/users/:id", sys.UpdateUser)
			adminAPI.DELETE("/users/:id", sys.DeleteUser)

			adminAPI.POST("/menus", sys.CreateMenu)
			adminAPI.PUT("/menus/:id", sys.UpdateMenu)
			adminAPI.PUT("/menus/:id/status", sys.UpdateMenuStatus)
			adminAPI.DELETE("/menus/:id", sys.DeleteMenu)
		}
	}
}

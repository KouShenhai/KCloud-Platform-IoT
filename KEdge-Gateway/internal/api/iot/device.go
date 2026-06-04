package iot

import (
	"net/http"

	"context"
	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/services"
	"github.com/cloudwego/hertz/pkg/app"
)

type RegisterDeviceRequest struct {
	Name string `json:"name,required"`
	Type string `json:"type,required"`
}

type UpdateDeviceRequest struct {
	Name   string `json:"name"`
	Status string `json:"status"`
}

func RegisterDevice(ctx context.Context, c *app.RequestContext) {
	var req RegisterDeviceRequest
	if err := c.BindAndValidate(&req); err != nil {
		c.JSON(http.StatusBadRequest, map[string]interface{}{"code": 400, "message": err.Error()})
		return
	}

	device, err := services.DefaultDeviceService.RegisterDevice(req.Name, req.Type)
	if err != nil {
		c.JSON(http.StatusInternalServerError, map[string]interface{}{"code": 500, "message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{"code": 200, "data": device})
}

func ListDevices(ctx context.Context, c *app.RequestContext) {
	devices, err := services.DefaultDeviceService.ListDevices()
	if err != nil {
		c.JSON(http.StatusInternalServerError, map[string]interface{}{"code": 500, "message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{"code": 200, "data": devices})
}

func UpdateDevice(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	var req UpdateDeviceRequest
	if err := c.BindAndValidate(&req); err != nil {
		c.JSON(http.StatusBadRequest, map[string]interface{}{"code": 400, "message": err.Error()})
		return
	}

	device, err := services.DefaultDeviceService.UpdateDevice(id, req.Name, req.Status)
	if err != nil {
		if err == services.ErrDeviceNotFound {
			c.JSON(http.StatusNotFound, map[string]interface{}{"code": 404, "message": "Device not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, map[string]interface{}{"code": 500, "message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{"code": 200, "data": device})
}

func DeleteDevice(ctx context.Context, c *app.RequestContext) {
	id := c.Param("id")
	err := services.DefaultDeviceService.DeleteDevice(id)
	if err != nil {
		if err == services.ErrDeviceNotFound {
			c.JSON(http.StatusNotFound, map[string]interface{}{"code": 404, "message": "Device not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, map[string]interface{}{"code": 500, "message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, map[string]interface{}{"code": 200, "message": "Device deleted"})
}

package repository

import (
	"errors"
	"sync"
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/models"
	"github.com/google/uuid"
)

var (
	ErrDeviceNotFound = errors.New("device not found")
)

type DeviceService struct {
	devices map[string]*models.Device
	mu      sync.RWMutex
}

var DefaultDeviceService = &DeviceService{
	devices: make(map[string]*models.Device),
}

func (s *DeviceService) RegisterDevice(name, deviceType string) (*models.Device, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	device := &models.Device{
		ID:        uuid.New().String(),
		Name:      name,
		Type:      deviceType,
		Status:    "offline",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	s.devices[device.ID] = device
	return device, nil
}

func (s *DeviceService) ListDevices() ([]*models.Device, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	var list []*models.Device
	for _, v := range s.devices {
		list = append(list, v)
	}
	return list, nil
}

func (s *DeviceService) UpdateDevice(id, name, status string) (*models.Device, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	device, exists := s.devices[id]
	if !exists {
		return nil, ErrDeviceNotFound
	}

	if name != "" {
		device.Name = name
	}
	if status != "" {
		device.Status = status
	}
	device.UpdatedAt = time.Now()

	s.devices[id] = device
	return device, nil
}

func (s *DeviceService) DeleteDevice(id string) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	if _, exists := s.devices[id]; !exists {
		return ErrDeviceNotFound
	}

	delete(s.devices, id)
	return nil
}

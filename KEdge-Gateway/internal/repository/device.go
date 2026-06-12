package repository

import (
	"errors"
	"sync"
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/google/uuid"
)

var (
	ErrDeviceNotFound = errors.New("device not found")
)

type DeviceService struct {
	devices map[string]*model.Device
	mu      sync.RWMutex
}

var DefaultDeviceService = &DeviceService{
	devices: make(map[string]*model.Device),
}

func (s *DeviceService) RegisterDevice(name, deviceType string) (*model.Device, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	device := &model.Device{
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

func (s *DeviceService) ListDevices() ([]*model.Device, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	var list []*model.Device
	for _, v := range s.devices {
		list = append(list, v)
	}
	return list, nil
}

func (s *DeviceService) UpdateDevice(id, name, status string) (*model.Device, error) {
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

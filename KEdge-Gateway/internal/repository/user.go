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

package repository

import (
	"errors"
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"sync"
	"time"

	"github.com/KouShenhai/KCloud-Platform-IoT/KEdge-Gateway/internal/model"
	"github.com/google/uuid"
	"golang.org/x/crypto/bcrypt"
	"gopkg.in/yaml.v3"
)

var (
	ErrUserNotFound       = errors.New("user not found")
	ErrUserAlreadyExists  = errors.New("username already exists")
	ErrLastAdmin          = errors.New("cannot delete the last admin user")
	ErrInvalidCredentials = errors.New("invalid old password")
)

// UserRepository manages edge gateway users with in-memory storage and YAML persistence
type UserRepository struct {
	mu       sync.RWMutex
	users    map[string]*model.User // key: user ID
	byName   map[string]string      // key: username → user ID
	dataPath string
}

// DefaultUserRepository is the package-level singleton
var DefaultUserRepository = &UserRepository{
	users:  make(map[string]*model.User),
	byName: make(map[string]string),
}

// ---------------------------------------------------------------------------
// CRUD Operations
// ---------------------------------------------------------------------------

// Create adds a new user. Password must already be bcrypt-hashed by the caller.
func (r *UserRepository) Create(username, passwordHash, role string) (*model.User, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	if _, exists := r.byName[username]; exists {
		return nil, ErrUserAlreadyExists
	}

	now := time.Now()
	u := &model.User{
		ID:           uuid.New().String(),
		Username:     username,
		PasswordHash: passwordHash,
		Role:         role,
		Status:       model.StatusActive,
		CreatedAt:    now,
		UpdatedAt:    now,
	}
	r.users[u.ID] = u
	r.byName[username] = u.ID

	if err := r.saveToFileLocked(); err != nil {
		// Non-fatal: log but don't fail the operation
		fmt.Printf("[UserRepository] warning: failed to persist users: %v\n", err)
	}
	return u, nil
}

// ListParams defines filter/pagination options for listing users
type ListParams struct {
	Keyword  string
	Page     int
	PageSize int
}

// ListResult is the paginated list response
type ListResult struct {
	Users []*model.User
	Total int
}

// List returns a filtered, paginated user list
func (r *UserRepository) List(params ListParams) *ListResult {
	r.mu.RLock()
	defer r.mu.RUnlock()

	var all []*model.User
	for _, u := range r.users {
		if params.Keyword != "" && !strings.Contains(strings.ToLower(u.Username), strings.ToLower(params.Keyword)) {
			continue
		}
		cp := *u
		all = append(all, &cp)
	}
	sort.Slice(all, func(i, j int) bool {
		return all[i].CreatedAt.Before(all[j].CreatedAt)
	})

	total := len(all)

	if params.Page <= 0 {
		params.Page = 1
	}
	if params.PageSize <= 0 {
		params.PageSize = 20
	}

	start := (params.Page - 1) * params.PageSize
	if start >= total {
		return &ListResult{Users: []*model.User{}, Total: total}
	}
	end := start + params.PageSize
	if end > total {
		end = total
	}

	return &ListResult{Users: all[start:end], Total: total}
}

// GetByID retrieves a user by ID
func (r *UserRepository) GetByID(id string) (*model.User, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	u, exists := r.users[id]
	if !exists {
		return nil, ErrUserNotFound
	}
	// Return a copy to avoid mutation outside the lock
	cp := *u
	return &cp, nil
}

// GetByUsername retrieves a user by username
func (r *UserRepository) GetByUsername(username string) (*model.User, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	id, exists := r.byName[username]
	if !exists {
		return nil, ErrUserNotFound
	}
	u := r.users[id]
	cp := *u
	return &cp, nil
}

// UpdateParams contains updatable user fields (empty string = no change)
type UpdateParams struct {
	Username string
	Role     string
	Status   string
}

// Update modifies a user's username, role, or status
func (r *UserRepository) Update(id string, params UpdateParams) (*model.User, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	u, exists := r.users[id]
	if !exists {
		return nil, ErrUserNotFound
	}

	if params.Username != "" && params.Username != u.Username {
		if _, taken := r.byName[params.Username]; taken {
			return nil, ErrUserAlreadyExists
		}
		delete(r.byName, u.Username)
		u.Username = params.Username
		r.byName[u.Username] = id
	}
	if params.Role != "" {
		u.Role = params.Role
	}
	if params.Status != "" {
		u.Status = params.Status
	}
	u.UpdatedAt = time.Now()
	r.users[id] = u

	if err := r.saveToFileLocked(); err != nil {
		fmt.Printf("[UserRepository] warning: failed to persist users: %v\n", err)
	}

	cp := *u
	return &cp, nil
}

// Delete removes a user by ID. Refuses if it would remove the last admin.
func (r *UserRepository) Delete(id string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	u, exists := r.users[id]
	if !exists {
		return ErrUserNotFound
	}

	// Guard: cannot delete last admin
	if u.Role == model.RoleAdmin {
		count := 0
		for _, v := range r.users {
			if v.Role == model.RoleAdmin {
				count++
			}
		}
		if count <= 1 {
			return ErrLastAdmin
		}
	}

	delete(r.byName, u.Username)
	delete(r.users, id)

	if err := r.saveToFileLocked(); err != nil {
		fmt.Printf("[UserRepository] warning: failed to persist users: %v\n", err)
	}
	return nil
}

// UpdatePassword replaces the password hash for a user.
// If oldHash is non-empty, it validates the old password first (self-service change).
// If oldHash is empty, it's an admin reset — no validation required.
func (r *UserRepository) UpdatePassword(id, oldPassword, newPasswordHash string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	u, exists := r.users[id]
	if !exists {
		return ErrUserNotFound
	}

	if oldPassword != "" {
		if err := bcrypt.CompareHashAndPassword([]byte(u.PasswordHash), []byte(oldPassword)); err != nil {
			return ErrInvalidCredentials
		}
	}

	u.PasswordHash = newPasswordHash
	u.UpdatedAt = time.Now()
	r.users[id] = u

	if err := r.saveToFileLocked(); err != nil {
		fmt.Printf("[UserRepository] warning: failed to persist users: %v\n", err)
	}
	return nil
}

// AdminCount returns the number of admin users (useful for guards)
func (r *UserRepository) AdminCount() int {
	r.mu.RLock()
	defer r.mu.RUnlock()
	count := 0
	for _, u := range r.users {
		if u.Role == model.RoleAdmin {
			count++
		}
	}
	return count
}

// ---------------------------------------------------------------------------
// YAML Persistence
// ---------------------------------------------------------------------------

type yamlStore struct {
	Users []*model.User `yaml:"users"`
}

// loadFromFile loads users from a YAML file into memory.
// Called once at startup.
func (r *UserRepository) loadFromFile(path string) error {
	data, err := os.ReadFile(path)
	if err != nil {
		return err
	}

	var store yamlStore
	if err := yaml.Unmarshal(data, &store); err != nil {
		return fmt.Errorf("parse users.yaml: %w", err)
	}

	r.mu.Lock()
	defer r.mu.Unlock()
	r.users = make(map[string]*model.User)
	r.byName = make(map[string]string)
	for _, u := range store.Users {
		r.users[u.ID] = u
		r.byName[u.Username] = u.ID
	}
	return nil
}

// saveToFileLocked persists all users to YAML using an atomic write (temp file + rename).
// Caller MUST hold the write lock.
func (r *UserRepository) saveToFileLocked() error {
	if r.dataPath == "" {
		return nil
	}

	var store yamlStore
	for _, u := range r.users {
		cp := *u
		store.Users = append(store.Users, &cp)
	}
	sort.Slice(store.Users, func(i, j int) bool {
		return store.Users[i].CreatedAt.Before(store.Users[j].CreatedAt)
	})

	data, err := yaml.Marshal(&store)
	if err != nil {
		return fmt.Errorf("marshal users: %w", err)
	}

	dir := filepath.Dir(r.dataPath)
	if err := os.MkdirAll(dir, 0750); err != nil {
		return fmt.Errorf("create data directory: %w", err)
	}
	tmp, err := os.CreateTemp(dir, "users-*.yaml.tmp")
	if err != nil {
		return fmt.Errorf("create temp file: %w", err)
	}
	tmpName := tmp.Name()
	defer func() {
		_ = tmp.Close()
		_ = os.Remove(tmpName) // no-op if rename succeeded
	}()

	if _, err := tmp.Write(data); err != nil {
		return fmt.Errorf("write temp file: %w", err)
	}
	if err := tmp.Close(); err != nil {
		return fmt.Errorf("close temp file: %w", err)
	}

	return os.Rename(tmpName, r.dataPath)
}

// ---------------------------------------------------------------------------
// Initialization
// ---------------------------------------------------------------------------

// InitDefaultAdmin loads users from the YAML file if it exists, or bootstraps
// a default admin user and writes it to the file.
func InitDefaultAdmin(path string) error {
	DefaultUserRepository.dataPath = path

	if _, err := os.Stat(path); err == nil {
		// File exists — load it
		if err := DefaultUserRepository.loadFromFile(path); err != nil {
			return fmt.Errorf("load users from %s: %w", path, err)
		}
		fmt.Printf("[UserRepository] loaded users from %s (%d users)\n",
			path, len(DefaultUserRepository.users))
		return nil
	}

	// File does not exist — create default admin
	if err := os.MkdirAll(filepath.Dir(path), 0750); err != nil {
		return fmt.Errorf("create user data directory: %w", err)
	}

	hash, err := bcrypt.GenerateFromPassword([]byte("admin123"), bcrypt.DefaultCost)
	if err != nil {
		return fmt.Errorf("hash default admin password: %w", err)
	}

	if _, err := DefaultUserRepository.Create("admin", string(hash), model.RoleAdmin); err != nil {
		return fmt.Errorf("create default admin: %w", err)
	}

	fmt.Println("[UserRepository] ⚠️  Default admin user created (username=admin, password=admin123). Please change the password immediately!")
	return nil
}

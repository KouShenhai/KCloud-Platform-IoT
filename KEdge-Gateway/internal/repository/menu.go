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
	"gopkg.in/yaml.v3"
)

var (
	ErrMenuNotFound       = errors.New("menu not found")
	ErrMenuPathExists     = errors.New("menu path already exists")
	ErrMenuParentNotFound = errors.New("parent menu not found")
	ErrMenuCircularParent = errors.New("menu parent cannot point to itself or a descendant")
	ErrMenuHasChildren    = errors.New("menu has child menus")
)

type MenuRepository struct {
	mu       sync.RWMutex
	menus    map[string]*model.Menu
	dataPath string
}

var DefaultMenuRepository = &MenuRepository{
	menus: make(map[string]*model.Menu),
}

type CreateMenuParams struct {
	Name     string
	Path     string
	Icon     string
	ParentID string
	Sort     int
	Status   string
}

type UpdateMenuParams struct {
	Name     string
	Path     string
	Icon     string
	ParentID string
	Sort     int
	Status   string
}

type MenuListParams struct {
	Keyword string
}

type MenuListResult struct {
	Menus []*model.Menu
	Total int
}

func (r *MenuRepository) Create(params CreateMenuParams) (*model.Menu, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	if r.existsPathLocked("", params.Path) {
		return nil, ErrMenuPathExists
	}
	if params.ParentID != "" {
		if _, exists := r.menus[params.ParentID]; !exists {
			return nil, ErrMenuParentNotFound
		}
	}

	now := time.Now()
	menu := &model.Menu{
		ID:        uuid.New().String(),
		Name:      params.Name,
		Path:      params.Path,
		Icon:      params.Icon,
		ParentID:  params.ParentID,
		Sort:      params.Sort,
		Status:    defaultMenuStatus(params.Status),
		CreatedAt: now,
		UpdatedAt: now,
	}
	r.menus[menu.ID] = menu

	if err := r.saveToFileLocked(); err != nil {
		return nil, err
	}
	cp := *menu
	return &cp, nil
}

func (r *MenuRepository) List(params MenuListParams) *MenuListResult {
	r.mu.RLock()
	defer r.mu.RUnlock()

	menus := r.filteredMenusLocked(params.Keyword)
	return &MenuListResult{Menus: menus, Total: len(menus)}
}

func (r *MenuRepository) TreeNodes(params MenuListParams) ([]*model.MenuNode, int) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	included := r.includedMenuIDsLocked(params.Keyword)
	nodes := make(map[string]*model.MenuNode, len(included))
	for id := range included {
		nodes[id] = r.menus[id].ToNode()
	}

	roots := make([]*model.MenuNode, 0)
	for id, node := range nodes {
		parentID := r.menus[id].ParentID
		parent, ok := nodes[parentID]
		if parentID == "" || !ok {
			roots = append(roots, node)
			continue
		}
		parent.Children = append(parent.Children, node)
	}
	sortMenuNodes(roots)
	return roots, len(included)
}

func (r *MenuRepository) GetByID(id string) (*model.Menu, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()

	menu, exists := r.menus[id]
	if !exists {
		return nil, ErrMenuNotFound
	}
	cp := *menu
	return &cp, nil
}

func (r *MenuRepository) Update(id string, params UpdateMenuParams) (*model.Menu, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	menu, exists := r.menus[id]
	if !exists {
		return nil, ErrMenuNotFound
	}
	if r.existsPathLocked(id, params.Path) {
		return nil, ErrMenuPathExists
	}
	if params.ParentID != "" {
		if _, exists := r.menus[params.ParentID]; !exists {
			return nil, ErrMenuParentNotFound
		}
		if params.ParentID == id || r.isDescendantLocked(id, params.ParentID) {
			return nil, ErrMenuCircularParent
		}
	}

	menu.Name = params.Name
	menu.Path = params.Path
	menu.Icon = params.Icon
	menu.ParentID = params.ParentID
	menu.Sort = params.Sort
	menu.Status = defaultMenuStatus(params.Status)
	menu.UpdatedAt = time.Now()
	r.menus[id] = menu

	if err := r.saveToFileLocked(); err != nil {
		return nil, err
	}
	cp := *menu
	return &cp, nil
}

func (r *MenuRepository) UpdateStatus(id string, status string) (*model.Menu, error) {
	r.mu.Lock()
	defer r.mu.Unlock()

	menu, exists := r.menus[id]
	if !exists {
		return nil, ErrMenuNotFound
	}
	menu.Status = status
	menu.UpdatedAt = time.Now()

	if err := r.saveToFileLocked(); err != nil {
		return nil, err
	}
	cp := *menu
	return &cp, nil
}

func (r *MenuRepository) Delete(id string) error {
	r.mu.Lock()
	defer r.mu.Unlock()

	menu, exists := r.menus[id]
	if !exists {
		return ErrMenuNotFound
	}
	for _, item := range r.menus {
		if item.ParentID == menu.ID {
			return ErrMenuHasChildren
		}
	}
	delete(r.menus, id)
	return r.saveToFileLocked()
}

func (r *MenuRepository) existsPathLocked(excludeID string, path string) bool {
	for _, menu := range r.menus {
		if menu.Path == path && menu.ID != excludeID {
			return true
		}
	}
	return false
}

func (r *MenuRepository) isDescendantLocked(id string, maybeDescendantID string) bool {
	currentID := maybeDescendantID
	seen := make(map[string]struct{})
	for currentID != "" {
		if _, ok := seen[currentID]; ok {
			return false
		}
		seen[currentID] = struct{}{}
		current, exists := r.menus[currentID]
		if !exists {
			return false
		}
		if current.ParentID == id {
			return true
		}
		currentID = current.ParentID
	}
	return false
}

func (r *MenuRepository) filteredMenusLocked(keyword string) []*model.Menu {
	keyword = strings.ToLower(strings.TrimSpace(keyword))
	menus := make([]*model.Menu, 0)
	for _, menu := range r.menus {
		if keyword != "" && !strings.Contains(strings.ToLower(menu.Name), keyword) &&
			!strings.Contains(strings.ToLower(menu.Path), keyword) {
			continue
		}
		cp := *menu
		menus = append(menus, &cp)
	}
	sortMenus(menus)
	return menus
}

func (r *MenuRepository) includedMenuIDsLocked(keyword string) map[string]struct{} {
	keyword = strings.ToLower(strings.TrimSpace(keyword))
	included := make(map[string]struct{})
	for id, menu := range r.menus {
		if keyword != "" && !strings.Contains(strings.ToLower(menu.Name), keyword) &&
			!strings.Contains(strings.ToLower(menu.Path), keyword) {
			continue
		}
		included[id] = struct{}{}
		parentID := menu.ParentID
		for parentID != "" {
			parent, exists := r.menus[parentID]
			if !exists {
				break
			}
			included[parent.ID] = struct{}{}
			parentID = parent.ParentID
		}
	}
	if keyword == "" {
		for id := range r.menus {
			included[id] = struct{}{}
		}
	}
	return included
}

type menuYAMLStore struct {
	Menus []*model.Menu `yaml:"menus"`
}

func (r *MenuRepository) loadFromFile(path string) error {
	data, err := os.ReadFile(path)
	if err != nil {
		return err
	}
	var store menuYAMLStore
	if err := yaml.Unmarshal(data, &store); err != nil {
		return fmt.Errorf("parse menus.yaml: %w", err)
	}

	r.mu.Lock()
	defer r.mu.Unlock()
	r.menus = make(map[string]*model.Menu)
	for _, menu := range store.Menus {
		cp := *menu
		r.menus[cp.ID] = &cp
	}
	return nil
}

func (r *MenuRepository) saveToFileLocked() error {
	if r.dataPath == "" {
		return nil
	}

	store := menuYAMLStore{Menus: make([]*model.Menu, 0, len(r.menus))}
	for _, menu := range r.menus {
		cp := *menu
		store.Menus = append(store.Menus, &cp)
	}
	sortMenus(store.Menus)

	data, err := yaml.Marshal(&store)
	if err != nil {
		return fmt.Errorf("marshal menus: %w", err)
	}

	dir := filepath.Dir(r.dataPath)
	if err := os.MkdirAll(dir, 0750); err != nil {
		return fmt.Errorf("create data directory: %w", err)
	}
	tmp, err := os.CreateTemp(dir, "menus-*.yaml.tmp")
	if err != nil {
		return fmt.Errorf("create temp file: %w", err)
	}
	tmpName := tmp.Name()
	defer func() {
		_ = tmp.Close()
		_ = os.Remove(tmpName)
	}()

	if _, err := tmp.Write(data); err != nil {
		return fmt.Errorf("write temp file: %w", err)
	}
	if err := tmp.Close(); err != nil {
		return fmt.Errorf("close temp file: %w", err)
	}
	return os.Rename(tmpName, r.dataPath)
}

func InitDefaultMenus(path string) error {
	DefaultMenuRepository.dataPath = path

	if _, err := os.Stat(path); err == nil {
		if err := DefaultMenuRepository.loadFromFile(path); err != nil {
			return fmt.Errorf("load menus from %s: %w", path, err)
		}
		fmt.Printf("[MenuRepository] loaded menus from %s (%d menus)\n",
			path, len(DefaultMenuRepository.menus))
		return nil
	}

	now := time.Now()
	menus := []*model.Menu{
		{
			ID:        uuid.New().String(),
			Name:      "Home",
			Path:      "/home",
			Icon:      "home",
			Sort:      1,
			Status:    model.MenuStatusEnabled,
			CreatedAt: now,
			UpdatedAt: now,
		},
		{
			ID:        uuid.New().String(),
			Name:      "User Management",
			Path:      "/users",
			Icon:      "user",
			Sort:      10,
			Status:    model.MenuStatusEnabled,
			CreatedAt: now,
			UpdatedAt: now,
		},
		{
			ID:        uuid.New().String(),
			Name:      "Menu Management",
			Path:      "/menus",
			Icon:      "menu",
			Sort:      20,
			Status:    model.MenuStatusEnabled,
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	DefaultMenuRepository.mu.Lock()
	DefaultMenuRepository.menus = make(map[string]*model.Menu)
	for _, menu := range menus {
		cp := *menu
		DefaultMenuRepository.menus[cp.ID] = &cp
	}
	err := DefaultMenuRepository.saveToFileLocked()
	DefaultMenuRepository.mu.Unlock()
	if err != nil {
		return fmt.Errorf("create default menus: %w", err)
	}
	fmt.Printf("[MenuRepository] default menus created at %s\n", path)
	return nil
}

func defaultMenuStatus(status string) string {
	if status == "" {
		return model.MenuStatusEnabled
	}
	return status
}

func sortMenus(menus []*model.Menu) {
	sort.Slice(menus, func(i, j int) bool {
		if menus[i].ParentID != menus[j].ParentID {
			return menus[i].ParentID < menus[j].ParentID
		}
		if menus[i].Sort != menus[j].Sort {
			return menus[i].Sort < menus[j].Sort
		}
		return menus[i].CreatedAt.Before(menus[j].CreatedAt)
	})
}

func sortMenuNodes(nodes []*model.MenuNode) {
	sort.Slice(nodes, func(i, j int) bool {
		if nodes[i].Sort != nodes[j].Sort {
			return nodes[i].Sort < nodes[j].Sort
		}
		return nodes[i].CreatedAt.Before(nodes[j].CreatedAt)
	})
	for _, node := range nodes {
		sortMenuNodes(node.Children)
	}
}

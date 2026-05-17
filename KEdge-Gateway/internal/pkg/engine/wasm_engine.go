package engine

import (
	"context"
	"errors"
	"fmt"
	"os"
	"sync"
	"sync/atomic"
	"time"

	extism "github.com/extism/go-sdk"
)

var (
	ErrPluginNotFound = errors.New("plugin not found")
	ErrPluginClosed   = errors.New("plugin closed")
)

type Plugin struct {
	name   string
	path   string
	plugin *extism.Plugin
	closed atomic.Bool
	mu     sync.Mutex
}

type PluginManager struct {
	ctx     context.Context
	plugins sync.Map
}

func NewPluginManager(ctx context.Context) *PluginManager {
	return &PluginManager{
		ctx: ctx,
	}
}

func (pm *PluginManager) Load(name, path string) error {
	// fast path
	if _, ok := pm.plugins.Load(name); ok {
		return nil
	}
	wasmBytes, err := os.ReadFile(path)
	if err != nil {
		return err
	}
	manifest := extism.Manifest{
		Wasm: []extism.Wasm{
			extism.WasmData{
				Data: wasmBytes,
			},
		},
	}
	config := extism.PluginConfig{
		EnableWasi: true,
	}
	p, err := extism.NewPlugin(
		pm.ctx,
		manifest,
		config,
		nil,
	)
	if err != nil {
		return fmt.Errorf("create plugin failed: %w", err)
	}
	plugin := &Plugin{
		name:   name,
		path:   path,
		plugin: p,
	}
	actual, loaded := pm.plugins.LoadOrStore(name, plugin)
	if loaded {
		// already exists
		plugin.Close(pm.ctx)
		_ = actual
		return nil
	}
	return nil
}

func (pm *PluginManager) Reload(name string) error {

	v, ok := pm.plugins.Load(name)
	if !ok {
		return ErrPluginNotFound
	}
	old := v.(*Plugin)
	old.mu.Lock()
	defer old.mu.Unlock()
	if old.closed.Load() {
		return ErrPluginClosed
	}
	wasmBytes, err := os.ReadFile(old.path)
	if err != nil {
		return err
	}
	manifest := extism.Manifest{
		Wasm: []extism.Wasm{
			extism.WasmData{
				Data: wasmBytes,
			},
		},
	}
	config := extism.PluginConfig{
		EnableWasi: true,
	}
	newPlugin, err := extism.NewPlugin(
		pm.ctx,
		manifest,
		config,
		nil,
	)
	if err != nil {
		return err
	}
	old.plugin.Close(pm.ctx)
	old.plugin = newPlugin
	return nil
}

func (pm *PluginManager) Unload(name string) error {
	v, ok := pm.plugins.Load(name)
	if !ok {
		return ErrPluginNotFound
	}
	p := v.(*Plugin)
	p.mu.Lock()
	defer p.mu.Unlock()
	if p.closed.Load() {
		return nil
	}
	p.closed.Store(true)
	p.plugin.Close(pm.ctx)
	pm.plugins.Delete(name)
	return nil
}

func (pm *PluginManager) Call(
	name string,
	function string,
	input []byte,
) ([]byte, error) {
	v, ok := pm.plugins.Load(name)
	if !ok {
		return nil, ErrPluginNotFound
	}
	p := v.(*Plugin)
	if p.closed.Load() {
		return nil, ErrPluginClosed
	}
	p.mu.Lock()
	defer p.mu.Unlock()
	ctx, cancel := context.WithTimeout(
		pm.ctx,
		5*time.Second,
	)
	defer cancel()
	exit, out, err := p.plugin.Call(
		function,
		input,
	)
	if err != nil {
		return nil, err
	}
	if exit != 0 {
		return nil, fmt.Errorf(
			"plugin exit code: %d",
			exit,
		)
	}
	result := make([]byte, len(out))
	copy(result, out)
	_ = ctx

	return result, nil
}

func (p *Plugin) Close(ctx context.Context) {
	p.plugin.Close(ctx)
}

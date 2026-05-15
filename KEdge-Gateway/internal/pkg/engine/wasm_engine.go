package engine

import (
	"context"
	"errors"
	"fmt"
	"os"
	"sync"

	"KEdge-Gateway/internal/pkg/config"
	"github.com/tetratelabs/wazero"
	"github.com/tetratelabs/wazero/api"
	"github.com/tetratelabs/wazero/imports/wasi_snapshot_preview1"
	"go.uber.org/zap"
)

var WASM *Wasm

type Wasm struct {
	ctx     context.Context
	r       wazero.Runtime
	cfg     wazero.ModuleConfig
	plugins sync.Map
}

type Plugin struct {
	name    string
	mod     api.Module
	allocFn api.Function
	parseFn api.Function
	mu      sync.Mutex
}

func InitWasm() *Wasm {
	ctx := context.Background()
	r := wazero.NewRuntime(ctx)
	cfg := wazero.NewModuleConfig()
	wasi_snapshot_preview1.MustInstantiate(ctx, r)
	wasm := &Wasm{ctx: ctx, r: r, cfg: cfg}
	WASM = wasm
	return wasm
}

func (w *Wasm) Load(path, name string) error {
	if name == "" {
		return errors.New("plugin name is empty")
	}
	if _, ok := w.plugins.Load(name); ok {
		return nil
	}

	wasmBytes, err := os.ReadFile(path)
	if err != nil {
		config.Logger.Error("read plugin error", zap.String("name", name), zap.String("path", path), zap.Error(err))
		return fmt.Errorf("read plugin %q: %w", name, err)
	}

	compiled, err := w.r.CompileModule(w.ctx, wasmBytes)
	if err != nil {
		config.Logger.Error("compile module error", zap.String("name", name), zap.Error(err))
		return fmt.Errorf("compile plugin %q: %w", name, err)
	}

	mod, err := w.r.InstantiateModule(w.ctx, compiled, w.cfg)
	if err != nil {
		config.Logger.Error("instantiate module error", zap.String("name", name), zap.Error(err))
		return fmt.Errorf("instantiate plugin %q: %w", name, err)
	}

	allocFn := mod.ExportedFunction("Alloc")
	if allocFn == nil {
		_ = mod.Close(w.ctx)
		return errors.New("wasm function Alloc not found")
	}
	parseFn := mod.ExportedFunction("Parse")
	if parseFn == nil {
		_ = mod.Close(w.ctx)
		return errors.New("wasm function Parse not found")
	}

	p := &Plugin{name: name, mod: mod, allocFn: allocFn, parseFn: parseFn}
	actual, loaded := w.plugins.LoadOrStore(name, p)
	if loaded {
		_ = mod.Close(w.ctx)
		_ = actual
	}

	return nil
}

func (w *Wasm) UnLoad(name string) {
	v, ok := w.plugins.Load(name)
	if !ok {
		return
	}
	p := v.(*Plugin)
	p.mu.Lock()
	defer p.mu.Unlock()
	_ = p.mod.Close(w.ctx)
	w.plugins.Delete(name)
}

// Parse invokes Parse(ptr, len) in the wasm plugin.
func (w *Wasm) Parse(name string, input []byte) (string, error) {
	v, ok := w.plugins.Load(name)
	if !ok {
		return "", errors.New("plugin not found")
	}
	plugin := v.(*Plugin)

	plugin.mu.Lock()
	defer plugin.mu.Unlock()

	results, err := plugin.allocFn.Call(w.ctx, uint64(len(input)))
	if err != nil {
		return "", fmt.Errorf("call Alloc failed: %w", err)
	}
	if len(results) < 1 {
		return "", errors.New("Alloc returned no results")
	}

	inputPtr := uint32(results[0])
	if !plugin.mod.Memory().Write(inputPtr, input) {
		return "", errors.New("write wasm input failed")
	}

	results, err = plugin.parseFn.Call(w.ctx, uint64(inputPtr), uint64(len(input)))
	if err != nil {
		return "", fmt.Errorf("call Parse failed: %w", err)
	}
	if len(results) < 1 {
		return "", errors.New("Parse returned no results")
	}

	packed := results[0]
	resultPtr := uint32(packed >> 32)
	resultLen := uint32(packed & 0xFFFFFFFF)

	resultBytes, ok := plugin.mod.Memory().Read(resultPtr, resultLen)
	if !ok {
		return "", errors.New("read wasm result failed")
	}
	return string(resultBytes), nil
}

func (w *Wasm) Close() {
	_ = w.r.Close(w.ctx)
}

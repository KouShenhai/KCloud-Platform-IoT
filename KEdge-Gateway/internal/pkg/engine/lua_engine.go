package engine

/*
#cgo CFLAGS: -I/usr/include/luajit-2.1
#cgo LDFLAGS: -lluajit-5.1

#include <lua.h>
#include <lauxlib.h>
#include <lualib.h>
#include <stdlib.h>

// ---------------- wrapper ----------------

static int lua_dostring_wrap(lua_State *L, const char *s) {
	return luaL_dostring(L, s);
}

static void lua_getglobal_wrap(lua_State *L, const char *name) {
	lua_getglobal(L, name);
}

static int lua_isfunction_wrap(lua_State *L, int idx) {
	return lua_isfunction(L, idx);
}

static void lua_pop_wrap(lua_State *L, int n) {
	lua_pop(L, n);
}

static const char* lua_tostring_wrap(lua_State *L, int idx) {
	return lua_tostring(L, idx);
}

*/
import "C"
import (
	"KEdge-Gateway/internal/pkg/config"
	"errors"
	"go.uber.org/zap"
	"sync"
	"unsafe"
)

type LuaEngine struct {
	L  *C.lua_State
	mu sync.Mutex
}

func NewLuaEngine() *LuaEngine {
	L := C.luaL_newstate()
	C.luaL_openlibs(L)
	return &LuaEngine{
		L: L,
	}
}

func (e *LuaEngine) Load(script string) error {
	e.mu.Lock()
	defer e.mu.Unlock()
	cscript := C.CString(script)
	defer C.free(unsafe.Pointer(cscript))
	if C.lua_dostring_wrap(e.L, cscript) != 0 {
		err := C.GoString(C.lua_tostring_wrap(e.L, -1))
		C.lua_pop_wrap(e.L, 1)
		config.Logger.Error("lua load error", zap.String("error", err))
		return errors.New(err)
	}
	return nil
}

func (e *LuaEngine) Execute(funcName string, jsonData string) (string, error) {
	e.mu.Lock()
	defer e.mu.Unlock()
	cFuncName := C.CString(funcName)
	defer C.free(unsafe.Pointer(cFuncName))
	C.lua_getglobal_wrap(e.L, cFuncName)
	if C.lua_isfunction_wrap(e.L, -1) == 0 {
		C.lua_pop_wrap(e.L, 1)
		config.Logger.Error("function not found: " + funcName)
		return "", errors.New("function not found: " + funcName)
	}
	cJson := C.CString(jsonData)
	defer C.free(unsafe.Pointer(cJson))
	C.lua_pushstring(e.L, cJson)
	if C.lua_pcall(e.L, 1, 1, 0) != 0 {
		errMsg := C.GoString(C.lua_tostring_wrap(e.L, -1))
		C.lua_pop_wrap(e.L, 1)
		config.Logger.Error("lua call error", zap.String("error", errMsg))
		return "", errors.New(errMsg)
	}
	result := C.GoString(C.lua_tostring_wrap(e.L, -1))
	C.lua_pop_wrap(e.L, 1)
	return result, nil
}

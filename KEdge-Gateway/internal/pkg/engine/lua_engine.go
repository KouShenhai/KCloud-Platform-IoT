package engine

/*
#cgo CFLAGS: -I/usr/include/luajit-2.1
#cgo LDFLAGS: -lluajit-5.1

#include <lua.h>
#include <lauxlib.h>
#include <lualib.h>
#include <stdlib.h>
*/
import "C"
import (
	"KEdge-Gateway/internal/pkg/config"
	"errors"
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
	if C.luaL_dostring(e.L, cscript) != 0 {
		err := C.GoString(C.lua_tostring(e.L, -1))
		C.lua_pop(e.L, 1)
		config.Logger.Error("lua load error", err)
		return errors.New(err)
	}
	return nil
}

func (e *LuaEngine) Call(funcName string, jsonData string) (string, error) {
	e.mu.Lock()
	defer e.mu.Unlock()
	cFuncName := C.CString(funcName)
	defer C.free(unsafe.Pointer(cFuncName))
	C.lua_getglobal(e.L, cFuncName)
	if C.lua_isfunction(e.L, -1) == 0 {
		C.lua_pop(e.L, 1)
		config.Logger.Error("function not found: " + funcName)
		return "", errors.New("function not found: " + funcName)
	}
	cJson := C.CString(jsonData)
	defer C.free(unsafe.Pointer(cJson))
	C.lua_pushstring(e.L, cJson)
	if C.lua_pcall(e.L, 1, 1, 0) != 0 {
		errMsg := C.GoString(C.lua_tostring(e.L, -1))
		C.lua_pop(e.L, 1)
		config.Logger.Error("lua call error", errMsg)
		return "", errors.New(errMsg)
	}
	result := C.GoString(C.lua_tostring(e.L, -1))
	C.lua_pop(e.L, 1)
	return result, nil
}

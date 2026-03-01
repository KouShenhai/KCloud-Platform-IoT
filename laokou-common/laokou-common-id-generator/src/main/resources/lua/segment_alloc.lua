-- Redis 分段 ID 分配 Lua 脚本
-- 原子操作：对指定 key 执行 INCRBY，返回当前值（即号段的最大值）
-- KEYS[1] = 号段 Key
-- ARGV[1] = 步长
local key = KEYS[1]
local step = tonumber(ARGV[1])
return redis.call('INCRBY', key, step)

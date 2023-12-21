-- User Request Rate Limiter filter
-- See https://stripe.com/blog/rate-limiters
-- See https://gist.github.com/ptarjan/e38f45f2dfe601419ca3af937fff574d#file-1-check_request_rate_limiter-rb-L11-L34

-- 令牌桶算法工作原理
-- 1.系统以恒定速率往桶里面放入令牌
-- 2.请求需要被处理，则需要从桶里面获取一个令牌
-- 3.如果桶里面没有令牌可获取，则可以选择等待或直接拒绝并返回

-- 令牌桶算法工作流程
-- 1.计算填满令牌桶所需要的时间（填充时间 = 桶容量 / 速率）
-- 2.设置存储数据的TTL（过期时间），为填充时间的两倍（存储时间 = 填充时间 * 2）
-- 3.从Redis获取当前令牌的剩余数量和上一次调用的时间戳
-- 4.计算距离上一次调用的时间间隔（时间间隔 = 当前时间 - 上一次调用时间）
-- 5.计算填充的令牌数量（填充令牌数量 = 时间间隔 * 速率）【前提：桶容量是固定的，不存在无限制的填充】
-- 6.判断是否有足够多的令牌满足请求【 (填充令牌数量 + 剩余令牌数量) >= 请求数量 && (填充令牌数量 + 剩余令牌数量) <= 桶容量 】
-- 7.如果请求被允许，则从桶里面取出相应数据的令牌
-- 8.如果TTL为正，则更新Redis键中的令牌和时间戳
-- 9.返回true/false

-- 随机写入
redis.replicate_commands()

-- 令牌桶Key -> 存储当前可用令牌的数量（剩余令牌数量）
local tokens_key = KEYS[1]

-- 时间戳Key -> 存储上次令牌刷新的时间戳
local timestamp_key = KEYS[2]

-- 令牌填充速率
local rate = tonumber(ARGV[1])

-- 令牌桶容量
local capacity = tonumber(ARGV[2])

-- 当前时间
local now = tonumber(ARGV[3])

-- 请求数量
local requested = tonumber(ARGV[4])

-- 填满令牌桶所需要的时间
local fill_time = capacity / rate

-- 设置key的过期时间（填满令牌桶所需时间的2倍）
local ttl = math.floor(fill_time * 2)

-- 判断当前时间，为空则从redis获取
if now == nil then
    now = redis.call('TIME')[1]
end

-- 获取当前令牌的剩余数量
local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
    last_tokens = capacity
end

-- 获取上一次调用的时间戳
local last_refreshed = tonumber(redis.call('get', timestamp_key))
if last_refreshed == nil then
    last_refreshed = 0
end

-- 计算距离上一次调用的时间间隔
local delta = math.max(0, now - last_refreshed)

-- 当前的令牌数量（剩余 + 填充 <= 桶容量）
local now_tokens = math.min(capacity, last_refreshed + (rate * delta))

-- 判断是否有足够多的令牌满足请求
local allowed = now_tokens >= requested

-- 定义当前令牌的剩余数量
local new_tokens = now_tokens

-- 定义被允许标志
local allowed_num = 0
if allowed then
    new_tokens = now_tokens - requested
    -- 允许访问
    allowed_num = 1
end

-- ttl > 0，将当前令牌的剩余数量和当前时间戳存入redis
if ttl > 0 then
    redis.call('setex', tokens_key, ttl, new_tokens)
    redis.call('setex', timestamp_key, ttl, now)
end

-- 返回参数
return { allowed_num == 1 }
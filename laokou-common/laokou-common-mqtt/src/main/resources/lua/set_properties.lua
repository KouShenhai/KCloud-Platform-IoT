-- Redis版本 >= 8.10.2
local changed = 0

for i = 1, #KEYS do
	local key = KEYS[i]
    local pos = 2 * i - 1
    local newTs = ARGV[pos]
    local oldTs = redis.call('HGET', key, 'timestamp')
    if (oldTs and newTs > oldTs) or not oldTs then
        redis.call(
            'HSET', key,
            'timestamp', ARGV[pos],
            'data', ARGV[pos + 1]
        )
		changed = changed + 1
    end
end

return changed

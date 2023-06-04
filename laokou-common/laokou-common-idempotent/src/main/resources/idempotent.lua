if redis.call('EXISTS',KEYS[1]) == 1 then
    -- 删除token
    redis.call("DEL",KEYS[1])
    return true
else
    return false
end
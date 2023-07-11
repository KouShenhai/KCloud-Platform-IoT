if redis.call('exists',KEYS[1]) == 1 then
    return true
else
    return false
end
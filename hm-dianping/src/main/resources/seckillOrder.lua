local voucherId = ARGV[1]
local userId = ARGV[2]

local stockKey = 'seckill:stock:' .. voucherId
local orderKey = 'seckill:order:'.. voucherId

-- 库存不足
local stock = redis.call('get', stockKey)
if (stock == nil) then
    return 1 -- 库存不存在
end
if (tonumber(stock) <= 0) then
    return 1
end

-- 重复下单
if (redis.call('sismember', orderKey, userId) == 1) then
    return 2
end

-- 扣减库存
redis.call('incrby', stockKey, -1)
-- 下单
redis.call('sadd', orderKey, userId)
return 0
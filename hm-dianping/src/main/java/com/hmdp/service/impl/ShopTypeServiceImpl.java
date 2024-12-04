package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONBeanParser;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result getList() {
        String shopList = stringRedisTemplate.opsForValue().get("shopList");
        if (StrUtil.isNotBlank(shopList)) {
            List<ShopType> list = JSONUtil.toList(shopList, ShopType.class);
            return Result.ok(list);
        }
        List<ShopType> list = query().orderByAsc("sort").list();
        if (list == null || list.isEmpty()) {
            return Result.fail("店铺类型不存在");
        }
        stringRedisTemplate.opsForValue().set("shopList", JSONUtil.toJsonStr(list), RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return Result.ok(list);
    }
}

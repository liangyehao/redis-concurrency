package com.liang.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/5/4 12:20
 * @content
 */
@RestController
public class RedisController {

    @Autowired
    Redisson redisson;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    /**
     * 测试减库存
     *
     * @return {@link String}
     */
    @GetMapping("/jkc")
    public String test(){


        String lockKey = "lockKey";
        // 1.获取锁对象
        RLock redissonLock = redisson.getLock(lockKey);

        try {
            // 2.加锁 .setIfAbsent(lockKey, clientId,30,TimeUnit.SECONDS)
            redissonLock.lock();
            int stock = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get("stock")));
            if (stock>0) {
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
                System.out.println("扣减成功,剩余库存: " + realStock);
            }else{
                System.out.println("扣减失败,库存不足.");
            }
        } finally {
            // 3.释放锁
            redissonLock.unlock();
        }



        return "end";
    }

}

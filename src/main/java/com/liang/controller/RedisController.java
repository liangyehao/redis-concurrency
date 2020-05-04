package com.liang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/5/4 12:20
 * @content
 */
@RestController
public class RedisController {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    /**
     * 测试减库存
     *
     * @return {@link String}
     */
    @GetMapping("/jkc")
    public String test(){

        // 1.加锁
        String lockKey = "lockKey";
        try {
            Boolean absent = Objects.requireNonNull(redisTemplate.opsForValue().setIfAbsent(lockKey, "liang"));
            // 2.锁存在,直接返回,不执行减库存
            if (!absent) {
                System.out.println("无法获取资源,请重试...");
                return "error";
            }
            // 3.加锁成功,扣减库存
            int stock = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get("stock")));
            if (stock>0) {
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
                System.out.println("扣减成功,剩余库存: " + realStock);
            }else{
                System.out.println("扣减失败,库存不足.");
            }
        } finally {
            // 4.库存扣减完成,释放锁
            redisTemplate.delete(lockKey);
        }



        return "end";
    }

}

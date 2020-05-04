package com.liang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/jkc")
    public String test(){

        synchronized (this){
            int stock = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get("stock")));
            if (stock>0) {
                int realStock = stock - 1;
                redisTemplate.opsForValue().set("stock", String.valueOf(realStock));
                System.out.println("扣减成功,剩余库存: " + realStock);

            }else{
                System.out.println("扣减失败,库存不足.");
            }
        }
        return "end";
    }

}

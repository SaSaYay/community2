package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author sasayaya
 * @create 2022/7/15 23:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
//        String redisKey = "test:count";
//        redisTemplate.opsForValue().set(redisKey,1);
//        System.out.println(redisTemplate.opsForValue().get(redisKey));
//        System.out.println(redisTemplate.opsForValue().increment(redisKey));
//        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }
//    编程式事务
    @Test
    public void testTransactional(){
        Object obj=redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "text:tx";
                operations.multi();

                operations.opsForSet().add(redisKey,"zhangsan");
                operations.opsForSet().add(redisKey,"l4");
                operations.opsForSet().add(redisKey,"w5");
                operations.opsForSet().add(redisKey,"z6");

                System.out.println(operations.opsForSet().members(redisKey));

                return operations.exec();
            }
        });

        System.out.println(obj);
    }
}

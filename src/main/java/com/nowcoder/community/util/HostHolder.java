package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author sasayaya
 * @create 2022/7/9 21:02
 * 起到容器的作用，代替session
 */
@Component
public class HostHolder {
    public static final Logger LOGGER = LoggerFactory.getLogger(HostHolder.class);
    private ThreadLocal<User> users = new ThreadLocal<>();
    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
//        LOGGER.debug("获取当前对象");
        return users.get();
    }

    public void clear(){
        users.remove();
    }



}

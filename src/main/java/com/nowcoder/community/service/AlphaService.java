package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author sasayaya
 * @create 2022/6/29 23:05
 */
@Service
//@Scope("sinb")
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;
    public AlphaService(){
        System.out.println("实例化");
    }

    @PostConstruct//在构造之后运行
    public void init(){
        System.out.println("初始化AService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("销毁之前调用");
    }

    public String find(){
        return alphaDao.select();
    }

}

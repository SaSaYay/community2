package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author sasayaya
 * @create 2022/6/29 22:59
 */
@Repository
@Primary
public class AlphaDaoMyBatisImp1 implements  AlphaDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}

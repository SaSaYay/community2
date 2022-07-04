package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author sasayaya
 * @create 2022/6/29 22:56
 */

@Repository("alphaHibernate")
public class AlphaDaoHibernateImp1 implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}

package com.nowcoder.community.util;

/**
 * @author sasayaya
 * @create 2022/7/7 22:48
 */
public interface CommunityConstant {
    /*
    激活成功

     */
    int ACTIVATION_SUCCESS = 0;
    /*
    重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /*
     fail
     */
    int ACTIVATION_FAILURE = 2;
    /*
    默认状态的登陆凭证的超时时间

     */
    int DEFAULT_EXPIRED_SECOND = 3600*12;
    /*
    点记住我的登陆凭证的超时时间
     */
    int REMEMBER_EXPIRED_SECOND = 3600*24*100;
    /*
    实体类型:帖子
     */
    int ENTITY_TYPE_POST = 1;
    /*
    实体类型:评论
     */
    int ENTITY_TYPE_COMMENT = 2;


























}

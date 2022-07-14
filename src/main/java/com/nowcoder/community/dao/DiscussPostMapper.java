package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sasayaya
 * @create 2022/7/2 21:43
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPost(int userId,int offset,int limit);

     int selectDiscussPostRows(@Param("userId") int userId);

     int insertDiscussPost(DiscussPost discussPost);

     DiscussPost selectDiscussPostById(int id);

     int updateCommentCount(int id,int commentCount);

     

}

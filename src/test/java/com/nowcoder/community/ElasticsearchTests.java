//package com.nowcoder.community;
//
//import com.nowcoder.community.dao.DiscussPostMapper;
//import com.nowcoder.community.dao.DiscussPostRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @author sasayaya
// * @create 2022/7/18 23:09
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = CommunityApplication.class)
//public class ElasticsearchTests {
//    @Autowired
//    private DiscussPostMapper discussMapper;
//    @Autowired
//    private DiscussPostRepository discussRepository;
////    @Autowired
////    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Test
//    public void testInsert(){
//        discussRepository.save(discussMapper.selectDiscussPostById(241));
//        discussRepository.save(discussMapper.selectDiscussPostById(242));
//        discussRepository.save(discussMapper.selectDiscussPostById(243));
//    }
//}

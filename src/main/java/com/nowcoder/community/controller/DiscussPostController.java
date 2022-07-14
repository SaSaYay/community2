package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.util.*;

/**
 * @author sasayaya
 * @create 2022/7/11 20:32
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content) throws IllegalAccessException {
        User user = hostHolder.getUser();
        if (user==null){
            return CommunityUtil.getJSONString(403,"你还没有登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return CommunityUtil.getJSONString(0,"发布成功");

    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
//        帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
//        作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

//       评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());
//      评论:给帖子的评论
//        回复:回复评论
//        评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                post.getId(), page.getOffset(), page.getLimit());
//        评论VO列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if (commentList!=null){
            for (Comment comment:commentList){
//                一个评论
                Map<String,Object> commentVo = new HashMap<>();
//                评论
                commentVo.put("comment",comment);
//                评论的作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));
//                回复列表
                List<Comment> replayList= commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);
//              回复VO列表
                List<Map<String,Object>> replayVoList = new ArrayList<>();
                if (replayList!=null){
                    for (Comment replay:replayList){
                        Map<String,Object> replayVo = new HashMap<>();
//                        回复
                        replayVo.put("replay",replay);
//                        回复的作者
                        replayVo.put("user",userService.findUserById(replay.getUserId()));
//                      回复的目标
                        User target = replay.getTargetId() == 0 ?
                                null : userService.findUserById(replay.getTargetId());
                        replayVo.put("target",target);

                        replayVoList.add(replayVo);
                    }
                }
                commentVo.put("replays",replayVoList);
//                回复数量
                int replayCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replayCount",replayCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";

    }



















}

package com.nowcoder.community.controller;

import    com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Map;

/**
 * @author sasayaya
 * @create 2022/7/9 21:59
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${quniu.bucket.header.url}")
    private String headerBucketUrl;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        // ??????????????????
        String fileName = CommunityUtil.generateUUID();
        // ??????????????????
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // ??????????????????
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);

        return "/site/setting";
    }

    // ??????????????????
    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "?????????????????????!");
        }

        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

        return CommunityUtil.getJSONString(0);
    }


    @LoginRequired
    @RequestMapping(path = "/change",method = RequestMethod.POST)
    public String changePassword(String password,String newPassword,String againPassword,Model model){

        User user = hostHolder.getUser();
        LOGGER.debug("??????????????????");
        LOGGER.debug(CommunityUtil.md5(1234+user.getSalt()));
        String s = CommunityUtil.md5(password + user.getSalt());
        LOGGER.debug("????????????");
        if (!s.equals(user.getPassword())){
            model.addAttribute("passwordMessage","???????????????");
            return "/site/setting";
        }

        if (!newPassword.equals(againPassword)){
            model.addAttribute("inputMessage","???????????????????????????");
            return "/site/setting";
        }

        user.setPassword(CommunityUtil.md5(newPassword+user.getSalt()));
//        int i = userMapper.updatePassword(user.getId(), CommunityUtil.\\\\\\\\\\\\\\\\\\md5(newPassword+user.getSalt()));
        return "redirect:/index";
    }
//    ??????
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage==null){
            model.addAttribute("error","????????????????????????");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","?????????????????????");
            return "/site/setting";
        }
//        ????????????????????????
        filename = CommunityUtil.generateUUID() + suffix;
//        ???????????????????????????
        File dest = new File(uploadPath+"/"+filename);
        try {
//            ?????????
            headerImage.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("??????????????????"+e.getMessage());
            throw new RuntimeException("??????????????????,?????????????????????",e);
        }
//        ????????????????????????????????????web?????????
        User user = hostHolder.getUser();
        String headUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headUrl);

        return "redirect:/index";
    }
    //      ??????
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename,
                          HttpServletResponse response){
//        ?????????????????????
        filename = uploadPath+"/"+filename;
//        ????????????
        String suffix = filename.substring(filename.lastIndexOf("."));
//        ????????????
        response.setContentType("image/"+suffix);
        try (
                FileInputStream fis = new FileInputStream(filename);
                ){
            ServletOutputStream os = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            LOGGER.error("??????????????????"+e.getMessage());
        }
    }
//    ????????????
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("??????????????????!");
        }

        // ??????
        model.addAttribute("user", user);
        // ????????????
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
//
        // ??????????????????
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // ????????????
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // ???????????????
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }




}

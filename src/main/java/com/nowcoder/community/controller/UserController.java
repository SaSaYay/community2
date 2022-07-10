package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
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
public class UserController {

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

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/change",method = RequestMethod.POST)
    public String changePassword(String password,String newPassword,String againPassword,Model model){

        User user = hostHolder.getUser();
        LOGGER.debug("获取当前对象");
        LOGGER.debug(CommunityUtil.md5(1234+user.getSalt()));
        String s = CommunityUtil.md5(password + user.getSalt());
        LOGGER.debug("加密密码");
        if (!s.equals(user.getPassword())){
            model.addAttribute("passwordMessage","密码不正确");
            return "/site/setting";
        }

        if (!newPassword.equals(againPassword)){
            model.addAttribute("inputMessage","两次输入密码不一致");
            return "/site/setting";
        }

        user.setPassword(CommunityUtil.md5(newPassword+user.getSalt()));
//        int i = userMapper.updatePassword(user.getId(), CommunityUtil.\\\\\\\\\\\\\\\\\\md5(newPassword+user.getSalt()));
        return "redirect:/index";
    }
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
//        生成随机的文件名
        filename = CommunityUtil.generateUUID() + suffix;
//        确定文件存放的路径
        File dest = new File(uploadPath+"/"+filename);
        try {
//            存文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常",e);
        }
//        更新当前用户的头像路径（web路径）
        User user = hostHolder.getUser();
        String headUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename,
                          HttpServletResponse response){
//        服务器存放路径
        filename = uploadPath+"/"+filename;
//        文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
//        响应图片
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
            LOGGER.error("读取头像失败"+e.getMessage());
        }
    }


}

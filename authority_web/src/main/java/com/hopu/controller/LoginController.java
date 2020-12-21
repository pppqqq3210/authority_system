package com.hopu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hopu.domain.User;
import com.hopu.service.IUserService;
import com.hopu.utils.OSSUtils;
import com.hopu.utils.ShiroUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;

@Controller
@RequestMapping("user")
public class LoginController {
    @Autowired
    private IUserService userService;

	@PostMapping("login")
	public String login(User user, HttpServletRequest request) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
//            HttpSession session = WebUtils.toHttp(request).getSession();
            User user1 = (User) subject.getPrincipal();
//            session.setAttribute("user",user1);
            return "redirect:/user/toIndexPage?name="+user1.getUserName();
        } catch (Exception e) {
            String msg = "账户[" + token.getPrincipal() + "]的用户名或密码错误！";
            request.setAttribute("msg",msg);
            return "forward:/login.jsp";
        }
	}
	@RequestMapping("toIndexPage")
	public String toIndexPage(String name,HttpServletRequest request){
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", name));
        HttpSession session = WebUtils.toHttp(request).getSession();
        session.setAttribute("user",user);
        request.setAttribute("user",user);
        return "admin/index";
    }

    @RequestMapping("getImgStream")
    @ResponseBody
    public void getImgStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) WebUtils.toHttp(request).getSession().getAttribute("user");
        String userImgName = user.getUserImg();
        BufferedImage image = OSSUtils.downloadPic(userImgName);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        outputStream.flush();
        outputStream.close();
//        File file = new File("D:\\upload\\123.jpg");
//
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
////        IOUtils.copy(ImgStream,fileOutputStream);
//        byte[] t =new byte[1024];
//        int len = 0;
//        while ((len=image.read(t))>0){
//            fileOutputStream.write(t,0,len);
//        }
//        fileOutputStream.close();


    }



    @RequestMapping("logout")
    public String logout(){
	    return "forward:/login.jsp";
    }

}

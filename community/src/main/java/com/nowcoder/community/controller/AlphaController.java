package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/alpha", method = RequestMethod.GET)
public class AlphaController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot";
    }

    @RequestMapping(value = "/teacher",method = RequestMethod.GET)
 //   @ResponseBody
    public String getTeacher(Model model){
        model.addAttribute("name","谭清");
        model.addAttribute("age",28);

        return "/demo/view";

    }
    //cookie 实例
    @RequestMapping(value = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    //因为cookie被携带于响应的头部
    public String setCookie(HttpServletResponse response){
        //创建cookie，（）里为cookie携带的内容,只能存字符串，session可以存各种数据
        Cookie cookie=new Cookie("code", CommunityUtil.generateUUID());
        //设置生效范围http://localhost:8080/community/alpha
        cookie.setPath("/community/alpha");
        //设置生效时间600S
        cookie.setMaxAge(600);

        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }
    @RequestMapping(value = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    //取cookie中key=“code”的value赋值给String code,要添加required=false否则报错
    public String getCookie(@CookieValue (value = "code",required=false) String code){
        System.out.println("code = " + code);

        return "get cookie";
    }
    //session实例
    @RequestMapping(value = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    //session可以存各种数据
    public String setSession(HttpSession session){
        session.setAttribute("ID",1);
        session.setAttribute("name","test");


        return "set Session";
    }

}

package com.nowcoder.community.controller;


import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.nowcoder.community.util.CommunityConstant.ACTIVATION_REPEAT;
import static com.nowcoder.community.util.CommunityConstant.ACTIVATION_SUCCESS;

@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;

    //获取注册页面
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    //获取登录页面
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    //将表单传入
    @RequestMapping(path = "/register", method = RequestMethod.POST)
//    可以使用三个参数接收username、password、email。也可以使用User接受参数，
//    当接收的参数名与User的属性匹配时，springMVC会将User添加到Model中；
//    input标签要添加name属性且名字要和user的属性相对应，springMVC根据同名原则将数据传到controller的user
//    前端表单传过来的两个参数username, password会自动绑定到User这个POJO上。
//    其实这都是Spring @RequestMapping这个注解的功劳，它会自动扫描形参的POJO，并创建对象，
//    如果前端传进来的参数与POJO成员变量名相同，会通过POJO的setter方法传给该对象。

    public String register(Model model, User user) {
//        获取处理好的数据，将注册的属性传入
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            //跳转到一个提示新注册成功的页面，map为空说明没有任何错误信息
//            使用model将参数名以及参数值传到前端
            model.addAttribute("msg", "注册成功！我们已经向您的邮箱发送了一封邮件，赶快去激活吧！");
            model.addAttribute("target", "/index");//跳转到首页的逻辑
            //将上述参数传入到operate-result。html文件后，返回一个已经配置好参数的页面
            return "/site/operate-result";
        } else {
            //反之，注册失败哦，返回注册失败的信息
            model.addAttribute("usernameMsg", map.get("passwordMsg"));//map.get("passwordMsg")可能为空
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            //注册失败重新返回注册页面
            return "/site/register";
        }
    }

    /**
     * 激活业务
     */
//path为http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "activation/{userId}/{code}", method = RequestMethod.GET)
    //@PathVariable("userId") int userId,在路径上取值
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {//成功跳转登录login页面
            model.addAttribute("msg", "激活成功！您的账号可以正常使用了，赶快去浏览吧！");
            model.addAttribute("target", "/login");//跳转到登录页面的逻辑

        } else if (result == ACTIVATION_REPEAT) {//重复激活跳转首页内
            model.addAttribute("msg", "无效操作，该账号已经激活过了！！！");
            model.addAttribute("target", "/index");//跳转到首页 面的逻辑
        } else {//失败跳转首页
            model.addAttribute("msg", "激活失败，激活码错误");
            model.addAttribute("target", "/index");//跳转到首页的逻辑
        }
        return "/site/operate-result";
    }
    /**
     * 生成验证码
     */
    @RequestMapping(path="/kaptcha",method=RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码后，赋值于session
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码填入session
        session.setAttribute("kaptcha",text);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();//获取输出流
            ImageIO.write(image,"png",os);//输出图片，格式，输出的流
        } catch (IOException e) {
            logger.error("验证码响应失败！！"+e.getMessage());
        }

    }
}
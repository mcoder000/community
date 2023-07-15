package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}

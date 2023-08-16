package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//将处理好的数据返回到前端页面
@Service
public class UserService implements CommunityConstant {
@Autowired
private UserMapper userMapper;
@Autowired
private MailClient mailClient;
@Autowired
private TemplateEngine templateEngine;
//因为后期需要更改域名，所以将域名配置在.propertise文件
@Value("${communiity.path.domain}")
private String domain;
@Value("${server.servlet.context-path}")
private String contextPath;

public User findUserById(int id){
    return userMapper.selectById(id);
    }

//因为进行表单提交会出现多种情况，需要将这些不同的 情况返回到前端，
// 可以将不同的情况put到map，通过返回map实现数据的后端返回前端，通过传入user实现前端传后端。
public Map<String,Object> register(User user){
    Map<String,Object> map=new HashMap<>();
    //检查参数有没有错误
    if (user==null){
        throw new IllegalArgumentException("参数不能为空");
    }
    //判断输入是否为空,空格也是空，为空时StringUtils.isBlank(key)为1
    if (StringUtils.isBlank(user.getUsername())){
        map.put("usernameMsg","账号不能为空");
        return map;
        }
    if (StringUtils.isBlank(user.getPassword())){
        map.put("passwordMsg","密码不能为空");
        return map;
        }
    if (StringUtils.isBlank(user.getEmail())){
        map.put("emailMsg","邮箱不能为空");
        return map;
        }
// 检查是否已经存在
    if (userMapper.selectByName(user.getUsername())!=null){
        map.put("duplicateMsg","该用户名已被使用！");
        return map;
    }else {

//      注册用户
// 用户名不重复则将剩下的属性赋值
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));//快捷键shift+Alt+↑上移一行shift+Alt+↓下移一行
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
//  拼接URL，生成0-1000的随机数，生成随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
//  将user添加到数据库
        userMapper.insertUser(user);

//  生成激活邮件
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);//有点类似于map的作用，将key的值赋值给map
//       将参数与网页拼接，但是需要在网页接受
        String content=templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(), "激活账号",content);
    }
    return map;
    }

    /**
 *  激活操作
 *  用户获取激活码会输入激活码，激活状态，根据用户ID判断是否已经激活,所以要获取userID 激活码code
 */

public int  activation(int userId,String code) {
    User user =userMapper.selectById(userId);
    if (user.getStatus()==1) {//注册时为0，为1说明已经激活
        return ACTIVATION_REPEAT;
    } else if (user.getActivationCode().equals(code)) {//激活操作时，输入的code与 user 的code相等则激活成功，并更改状态
        userMapper.updateStatus(userId,1);
        return ACTIVATION_SUCCESS;
    }else {
        return ACTIVATION_FAILURE;
    }
}

}

package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//将处理好的数据返回到前端页面
@Service
public class UserService {
@Autowired
private UserMapper userMapper;
public User findUserById(int id){
    return userMapper.selectById(id);
    }

}

package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//业务层
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
//查询页面数据
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);

    }
//查询某一行
    public int findDiscussPostRaws(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);

    }

}

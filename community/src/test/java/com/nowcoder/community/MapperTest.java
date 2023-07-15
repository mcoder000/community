package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    //注入对象
    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Test
    public void testSelectPost(){
        List<DiscussPost> list=discussPostMapper.selectDiscussPosts(0,0,10);
        for (DiscussPost post:list) {
            System.out.println(post);
        }




    }






}

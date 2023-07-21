package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class MailTest {//注入对象
    @Autowired
    private MailClient mailClient;
    @Test
    public void testTextMail(){
        mailClient.sendMail("596844984@qq.com","TSTE","this is a new email!!!");
    }
    @Test
    public void testHtmltMail(){

    }
}

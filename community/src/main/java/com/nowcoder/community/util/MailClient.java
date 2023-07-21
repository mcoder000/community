package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {
//    类似于实体类，有什么属性，方法，邮件主题，发送到，内容，接受者
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;
//    发送者 获取配置表的用户名
    @Value("${spring.mail.username}")
    private String from;
//    private String to;
//    private String subject;
//    private String content;
//    构造一个方法供外部调用
    public void sendMail(String to,String subject,String content) {

        try {
            MimeMessage message= mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
//        true,表示邮件可以读取HTML文件，网页形式展示邮件，而不是只是文字
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件时发生错误"+e.getMessage());
        }

    }

}

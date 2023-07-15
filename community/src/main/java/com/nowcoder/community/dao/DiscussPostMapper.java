package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {
//首页查询userid为0,个人主页userID为实际值，即动态SQL来实现，offset表示每一页起始行行号。limit每一页显示多少条数据
    List<DiscussPost> selectDiscussPosts(int userid,int offset,int limit);
    //计算查询的行数，param注解用于给参数起别名，如果只有一个参数而且在<if>里面使用，则必须起别名
    //动态SQL使用到参数，且改参数只有一个，需要起别名
    int selectDiscussPostRows(@Param("userid")int userid);


}

package com.planet.message.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.message.domain.Message;
import com.planet.vo.MessageVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MessageMapper extends MybatisMapper{
    int deleteByPrimaryKey(Integer msgid);

    int insert(Message message);

    int insertSelective(Message message);

    Message selectByPrimaryKey(Integer msgid);

    int updateByPrimaryKeySelective(Message message);

    int updateByPrimaryKey(Message message);

    //查询所有mbid用于删除
    List<Message> selectByMbid(Integer mbid);

    //消息分页模糊查询
    List<MessageVo> listPageSelectMessage(Map<String, Object> map);

    //消息分页查询（ for customer）
    List<MessageVo> listPageSelectForUser(Map<String, Object> map);
}
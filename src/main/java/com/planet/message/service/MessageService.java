package com.planet.message.service;

import com.planet.message.domain.Message;
import com.planet.vo.MessageVo;

import java.util.List;
import java.util.Map;

/**
 * Created by yijinjing on 16/3/1.
 */
public interface MessageService {

    int insert(Message message) throws Exception;

    int deleteByPrimaryKey(Integer msgid) throws Exception;

    int insertSelective(Message message) throws Exception;

    int updateByPrimaryKey(Message message) throws Exception;

    int updateByPrimaryKeySelective(Message message) throws Exception;

    Message selectByPrimaryKey(Integer msgid) throws Exception;

    //查询所有mbid用于删除
    List<Message> selectByMbid(Integer mbid) throws Exception;

    //消息分页模糊查询
    List<MessageVo> listPageSelectMessage(Map<String, Object> map) throws Exception;

    //消息分页查询（ for customer）
    List<MessageVo> listPageSelectForUser(Map<String, Object> map) throws Exception;

}

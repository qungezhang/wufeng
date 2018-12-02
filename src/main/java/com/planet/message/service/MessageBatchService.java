package com.planet.message.service;


import com.planet.message.domain.MessageBatch;

import java.util.List;
import java.util.Map;

/**
 * Created by yijinjing on 16/3/1.
 */

public interface MessageBatchService  {

    MessageBatch selectByPrimaryKey(Integer mbid) throws Exception;

    int deleteByPrimaryKey(Integer mbid) throws Exception;

    int insert(MessageBatch messageBatch) throws Exception;

    int insertSelective(MessageBatch messageBatch) throws Exception;

    int updateByPrimaryKeySelective(MessageBatch messageBatch) throws Exception;

    int updateByPrimaryKey(MessageBatch messageBatch) throws Exception;

    //消息发送
    int insertMessage(Map<String,Object> map) throws Exception;

    //消息群发
    int insertMessageAll(Map<String,Object> map) throws Exception;

    //消息模板分页查询
    List<MessageBatch> listPageSelectForUser(Map<String, Object> map) throws Exception;

}

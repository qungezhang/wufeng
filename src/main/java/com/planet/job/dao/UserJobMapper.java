package com.planet.job.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.job.domain.UserJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author winnie
 * @ClassName:UserJobMapper
 * @Description:用户任务关联Mapper
 * @date 2018/8/10
 */
@Repository
public interface UserJobMapper extends MybatisMapper{


    /**
     * @Description 获取最新的用户放弃的任务
     * @author winnie
     * @create 2018/8/10 13:33
     */
    List<UserJob> getUpToDate( Map<String, Object> map);

    /**
     * @Description 获取数据
     * @param id 用户任务id
     * @author aiveily
     * @create 2018/8/10 16:16
     */
    UserJob get(Integer id);

    /**
     * @Description 修改用户任务
     * @param userJob 用户任务实体类
     * @author aiveily
     * @create 2018/8/10 15:27
     */
    int updateByPrimaryKeySelective(UserJob userJob);

    /**
     * @Description 根据任务编号查找被领取的任务
     * @param jid 任务编号
     * @author aiveily
     * @create 2018/8/10 17:27
     */
    UserJob findByReceive(String jid);

    /**
     * @Description 删除被用户放弃的任务记录
     * @param jid 任务编号
     * @author aiveily
     * @create 2018/8/10 17:31
     */
    int deleteByJid(String jid);

    /**
     * @Description 保存用户任务
     * @param userJob 用户任务实体类
     * @author winnie
     * @create 2018/8/10 16:27
     */
    int insertByPrimaryKeySelective(UserJob userJob);

    /**
     * @Description 根据jobId获取是否存在领取任务信息
     * @param jobId
     * @author aiveily
     * @create 2018/9/5 9:49
     */
    List<UserJob> getUserJobListByJid(Map<String,Object> map);


    /**
     * 一个人接取成功，其他人状态变成失败
     * @return
     */
    int updateByJodidAndUserid(Map<String,Object> map);

    /**
     * 判断该用户是否领取过这个任务
     * @param map
     * @return
     */
    List<UserJob> getUserJobListByJidAndUser(Map<String,Object> map);

    /**
     * 获取接这个任务的所有人员
     * @param jid
     * @return
     */
    List<Map<String,Object>> getJobUserList(String jid);
}

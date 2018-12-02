package com.planet.job.service;

import com.planet.job.domain.Job;

import java.util.List;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:JobService
 * @Description:任务接口
 * @date 2018/8/9
 */
public interface JobService {

    /**
     * @Description 查询任务列表
     * @param map.jid 任务编号
     * @param map.name 任务名称
     * @param map.status 任务状态
     * @author aiveily
     * @create 2018/8/9 16:31
     */
    List<Job> listPageSelectJob(Map map) throws Exception;

    /**
     * @Description 查询任务列表
     * @author aiveily
     * @create 2018/8/9 16:31
     */
    List<Job> listPageSelectJobAll(Map map) throws Exception;

    /**
     * @Description 新增任务
     * @author aiveily
     * @create 2018/8/9 17:32
     */
    String insertByPrimaryKeySelective(Job job)  throws Exception;

    /**
     * @Description 修改任务
     * @author aiveily
     * @create 2018/8/9 17:32
     */
    int updateByPrimaryKeySelective(Job job)  throws Exception;

    /**
     * @Description 删除任务
     * @author aiveily
     * @create 2018/8/9 17:32
     */
    int deleteByPrimaryKey(String jid) throws Exception;

    /**
     * @Description 通过主键id获取任务详情
     * @author winnie
     * @create 2018/8/10 10:32
     */
    Job get(String jobId)  throws Exception;

    /**
     * @Description 通过用户id获取任务列表
     * @author winnie
     * @create 2018/8/10 10:32
     */
    List<Job> listPageSelectUserJob( Map map)  throws Exception;

    /**
     * @Description 查询截止时间在30分钟之内以及超过了截止时间的数据
     * @author aiveily
     * @create 2018/9/5 14:29
     */
    List<Job> selectIn30MinuteJobList();

}

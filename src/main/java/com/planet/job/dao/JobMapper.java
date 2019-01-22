package com.planet.job.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.job.domain.Job;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:JobMapper
 * @Description:任务Mapper
 * @date 2018/8/9
 */
@Repository
public interface JobMapper extends MybatisMapper{

    /**
     * @Description 查询任务列表
     * @param map.jid 任务编号
     * @param map.name 任务名称
     * @param map.status 任务状态
     * @author aiveily
     * @create 2018/8/9 16:31
     */
    List<Job> listPageSelectJob(Map map);

    /**
     * @Description 查询任务列表
     * @param map.jid 任务编号
     * @param map.name 任务名称
     * @param map.status 任务状态
     * @author aiveily
     * @create 2018/8/9 16:31
     */
    List<Job> listPageSelectJobAll(Map map);

    /**
     * @Description 新增任务
     * @author aiveily
     * @create 2018/8/9 17:33
     */
    int insertByPrimaryKeySelective(Job job);

    /**
     * @Description 修改任务
     * @author aiveily
     * @create 2018/8/9 17:33
     */
    int updateByPrimaryKeySelective(Job job);

    /**
     * @Description 删除任务
     * @author aiveily
     * @create 2018/8/9 17:33
     */
    int deleteByPrimaryKey(String jid);

    /**
     * @Description 获取任务详情
     * @author winnie
     * @create 2018/8/10 13:13
     */
    Job get(String id);

    /**
     * @Description 获取当前用户的任务列表
     * @author winnie
     * @create 2018/8/10 17:34
     */
    List<Job> listPageSelectUserJob(Map map);

    /**
     * @Description 查询截止时间在30分钟之内以及超过了截止时间的数据
     * @author aiveily
     * @create 2018/9/5 14:29
     */
    List<Job> selectIn30MinuteJobList();



}

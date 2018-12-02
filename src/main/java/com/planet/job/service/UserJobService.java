package com.planet.job.service;

import com.planet.job.domain.UserJob;

import java.util.List;
import java.util.Map;

/**
 * @author winnie
 * @ClassName:UserJobService
 * @Description:用户任务关联接口
 * @date 2018/8/10
 */
public interface UserJobService {

    List<UserJob> getUpToDate( Map<String, Object> map);

    /**
     * @Description 获取数据
     * @param id 用户任务id
     * @author aiveily
     * @create 2018/8/10 16:16
     */
    UserJob get(Integer id);

    /**
     * @Description 删除被用户放弃的任务记录
     * @param jid 任务编号
     * @author aiveily
     * @create 2018/8/10 17:31
     */
    int deleteByJid(String jid);

    /**
     * @Description 查找被领取的任务（status in 1,2,5）
     * @param jid 任务编号
     * @author aiveily
     * @create 2018/8/10 17:27
     */
    UserJob findByReceive(String jid);

    /**
     * @Description 修改用户任务
     * @param userJob 用户任务实体类
     * @author aiveily
     * @create 2018/8/10 15:27
     */
    int updateByPrimaryKeySelective(UserJob userJob);

    /**
     * @Description 审核
     * @author aiveily
     * @create 2018/8/10 15:43
     */
    int examineUserJob(UserJob userJob);

    /**
     * @Description 保存用户任务关联表
     * @author winnie
     * @create 2018/8/10 16:43
     */
    int insertByPrimaryKeySelective(UserJob userJob);

    /**
     * @Description 根据jobId获取是否存在领取任务信息
     * @param jobId
     * @author aiveily
     * @create 2018/9/5 9:49
     */
    List<UserJob> getUserJobListByJid(String jobId,Integer uid);

}

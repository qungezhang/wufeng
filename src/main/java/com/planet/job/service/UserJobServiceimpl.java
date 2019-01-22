package com.planet.job.service;

import com.planet.common.Constant;
import com.planet.job.dao.JobMapper;
import com.planet.job.dao.UserJobMapper;
import com.planet.job.domain.Job;
import com.planet.job.domain.UserJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author winnie
 * @ClassName:UserJobServiceimpl
 * @Description:用户任务关联
 * @date 2018/8/10
 */
@Service
@Transactional
public class UserJobServiceimpl implements UserJobService{

    private static final Logger logger = LoggerFactory.getLogger(UserJobServiceimpl.class);

    @Autowired
    private UserJobMapper userJobMapper;
    @Autowired
    private JobMapper jobMapper;

    public List<UserJob> getUpToDate( Map<String, Object> map){
        return userJobMapper.getUpToDate(map);
    }

    @Override
    public UserJob get(Integer id) {
        return userJobMapper.get(id);
    }

    @Override
    public int deleteByJid(String jid) {
        return userJobMapper.deleteByJid(jid);
    }

    @Override
    public UserJob findByReceive(String jid) {
        return userJobMapper.findByReceive(jid);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(UserJob userJob) {
        int i = 0;
        i = userJobMapper.updateByPrimaryKeySelective(userJob);
        if(i==0){
            logger.info("更新用户任务失败");
        }else if(i==1){
            logger.info("更新用户任务成功");
        }
        return i;
    }

    @Override
    public int examineUserJob(UserJob userJob) {
        int i = 0;
        i = userJobMapper.updateByPrimaryKeySelective(userJob);
        if(i==0){
            logger.info("审核失败");
        }else if(i==1){
            Job job = jobMapper.get(userJob.getJobId());
            if(null != job){
                Integer statu = userJob.getExamineStatus();
                /**思路：审核人的时候改变任务状态，当是审核通过时，任务状态变成审核通过
                 * 其他也接了这个任务的用户状态改变成审核失败*/
                if(statu == 1){
                    job.setStatus(Constant.JOB_STATUS_02);
                    jobMapper.updateByPrimaryKeySelective(job);
                    Map<String,Object> map = new HashMap<>();
                    map.put("uid",userJob.getUid());
                    map.put("JobId",userJob.getJobId());
                    userJobMapper.updateByJodidAndUserid(map);
                }
//                job.setStatus(Constant.JOB_EXAMINE_STATUS_PASS == statu ? Constant.JOB_STATUS_02 : Constant.JOB_STATUS);
            }
            logger.info("审核成功");
        }
        return i;
    }

    public int insertByPrimaryKeySelective(UserJob userJod){return userJobMapper.insertByPrimaryKeySelective(userJod);}

    @Override
    public List<UserJob> getUserJobListByJid(String jobId,Integer uid) {
        Map<String,Object> map = new HashMap<>();
        map.put("jobId",jobId);
        map.put("uid",uid);
        return userJobMapper.getUserJobListByJid(map);
    }

    @Override
    public List<UserJob> getUserJobListByJidAndUser(Map<String, Object> map) {
        return userJobMapper.getUserJobListByJidAndUser(map);
    }

    /**
     * 获取接这个任务的所有人员
     * @param jid
     * @return
     */
    @Override
    public List<Map<String, Object>> getJobUserList(String jid) {
        return userJobMapper.getJobUserList(jid);
    }


}

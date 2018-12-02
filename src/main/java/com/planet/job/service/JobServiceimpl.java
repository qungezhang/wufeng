package com.planet.job.service;

import com.planet.common.Constant;
import com.planet.idrule.service.IdRuleService;
import com.planet.job.dao.JobMapper;
import com.planet.job.domain.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:JobServiceimpl
 * @Description:任务实现类
 * @date 2018/8/9
 */
@Service
@Transactional
public class JobServiceimpl implements JobService{

    private static final Logger logger = LoggerFactory.getLogger(JobServiceimpl.class);

    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private IdRuleService idRuleService;

    @Override
    public List<Job> listPageSelectJob(Map map) throws Exception {
        return jobMapper.listPageSelectJob(map);
    }

    @Override
    public List<Job> listPageSelectJobAll(Map map) throws Exception {
        return jobMapper.listPageSelectJobAll(map);
    }

    @Override
    public Job get(String id) throws Exception {
        return jobMapper.get(id);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String insertByPrimaryKeySelective(Job job) throws Exception {
        String jId = idRuleService.selectByNumName("J");
        job.setJid(jId);
        job.setStatus(Constant.JOB_STATUS);
        job.setCreateTime(new Date());
        int i = 0;
        i = jobMapper.insertByPrimaryKeySelective(job);
        if(i==0){
            logger.info("创建任务失败");
        }else if(i==1){
            logger.info("创建任务成功");
            return jId;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(Job job) throws Exception {
        job.setModifyTime(new Date());
        int i = 0;
        i = jobMapper.updateByPrimaryKeySelective(job);
        if(i==0){
            logger.info("更新任务失败");
        }else if(i==1){
            logger.info("更新任务成功");
        }
        return i;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteByPrimaryKey(String jid) throws Exception {
        int i = 0;
        i = jobMapper.deleteByPrimaryKey(jid);
        if(i==0){
            logger.info("删除任务失败");
        }else if(i==1){
            logger.info("删除任务成功");
        }
        return i;
    }

    public List<Job> listPageSelectUserJob(Map map) throws Exception {
        return jobMapper.listPageSelectUserJob(map);
    }

    @Override
    public List<Job> selectIn30MinuteJobList() {
        return jobMapper.selectIn30MinuteJobList();
    }
}

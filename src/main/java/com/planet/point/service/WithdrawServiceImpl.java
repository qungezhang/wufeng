package com.planet.point.service;

import com.planet.common.Constant;
import com.planet.idrule.service.IdRuleService;
import com.planet.point.dao.PointLogMapper;
import com.planet.point.dao.WithdrawMapper;
import com.planet.point.domain.PointLog;
import com.planet.point.domain.Withdraw;
import com.planet.user.dao.UserAgentMapper;
import com.planet.user.domain.UserAgent;
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
 * @ClassName:WithdrawServiceImpl
 * @Description:提现接口实现类
 * @date 2018/8/13
 */
@Service
@Transactional
public class WithdrawServiceImpl implements WithdrawService{

    private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private IdRuleService idRuleService;

    @Autowired
    private UserAgentMapper userAgentMapper;

    @Autowired
    private PointLogMapper pointLogMapper;

    @Override
    public List<Withdraw> listPageSelectWithdraw(Map<String, Object> map) {
        return withdrawMapper.listPageSelectWithdraw(map);
    }

    @Override
    public Withdraw get(String wid) {
        return withdrawMapper.get(wid);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertByPrimaryKeySelective(Withdraw withdraw)  throws Exception{
        String wid = idRuleService.selectByNumName("W");
        int i = 0;
        withdraw.setWid(wid);
        withdraw.setCreateTime(new Date());
        i = withdrawMapper.insertByPrimaryKeySelective(withdraw);
        if(i==0){
            logger.info("新增提现记录失败");
        }else if(i==1){
            logger.info("新增提现记录成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(Withdraw withdraw)  throws Exception{
        int i = 0;
        withdraw.setTransferTime(new Date());
        i = withdrawMapper.updateByPrimaryKeySelective(withdraw);
        if(i==0){
            logger.info("更新提现记录失败");
        }else if(i==1){
            logger.info("更新提现记录成功");
        }
        return i;
    }

    @Override
    public int examineWithdraw(Withdraw withdraw) throws Exception {
        int i = 0;
        withdraw.setTransferTime(new Date());
        i = withdrawMapper.updateByPrimaryKeySelective(withdraw);
        if(i==0){
            logger.info("更新提现记录失败");
        }else if(i==1){
            UserAgent userAgent = userAgentMapper.selectByPrimaryKey(withdraw.getUid());
            if(Constant.WITHDRWA_STATUS_SUCCESS == withdraw.getStatus()){
                userAgent.setPoint(userAgent.getPoint() - withdraw.getExchangePoint());
                int success = userAgentMapper.updateByPrimaryKey(userAgent);

                PointLog pointLog = new PointLog();
                if (success != 0) {
                    pointLog.setUid(userAgent.getUid());
                    pointLog.setAfterpoint(userAgent.getPoint());
                    pointLog.setPoint(withdraw.getExchangePoint());
                    pointLog.setMsg("用户提现消耗积分" + withdraw.getExchangePoint() );
                    pointLog.setTel(userAgent.getTel());
                    pointLog.setCreatedate(new Date());
                    pointLog.setType(2);
                    //插入记录
                    pointLogMapper.insertSelective(pointLog);
                }
            }else if(Constant.WITHDRWA_STATUS_FAIL == withdraw.getStatus()){
                if(0 == withdraw.getWithdrawConsumeType()){
                    userAgent.setOrderAmount(userAgent.getOrderAmount().add(withdraw.getWithdrawBeforeAmount()));
                }else{
                    userAgent.setServiceNum(userAgent.getServiceNum()+3);
                }
                userAgentMapper.updateByPrimaryKey(userAgent);
            }
            logger.info("更新提现记录成功");
        }
        return i;
    }
}

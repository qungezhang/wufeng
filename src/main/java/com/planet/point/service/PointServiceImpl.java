package com.planet.point.service;

import com.planet.common.Constant;
import com.planet.message.service.MessageBatchService;
import com.planet.point.dao.PointBalloonMapper;
import com.planet.point.dao.PointLogMapper;
import com.planet.point.domain.PointBalloon;
import com.planet.point.domain.PointLog;
import com.planet.user.dao.UserAgentMapper;
import com.planet.user.domain.UserAgent;
import org.springframework.aop.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yehao on 2016/4/13.
 */
@Service
@Transactional
public class PointServiceImpl implements PointService {

    @Autowired
    private UserAgentMapper userAgentMapper;

    @Autowired
    private PointLogMapper pointLogMapper;

    @Autowired
    private PointBalloonMapper pointBalloonMapper;

    @Autowired
    private MessageBatchService messageBatchService;

    /**
     * 修改积分，并添加日志记录
     *
     * @param uid
     * @param quentity
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int modifyPoint(int uid, Double quentity, String msg, String adminname) {

        //查询user
        UserAgent userAgent = userAgentMapper.selectByPrimaryKey(uid);

        //修改积分
        userAgent.setPoint(userAgent.getPoint() + quentity);
        int success = userAgentMapper.updateByPrimaryKeySelective(userAgent);

        PointLog pointLog = new PointLog();
        if (success != 0) {
            pointLog.setUid(userAgent.getUid());
            pointLog.setAfterpoint(userAgent.getPoint());
            pointLog.setPoint(quentity);
            pointLog.setMsg(msg);
            pointLog.setTel(userAgent.getTel());
            pointLog.setCreatedate(new Date());
            pointLog.setAdminname(adminname);
            if (quentity > 0) {
                //type = 1 增加
                pointLog.setType(1);
            } else {
                //type = 2 减少
                pointLog.setType(2);
            }
            //插入记录
            success = pointLogMapper.insertSelective(pointLog);
            return success;
        }
        return 0;
    }

    /**
     * 摘取能量球积分，并添加日志记录
     *
     * @param uid
     * @param pbid
     * @param adminname
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int gatherEnergyBall(int uid, Integer pbid, String adminname) {

        //查询user
        UserAgent userAgent = userAgentMapper.selectByPrimaryKey(uid);

        PointBalloon pointBalloon = pointBalloonMapper.get(pbid);

        Double quentity = pointBalloon.getPoint();

        //修改积分
        userAgent.setPoint(userAgent.getPoint() + quentity);
        int success = userAgentMapper.updateByPrimaryKeySelective(userAgent);

        String pointMsg = "";
        Integer getWay = 0;
        // 注册 > 认证 > 交易 > 邀请码注册 > 转发 > 默认
        if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_REGISTER){
            pointMsg = "新用户注册";
            getWay = Constant.POINT_BALLOON_REGISTER;
        }else if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_AUTH){
            pointMsg = "用户认证";
            getWay = Constant.POINT_BALLOON_AUTH;
        }else if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_TRADE){
            pointMsg = "订单交易";
            getWay = Constant.POINT_BALLOON_TRADE;
        }else if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_CODE_REGISTER){
            pointMsg = "邀请码被注册";
            getWay = Constant.POINT_BALLOON_CODE_REGISTER;
        }else if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_SHARE){
            pointMsg = "转发";
            getWay = Constant.POINT_BALLOON_SHARE;
        }else if(pointBalloon.getGetWay() == Constant.POINT_BALLOON_BRAND){
            pointMsg = "登录赠送品牌积分";
            getWay = Constant.POINT_BALLOON_BRAND;
        }

        PointLog pointLog = new PointLog();
        if (success != 0) {
            pointLog.setUid(userAgent.getUid());
            pointLog.setAfterpoint(userAgent.getPoint());
            pointLog.setPoint(quentity);
            pointLog.setMsg(pointMsg);
            pointLog.setTel(userAgent.getTel());
            pointLog.setCreatedate(new Date());
            pointLog.setAdminname(adminname);
            pointLog.setGetWay(getWay);
            pointLog.setPbid(pbid);
            pointLog.setType(1);

            //插入记录
            success = pointLogMapper.insertSelective(pointLog);

            if(pointBalloon.getType() == 2){
                pointBalloon.setStatus(1);
                pointBalloon.setEndTime(new Date());
                pointBalloonMapper.updateByPrimaryKey(pointBalloon);
            }

            //摘取积分发送系统消息
            try {
                Map<String, Object> info = new HashMap<>();
                info.put("title","获得积分");
                info.put("message","用户摘取"+pointMsg+"（" + quentity + "）积分");
                info.put("receiver",uid);
                info.put("sender",0);//0:系统发送
                info.put("mtype",1);
                info.put("rtype", 1);
                int i =  messageBatchService.insertMessage(info);
            }catch (Exception e){
                e.printStackTrace();
            }
            return success;
        }
        return 0;
    }

    /**
     * 分页查询积分日志
     *
     * @param map
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<PointLog> searchPointLogs(Map map) {
        return pointLogMapper.listPagePointLog(map);
    }

    /**
     * 根据当前用户的UID查询最新积分信息
     *
     * @param uid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PointLog getPointLog(Integer uid) {
        return pointLogMapper.getPointLogByUID(uid) == null ? null : pointLogMapper.getPointLogByUID(uid).get(0);

    }

    /**
     * 插入积分日志数据
     *
     * @param pointLog
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertByPrimaryKeySelective(PointLog pointLog) {
        return pointLogMapper.insertSelective(pointLog);

    }
}

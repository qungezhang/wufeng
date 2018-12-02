package com.planet.point.service;

import com.planet.point.dao.PointBalloonMapper;
import com.planet.point.dao.PointLogMapper;
import com.planet.point.domain.PointBalloon;
import com.planet.point.domain.PointLog;
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
 * Created by yehao on 2016/4/13.
 */
@Service
@Transactional
public class PointBalloonServiceImpl implements PointBalloonService {

    private static final Logger logger = LoggerFactory.getLogger(PointBalloonService.class);

    @Autowired
    private PointBalloonMapper pointBalloonMapper;

    @Override
    public List<PointBalloon> getListAll(Map<String,Object> map) {
        return pointBalloonMapper.getListAll(map);
    }

    @Override
    public List<PointBalloon> getListCreateTimeNotNull(Map<String,Object> map) {
        return pointBalloonMapper.getListCreateTimeNotNull(map);
    }

    /**
     * 获取已产生的能量球
     * @return
     */
    public List<PointBalloon> getPointBalloonList(Map<String,Object> map){
        return pointBalloonMapper.getPointBalloonList(map);
    }

    /**
     * 随机获取未产生的能量球
     *
     * @return
     */
    public PointBalloon get(Integer pointBalloonId){
        return pointBalloonMapper.get(pointBalloonId);
    }

    @Override
    public int getBalloonNum(Integer type) {
        return pointBalloonMapper.getBalloonNum(type);
    }

    @Override
    public PointBalloon randPointBalloon(Integer type) {
        return pointBalloonMapper.randPointBalloon(type);
    }

    @Override
    public int insertByPrimaryKeySelective(PointBalloon pointBalloon) throws Exception {
        int i = 0;
        i = pointBalloonMapper.insertByPrimaryKeySelective(pointBalloon);
        if(i==0){
            logger.info("新增能量积分失败");
        }else if(i==1){
            logger.info("新增能量积分成功");
        }
        return i;
    }

    @Override
    public int updateByPrimaryKey(PointBalloon pointBalloon) throws Exception {
        int i = 0;
        i = pointBalloonMapper.updateByPrimaryKey(pointBalloon);
        if(i==0){
            logger.info("更新能量积分失败");
        }else if(i==1){
            logger.info("更新能量积分成功");
        }
        return i;
    }

    @Override
    public int updateEnergyStatus(Integer type) throws Exception {
        int i = 0;
        i = pointBalloonMapper.updateEnergyStatus(type);
        if(i==0){
            logger.info("更新能量积分失败");
        }else if(i==1){
            logger.info("更新能量积分成功");
        }
        return i;
    }

    @Override
    public PointBalloon randGetUrlByPointBalloon(Map<String,Object> map) {
        return pointBalloonMapper.randGetUrlByPointBalloon(map);
    }

    @Override
    public int findByCurr(Map<String, Object> map) {
        return pointBalloonMapper.findByCurr(map);
    }

}

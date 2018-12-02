package com.planet.point.service;

import com.planet.point.domain.PointLog;

import java.util.List;
import java.util.Map;

/**
 * Created by yehao on 2016/4/13.
 */
public interface PointService {

    /**
     * 修改积分，并添加日志记录
     *
     * @param uid
     * @param quentity
     * @return
     */
    int modifyPoint(int uid, Double quentity, String msg,String adminname) throws Exception;


    /**
     * 摘取能量球積分，并添加日志记录
     *
     * @param uid
     * @param pbid
     * @return
     */
    int gatherEnergyBall(int uid, Integer pbid,String adminname) throws Exception;

    /**
     * 分页查询积分日志
     *
     * @param map
     * @return
     */
    List<PointLog> searchPointLogs(Map map);

    /**
     * 查询当前用户最新积分信息
     *
     * @param uid
     * @return
     */
    PointLog getPointLog(Integer uid);

    /**
     * @Description 新增积分日志
     * @param pointLog
     * @author winnie
     * @create 2018/8/13 13:46
     */
    int insertByPrimaryKeySelective(PointLog pointLog);
}

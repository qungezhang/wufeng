package com.planet.point.service;

import com.planet.point.domain.PointBalloon;

import java.util.List;
import java.util.Map;


/**
 * Created by winnie on 2018/8/08.
 */
public interface PointBalloonService {

    /**
     * 获取所有列表品牌积分列表
     * @return
     */
    List<PointBalloon> getListAll(Map<String, Object> map);

    /**
     * 获取创建时间不为空的品牌积分列表
     * @return
     */
    List<PointBalloon> getListCreateTimeNotNull(Map<String, Object> map);

    /**
     * 通过主键id获取能量球
     *
     * @return
     */
    PointBalloon get(Integer pointBalloonId) throws Exception;

    /**
     * @Description 查询能量球生成的数量
     * @param type 类型
     * @author aiveily
     * @create 2018/8/9 9:25
     */
    int getBalloonNum(Integer type);

    /**
     * @Description 查询未产生的能量气泡
     * @param type 类型
     * @author aiveily
     * @create 2018/8/9 9:40
     */
    PointBalloon randPointBalloon(Integer type);

    /**
     * @Description 新增能量气泡
     * @param pointBalloon
     * @author aiveily
     * @create 2018/8/14 14:33
     */
    int insertByPrimaryKeySelective(PointBalloon pointBalloon) throws Exception;

    /**
     * @Description 修改能量气泡数据
     * @param pointBalloon
     * @author aiveily
     * @create 2018/8/9 9:54
     */
    int updateByPrimaryKey(PointBalloon pointBalloon) throws Exception;

    /**
     * @Description 更新自动生成的气泡球的状态
     * @author aiveily
     * @create 2018/8/9 13:45
     */
    int updateEnergyStatus(Integer type) throws Exception;

    /**
     * @Description 获取已经产生的能量球
     * @author winnie
     * @return
     */
    List<PointBalloon> getPointBalloonList(Map<String, Object> map);


    /**
     * 随机获取一个产品类型的中的图片
     * @param map
     * @return
     */
    PointBalloon randGetUrlByPointBalloon(Map<String, Object> map);

    /**
     * 查询今日的条数
     * @param map
     * @return
     */
    int findByCurr(Map<String,Object> map);

}

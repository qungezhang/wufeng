package com.planet.point.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.point.domain.PointBalloon;
import com.planet.point.domain.PointLog;

import java.util.List;
import java.util.Map;

public interface PointBalloonMapper extends MybatisMapper {

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
     *获取已产生的能量球
     *
     * @return
     */
    List<PointBalloon> getPointBalloonList(Map<String, Object> map);

    /**
     * 根据主键id获取能量球
     *
     * @return
     */
    PointBalloon get(Integer id);

    /**
     * @Description 查询有用户生成了多少能量球
     * @param type 类型
     * @author aiveily
     * @create 2018/8/9 9:39
     */
    int getBalloonNum(Integer type);

    /**
     * @Description 查询未产生的能量气泡
     * @param type 类型
     * @author aiveily
     * @create 2018/8/9 9:41
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
     * @create 2018/8/9 9:56
     */
    int updateByPrimaryKey(PointBalloon pointBalloon);

    /**
     * @Description 更新自动生成的能量气泡求状态
     * @author aiveily
     * @create 2018/8/9 13:46
     */
    int updateEnergyStatus(Integer type);

    /**
     * 根据参数获取能量球列表
     * @param map
     * @return
     */
    List<PointBalloon> getList(Map<String, Object> map);

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
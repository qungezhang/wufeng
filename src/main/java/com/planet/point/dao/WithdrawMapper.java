package com.planet.point.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.point.domain.Withdraw;

import java.util.List;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:WithdrawMapper
 * @Description:
 * @date 2018/8/13
 */
public interface WithdrawMapper extends MybatisMapper {


    /**
     * @Description 查询提现列表
     * @param map
     * @author aiveily
     * @create 2018/8/13 10:11
     */
    List<Withdraw> listPageSelectWithdraw(Map<String,Object> map);

    /**
     * @Description 根据id查询数据
     * @param wid 提现编号
     * @author aiveily
     * @create 2018/8/13 13:45
     */
    Withdraw get(String wid);

    /**
     * @Description 新增提现记录
     * @param withdraw
     * @author aiveily
     * @create 2018/8/13 13:46
     */
    int insertByPrimaryKeySelective(Withdraw withdraw);

    /**
     * @Description 修改提现记录
     * @param withdraw
     * @author aiveily
     * @create 2018/8/13 13:46
     */
    int updateByPrimaryKeySelective(Withdraw withdraw);
}

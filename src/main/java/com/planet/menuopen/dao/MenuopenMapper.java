package com.planet.menuopen.dao;

import com.planet.common.mybatis.MybatisMapper;
import com.planet.menuopen.domain.Menuopen;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:28 2018/12/9
 * @Description: Modified by:
 */
@Repository
public interface MenuopenMapper extends MybatisMapper {

   List<Menuopen> getMenuOpenList(Map<String, Object> map);

   int updateByPrimaryKeySelective(Menuopen menuopen);

   Menuopen getOneMenuopen(Integer ids);
}

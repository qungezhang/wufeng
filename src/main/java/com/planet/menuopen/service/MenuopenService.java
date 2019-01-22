package com.planet.menuopen.service;

import com.planet.menuopen.domain.Menuopen;

import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:29 2018/12/9
 * @Description: Modified by:
 */
public interface MenuopenService {

    List<Menuopen> getmenuopenList(Map<String, Object> map);

    int updateByPrimaryKeySelective(Menuopen menuopen);

    Menuopen getOneMenuopen(Integer ids);
}

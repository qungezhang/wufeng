package com.planet.menuopen.service;

import com.planet.menuopen.dao.MenuopenMapper;
import com.planet.menuopen.domain.Menuopen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:30 2018/12/9
 * @Description: Modified by:
 */
@Service
@Transactional
public class MenuopenServiceimpl implements MenuopenService{

    private static final Logger logger = LoggerFactory.getLogger(MenuopenServiceimpl.class);
    @Autowired
    private MenuopenMapper menuopenMapper;


    @Override
    public List<Menuopen> getmenuopenList(Map<String, Object> map) {
        return menuopenMapper.getMenuOpenList(map);
    }

    @Override
    public int updateByPrimaryKeySelective(Menuopen menuopen) {
        int i = 0;
        i = menuopenMapper.updateByPrimaryKeySelective(menuopen);
        if(i==0){
            logger.info("更新失败");
        }else if(i==1){
            logger.info("更新成功");
        }
        return i;
    }

    @Override
    public Menuopen getOneMenuopen(Integer ids) {
        return menuopenMapper.getOneMenuopen(ids);
    }
}

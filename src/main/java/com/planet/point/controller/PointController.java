package com.planet.point.controller;

import com.planet.admin.domain.Admin;
import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.common.util.DateTools;
import com.planet.point.domain.PointBalloon;
import com.planet.point.domain.PointLog;
import com.planet.point.service.PointBalloonService;
import com.planet.point.service.PointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yehao on 2016/4/13.
 */
@Controller
@RequestMapping("/point")
public class PointController {

    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    @Autowired
    private PointService pointService;

    @Autowired
    private PointBalloonService pointBalloonService;

    /**
     * 积分管理首页
     *
     * @return
     */
    @RequestMapping("/pointIndex")
    public ModelAndView messageIndex() {
        return new ModelAndView("/point/index");
    }

    /**
     * 修改积分，并添加日志记录
     *
     * @param uid
     * @param quentity
     * @param pointMsg
     * @return
     */
    @RequestMapping("/modifyPoint")
    @ResponseBody
    public Map modifyPoint(HttpServletRequest request, Integer uid, Double quentity, String pointMsg) {
        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "操作失败";
        int success = 0;
        try {
            Admin admin = (Admin) request.getSession().getAttribute("admin");
            success = pointService.modifyPoint(uid, quentity, pointMsg, admin.getUsername());
            if (success == 1) {
                msg = "操作成功";
            }
        } catch (Exception e) {
            logger.error("修改积分出错！", e.getMessage());
            msg = "操作失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

    /**
     * @param page
     * @param rows
     * @param tel
     * @return
     */
    @RequestMapping("/searchPointLogs")
    @ResponseBody
    public Map searchPointLogs(Integer page, Integer rows, String tel) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        List<PointLog> pointLogs = null;
        Pagination pagination = null;

        try {

            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);
            if (!StringUtils.isEmpty(tel))
                map.put("tel", tel);
            pointLogs = pointService.searchPointLogs(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        model.put("rows", pointLogs);
        model.put("total", pagination.getCount());
        return model;
    }

    /**
     * @version 1.0.0
     * @description 生成能量球时间）
     * @param
     * @return
     * @author aiveily
     * @date 2018/9/18 15:06
     */
    @RequestMapping("/pointApi/createTime")
    @ResponseBody
    public Map<String,Object> createTime(){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        map.put("type",1);
        map.put("getWay", Constant.POINT_BALLOON_BRAND);
        // 查询所有创建时间不为空的类型为品牌的能量数据
        List<PointBalloon> list = pointBalloonService.getListAll(map);
        Date newDate = DateTools.getTimesmorn();

        try {
            if (null != list && list.size() > 0) {
                for (PointBalloon p : list) {
                    String tempStr = DateTools.getDateMinut(DateTools.parseDateToString(newDate, "yyyy-MM-dd HH:mm:ss"), 140);
                    p.setCreateTime(DateTools.parseStringToDate(tempStr, "HH:mm:ss"));
                    pointBalloonService.updateByPrimaryKey(p);
                    newDate = DateTools.parseStringToDate(tempStr, "HH:mm:ss");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("list",list);
        return resultMap;
    }

}

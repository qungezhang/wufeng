package com.planet.point.controller;

import com.planet.admin.domain.Admin;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.point.domain.Withdraw;
import com.planet.point.service.WithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aiveily
 * @ClassName:WithdrawController
 * @Description:提现后台接口
 * @date 2018/8/13
 */

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);

    @Autowired
    WithdrawService withdrawService;

    
    /**
     * @Description 提现管理首页
     * @author aiveily
     * @create 2018/8/13 10:26
     */
    @RequestMapping("/index")
    public ModelAndView withdrawIndex() {
        return new ModelAndView("/point/withdrawIndex");
    }


    @RequestMapping("/listWithdraw")
    @ResponseBody
    public Map listWithdraw(HttpServletRequest request, int rows, int page) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<String, Object>();
        Pagination pagination = null;
        List<Withdraw> list = new ArrayList<>();

        String wid = null;
        String account = null;
        String type = null;
        String status = null;

        Admin admin = new Admin();
        admin = (Admin) request.getSession().getAttribute("admin");
        if (!"0".equals(admin.getRole())) {
            return model;
        }

        try {
            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            wid = request.getParameter("wid");
            account = request.getParameter("account");
            type = request.getParameter("type");
            status = request.getParameter("status");
            map.put("wid", wid);
            map.put("account",account);
            map.put("type",type);
            map.put("status", status);

            list = withdrawService.listPageSelectWithdraw(map);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        model.put("rows", list);
        model.put("total", pagination.getCount());
        return model;
    }


    /**
     * @Description 提现审核
     * @author aiveily
     * @create 2018/8/13 13:43
     */
    @RequestMapping("/examineWithdraw")
    @ResponseBody
    public Map examineWithdraw(HttpServletRequest request) {

        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "操作失败";
        int success = 0;
        String wid = null;
        String status = null;
        String remark = null;

        Admin a = (Admin) request.getSession().getAttribute("admin");
        if ("0".equals(a.getRole())) {
            try {
                wid = request.getParameter("wid");
                status = request.getParameter("status");
                remark = request.getParameter("remark");

                Withdraw withdraw = withdrawService.get(wid);
                withdraw.setStatus(Integer.parseInt(status));
                withdraw.setRemark(remark);

                success = withdrawService.examineWithdraw(withdraw);
                logger.info("操作成功");
                if (success == 1) {
                    msg = "操作成功";
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                success = 0;
            }
        } else {
            msg = "权限错误！";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

}

package com.planet.message.controller;

import cn.jpush.api.push.PushResult;
import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.jiguang.JiGService;
import com.planet.message.domain.MessageBatch;
import com.planet.message.service.MessageBatchService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by yehao on 2016/3/3.
 */
@Controller
@RequestMapping("/messageBatch")
public class MessageBatchController {

    private static final Logger logger = LoggerFactory.getLogger(MessageBatchController.class);

    @Autowired
    private MessageBatchService messageBatchService;
    @Autowired
    private UserService userService;


    @RequestMapping("/index")
    public ModelAndView messageIndex() {
        return new ModelAndView("/messageBatch/index");
    }


    @RequestMapping("/list")
    @ResponseBody
    public Map listMessage(HttpServletRequest request, int rows, int page) {
        //init
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        Pagination pagination = null;
        String sended = null;
        String mtype = null;
        String title = null;
        String rtype = null;

        List<MessageBatch> messageBatchs = new ArrayList<>();
        try {

            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);

            mtype = request.getParameter("mtype");
            if (mtype != null && !"".equals(mtype)) {
                map.put("mtype", mtype);
            }

            title = request.getParameter("title");
            if (title != null && !"".equals(title)) {
                map.put("title", title);
            }

            sended = request.getParameter("sended");
            if (sended != null && !"".equals(sended)) {
                map.put("sended", sended);
            }

            rtype = request.getParameter("rtype");
            if (rtype != null && !"".equals(rtype)) {
                map.put("rtype", rtype);
            }

            messageBatchs = messageBatchService.listPageSelectForUser(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        model.put("rows", messageBatchs);
        model.put("total", pagination.getCount());
        return model;
    }


    @RequestMapping("/remove")
    @ResponseBody
    public Map removeMessageBatch(int mbid) {
        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "删除失败";
        int success = 0;
        try {
            success = messageBatchService.deleteByPrimaryKey(mbid);
            if (success == 1) {
                msg = "删除成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map addMessageBatch(MessageBatch messageBatch,String tel) {
        //init
        Map<String, Object> model = new HashMap<String, Object>();
        String msg = "新增失败";
        int success = 0;
        try {
//            if (messageBatch.getRtype() == 1) {
//                UserAgent userAgent = userService.selectByUserName(tel);
//                if (userAgent != null) {
//                    messageBatch.setReceiver(userAgent.getUid());
//                }else {
//                    msg = "手机号不存在";
//                    success = 0;
//                    model.put("msg", msg);
//                    model.put("success", success);
//                    return model;
//                }
//            }
            messageBatch.setMtype(1);
            messageBatch.setRtype(2);
            if (messageBatch.getMbid() != null) {
                success = messageBatchService.updateByPrimaryKeySelective(messageBatch);
                if (success == 1) {
                    msg = "修改成功";
                }
            } else {
                messageBatch.setSenddate(new Date());
                messageBatch.setStatus(0);
                messageBatch.setSended(0);
                success = messageBatchService.insertSelective(messageBatch);
                if (success == 1) {
                    msg = "新增成功";
                }
            }


            logger.info("新增消息模板成功");
        } catch (Exception e) {
            logger.error("新增消息错误", e);
            msg = "新增失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

    /**
     * @version 1.0.0
     * @description 推送消息
     * @author aiveily
     * @date 2018/10/26 16:50
     */
    @RequestMapping(value = "/sendJiGMessage")
    @ResponseBody
    public Map<String,Object> sendJiGMessage(Integer mbid) {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> message = new HashMap<>();
        try {
            MessageBatch messageBatch = messageBatchService.selectByPrimaryKey(mbid);
            message.put("type", Constant.MESSAGE_SEND);
            message.put("title",messageBatch.getTitle());
            message.put("content",messageBatch.getMessage());
            PushResult pushResult = JiGService.sendJiGMessage(message);
            if (null == pushResult || 0 != pushResult.getResponseCode()) {
                messageBatch.setSended(1);
                messageBatchService.updateByPrimaryKeySelective(messageBatch);
                /**
                 *
                 Map<String,Object> map = new HashMap<>();
                 map.put("title", messageBatch.getTitle());
                 map.put("message", messageBatch.getMessage());
                 map.put("mtype",1);
                 map.put("rtype",2);
                 map.put("sender",0);
                 messageBatchService.insertMessageAll(map);
                 */

                result.put("msg", "推送成功");
                result.put("success", 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询出现错误!",e);
            result.put("msg", "推送失败");
            result.put("success", 400);
        }
        return result;
    }

}

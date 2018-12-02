package com.planet.ordorder.service;

import com.planet.common.Constant;
import com.planet.common.sms.SmsService;
import com.planet.common.util.DateTools;
import com.planet.idrule.service.IdRuleService;
import com.planet.message.service.MessageBatchService;
import com.planet.ordorder.dao.OrdOrderMapper;
import com.planet.ordorder.domain.OrdOrder;
import com.planet.ordpreorder.domain.OrdPreOrder;
import com.planet.ordpreorder.service.OrdPreOrderService;
import com.planet.quoquotation.domain.QuoQuotation;
import com.planet.quoquotation.service.QuoQuotationService;
import com.planet.reqrequirement.domain.ReqRequirement;
import com.planet.reqrequirement.service.ReqrequirementService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import com.planet.vo.OrdOrderDetailVo;
import com.planet.vo.OrdOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Li on 2016/2/14.
 */
@Service
@Transactional
public class OrdOrderServiceimpl implements OrdOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrdOrderServiceimpl.class);

    @Autowired
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private IdRuleService idRuleService;

    @Autowired
    private OrdPreOrderService ordPreOrderService;

    @Autowired
    private MessageBatchService messageBatchService;

    @Autowired
    private QuoQuotationService quotationService;

    @Autowired
    private ReqrequirementService reqrequirementService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteByPrimaryKey(String oid) throws Exception {
        int i = 0;
        i = ordOrderMapper.deleteByPrimaryKey(oid);
        if (i == 0) {
            logger.info("订单删除失败");
        } else if (i == 1) {
            logger.info("订单删除成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insert(OrdOrder ordOrder) throws Exception {
        int i = 0;
        i = ordOrderMapper.insert(ordOrder);
        if (i == 0) {
            logger.info("订单插入失败");
        } else if (i == 1) {
            logger.info("订单插入成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertSelective(OrdOrder ordOrder) throws Exception {
        int i = 0;
        String oId = idRuleService.selectByNumName("D");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        Date date = sdf.parse(time);
        ordOrder.setOid(oId);
        ordOrder.setCreatedate(date);
        ordOrder.setStatus(0);
        i = ordOrderMapper.insertSelective(ordOrder);
        if (i == 0) {
            logger.info("生成订单失败");
        } else if (i == 1) {
            logger.info("生成订单成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrdOrder selectByPrimaryKey(String oid) throws Exception {
        return ordOrderMapper.selectByPrimaryKey(oid);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(OrdOrder ordOrder) throws Exception {
        int i = 0;
        i = ordOrderMapper.updateByPrimaryKeySelective(ordOrder);
        if (i == 0) {
            logger.info("更新订单失败");
        } else if (i == 1) {
            logger.info("更新订单成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKey(OrdOrder ordOrder) throws Exception {
        int i = 0;
        i = ordOrderMapper.updateByPrimaryKey(ordOrder);
        if (i == 0) {
            logger.info("更新订单失败");
        } else if (i == 1) {
            logger.info("更新订单成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<OrdOrderVo> listPageSelectOrder(Map map) throws Exception {
        return ordOrderMapper.listPageSelectOrder(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrdOrderDetailVo selectByOid(String oid) throws Exception {
        OrdOrderDetailVo ordOrderDetailVo = new OrdOrderDetailVo();
        List<OrdPreOrder> ordPreOrder = ordPreOrderService.selectByOid(oid);
        OrdOrder ordOrder = ordOrderMapper.selectByPrimaryKey(oid);
        if (ordPreOrder != null && ordPreOrder.size() > 0) {
            ordOrder.setQid(ordPreOrder.get(0).getQid());
        }
        ordOrderDetailVo.setOrdOrder(ordOrder);
        ordOrderDetailVo.setOrdPreOrders(ordPreOrder);

        return ordOrderDetailVo;
    }

    /**
     * 订单完成确认
     *
     * @param oid
     * @return
     * @throws Exception
     */
    @Override
    public int firmOrder(String oid) throws Exception {
        int i = 0;
        OrdOrder ordOrder = ordOrderMapper.selectByPrimaryKey(oid);
        List<OrdPreOrder> ordPreOrders = new ArrayList<>();
        BigDecimal orderAmount = new BigDecimal(0);
        if (ordOrder.getStatus() != Constant.ORDER_STATUS_FINISH) {
            ordOrder.setOid(oid);
            ordOrder.setStatus(Constant.ORDER_STATUS_FINISH);//订单完成
            ordOrderMapper.updateByPrimaryKeySelective(ordOrder);
            ordPreOrders = ordPreOrderService.selectByOid(oid);
            for (Iterator iterator = ordPreOrders.iterator(); iterator.hasNext(); ) {
                int k = 0;
                OrdPreOrder ordPreOrder = new OrdPreOrder();
                ordPreOrder = (OrdPreOrder) iterator.next();
                ordPreOrder.setStatus(Constant.PREORDER_FINISH);//更新状态为5
                k = ordPreOrderService.updateByPrimaryKeySelective(ordPreOrder);
                if (null != ordPreOrder.getQuoPrice() && ordPreOrder.getQuoPrice().compareTo(new BigDecimal(0)) > 0) {
                    orderAmount = orderAmount.add(ordPreOrder.getQuoPrice());
                }
                if (k == 0) {
                    throw new RuntimeException("更新预订单错误");
                }
            }

            ReqRequirement reqRequirement = reqrequirementService.selectByPrimaryKey(ordOrder.getRid());
            reqRequirement.setStatus(Constant.REQUIREMENT_FINISH);  //更新需求订单为完成状态
            reqrequirementService.updateByPrimaryKey(reqRequirement);

            QuoQuotation quoQuotation = quotationService.selectByOid(oid);
            if(null != quoQuotation && 1 == quoQuotation.getStatus()){
                Map<String, Object> point = new HashMap<>();
                point.put("uid", ordOrder.getUid());
                point.put("level","order");
                point.put("oid",ordOrder.getOid());
                point.put("orderAmount", orderAmount.toString());
                userService.updatePoint(point);
            }

            i = 1;
            logger.info("整个订单流程完成。。。");

            //给用户的订单积累金额添加数据
            UserAgent user = userService.selectByPrimaryKey(Integer.parseInt(ordOrder.getUid()));
            BigDecimal amount = user.getOrderAmount();
            if(amount != null){
                amount = amount.add(orderAmount);
                user.setOrderAmount(amount);
            }else{
                user.setOrderAmount(amount);
            }
            userService.updateByPrimaryKey(user);

            Map<String, Object> insertMsgMap = new HashMap<>();
            insertMsgMap.put("title","订单");
            insertMsgMap.put("message", oid + "订单已完成。");
            insertMsgMap.put("mtype", "1");
            insertMsgMap.put("rtype","1");
            insertMsgMap.put("receiver", Integer.valueOf(ordOrder.getUid()));
            insertMsgMap.put("sender", 0);
            int m = messageBatchService.insertMessage(insertMsgMap);
            if (m == 0) {
                logger.info("报价单生成发送消息失败");
                throw new RuntimeException("报价单生成发送消息失败");
            }

                /*     订单完成发短信。。先不发，待讨论，呵呵哒
                UserAgent userAgent=new UserAgent();
                userAgent=userService.selectByPrimaryKey(Integer.parseInt(ordOrderMapper.selectByPrimaryKey(oid).getUid()));
                if (userAgent.getTel() !=null){

                    String text = "您的订单："+oid+" 已完成报价，请及时查看。关注微信公众号：kunmajidian，每日特价、产品动态，尽在坤玛。";
                    String info = smsService.sendSms(text, userAgent.getTel());

                }
                */

        } else {
            i = 2;
        }

        return i;
    }

    @Override
    public List<Map> selectAllOrderToVo(Map<String, Object> map) throws Exception {
        List<OrdOrderVo> orderList=ordOrderMapper.selectAllOrder(map);
        List<Map> orderVoList =new ArrayList<>();
        if(null!=orderList && orderList.size()>0){
            for (OrdOrderVo oov:orderList){
                Map vo = new HashMap();
                vo.put("oid",oov.getOid());
                vo.put("username", oov.getUsername());
                vo.put("name",oov.getName());
                String vip ="否";
                if(null!=oov.getVip()){
                    vip = oov.getVip()==1 ? "是":"否";
                }
                vo.put("vip",vip);
                String ptype = "-";
                    switch (oov.getPtype()){
                        case 1:
                            ptype="特价";
                            break;
                        case 2:
                            ptype="普通";
                            break;
                    }
                vo.put("ptype",ptype);
                String status="-";
                switch (oov.getStatus()){
                    case 0:
                        status="生成";
                        break;
                    case 1:
                        status="预订单已生成";
                        break;
                    case 2:
                        status="报价单已生成";
                        break;
                    case 3:
                        status="报价单已确认";
                        break;
                    case 5:
                        status="订单完成";
                        break;
                    case -1:
                        status="订单取消";
                        break;
                }
                vo.put("status",status);
                vo.put("opCount",oov.getOpCount());
                vo.put("quoCount",(null!=oov.getQuoCount()&&"0".equals(oov.getQuoCount()))?"无":"有");
                vo.put("createdateString",null!=oov.getCreatedate()? DateTools.parseDateToString(oov.getCreatedate(),"yyyy-MM-dd hh:mm:ss EE"):"");
                orderVoList.add(vo);
            }
        }
        return orderVoList;
    }
}

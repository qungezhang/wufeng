package com.planet.common;

/**
 * @author aiveily
 * @ClassName:Constant
 * @Description:状态常量类
 * @date 2018/8/9
 */
public class Constant {

    /*************************** 订单主状态 ************************************/

    public static final Integer ORDER_STATUS = 0;   //生成

    public static final Integer ORDER_STATUS_PREORDER = 1;   //预订单已生成

    public static final Integer ORDER_STATUS_QUOTATION = 2;   //报价单已生成

    public static final Integer ORDER_STATUS_QUOTATION_CONFIRM = 3;   //报价单已确认

    public static final Integer ORDER_STATUS_CANCEL = -1;   //订单已取消

    public static final Integer ORDER_STATUS_FINISH = 5;   //订单已完成

    /*************************** 需求单状态 ************************************/

    public static final Integer REQUIREMENT_STATUS = 0;     //已生成

    public static final Integer REQUIREMENT_FINISH = 5;     //已完成

    public static final Integer REQUIREMENT_CANCEL = -1;    //已作废

    /*************************** 预订单状态 ************************************/

    public static final Integer PREORDER_STATUS = 0;     //已生成

    public static final Integer PREORDER_FINISH = 5;     //已完成

    public static final Integer PREORDER_CANCEL = -1;     //已作废

    /*************************** 报价单状态 ************************************/

    public static final Integer QUOTATION_STATUS = 0;     //已生成

    public static final Integer QUOTATION_CONFIRMED = 1;     //已确认

    public static final Integer QUOTATION_FINISH = 5;     //已完成

    public static final Integer QUOTATION_CANCEL = -1;     //已作废

    /*************************** 获取能量球的方式 ************************************/


    // 根据能量球的优先级设置： 注册 > 认证 > 交易 > 邀请码注册 > 转发 > 默认

    public static final Integer POINT_BALLOON_BRAND = 6;    //品牌

    public static final Integer POINT_BALLOON_SHARE = 5;    //转发/分享

    public static final Integer POINT_BALLOON_CODE_REGISTER = 4;    //邀请码被注册

    public static final Integer POINT_BALLOON_TRADE = 3;    //交易

    public static final Integer POINT_BALLOON_AUTH = 2;    //认证

    public static final Integer POINT_BALLOON_REGISTER = 1;    //注册


    /*************************** 任务状态 ************************************/

    public static final Integer JOB_STATUS = 0;    //默认

    public static final Integer JOB_STATUS_01 = 1;     //待审核

    public static final Integer JOB_STATUS_02 = 2;     //审核通过

    public static final Integer JOB_STATUS_04 = 4;     //任务关闭

    public static final Integer JOB_STATUS_05 = 5;     //任务完成

    /*************************** 任务领取状态 ************************************/

    public static final Integer JOB_CLAIM_STATUS = 0;    //领取任务

    public static final Integer JOB_DISCARD_STATUS = 1;    //放弃任务

    public static final Integer JOB_FINISH_STATUS = 5;    //任务完成

    /*************************** 任务审核状态 ************************************/

    public static final Integer JOB_EXAMINE_STATUS = 0;    //待审核

    public static final Integer JOB_EXAMINE_STATUS_PASS = 1;    //审核通过

    public static final Integer JOB_EXAMINE_STATUS_NOPASS = 2;    //审核不通过

    /*************************** 积分提现类型 ************************************/

    public static final Integer WITHDRWA_TYPE_ALIPAY = 1; //支付宝

    public static final Integer WITHDRWA_TYPE_WECHAT = 2; //微信

    /*************************** 积分提现状态 ************************************/

    public static final Integer WITHDRAW_STATUS = 0;    //审核中

    public static final Integer WITHDRWA_STATUS_SUCCESS = 1; //已打款

    public static final Integer WITHDRWA_STATUS_FAIL = -1; //打款失败


    /*************************** 推送状态 ************************************/

    public static final Integer MESSAGE_SEND = 1;    //消息

    public static final Integer PRODUCT_SEND = 2;    //产品

    public static final Integer JOB_SEND = 3;    //任务

}

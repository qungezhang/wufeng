package com.planet.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.planet.common.Constant;
import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.common.sms.SmsService;
import com.planet.invoice.domain.Invoice;
import com.planet.invoice.service.InvoiceService;
import com.planet.job.domain.Job;
import com.planet.job.service.JobService;
import com.planet.menuopen.domain.Menuopen;
import com.planet.menuopen.service.MenuopenService;
import com.planet.message.domain.Message;
import com.planet.message.service.MessageBatchService;
import com.planet.message.service.MessageService;
import com.planet.ordorder.domain.OrdOrder;
import com.planet.ordorder.service.OrdOrderService;
import com.planet.ordpreorder.domain.OrdPreOrder;
import com.planet.ordpreorder.service.OrdPreOrderService;
import com.planet.point.domain.PointLog;
import com.planet.quoquotation.domain.QuoQuotation;
import com.planet.quoquotation.service.QuoQuotationService;
import com.planet.reqrequirement.service.ReqrequirementService;
import com.planet.suggest.domain.Suggest;
import com.planet.suggest.service.SuggestService;
import com.planet.sysfile.domain.SysFile;
import com.planet.sysfile.service.SysFileService;
import com.planet.user.domain.UserAgent;
import com.planet.user.service.UserService;
import com.planet.utils.HttpUtil;
import com.planet.vip.domain.Vip;
import com.planet.vip.service.VipService;
import com.planet.vo.*;
import com.planet.wechat.Utils.WeChatConf;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 个人中心
 * Created by Li on 2016/1/24.
 */
@Controller
@RequestMapping("/personal")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private OrdPreOrderService ordPreOrderService;

    @Autowired
    private QuoQuotationService quoQuotationService;

    @Autowired
    private VipService vipService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MessageBatchService messageBatchService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ReqrequirementService reqrequirementService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private JobService jobService;

    @Autowired
    private MenuopenService menuopenService;



    /**
     * @api {post} /personal/register 注册
     * @apiDescription 注册，成功后做登录处理和送积分
     * @apiName userRegister
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} uname *用户名/手机号码
     * @apiParam {String} name *姓名
     * @apiParam {String} pwd *明文密码
     * @apiParam {String} referralcode 邀请码
     *
     * @apiSampleRequest /personal/register
     *
     * @apiSuccess (success 200) {Object} result 响应内容
     * @apiSuccess (success 200) {Number} result.code 响应码
     * @apiSuccess (success 200) {String} result.message 响应信息
     * @apiSuccess (success 200) {String} result.success 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "result":
     *          {
     *              "code":200,
     *              "success":"ok",
     *              "message":"ok"
     *          }
     * }
     *
     * @apiError (error 400) {Object} result 响应内容
     * @apiError (error 400) {Number} result.code 400-注册失败/该账号已注册
     * @apiError (error 400) {String} result.success 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "result":
     *          {
     *              "code":400,
     *              "success":"注册失败"
     *          }
     * }
     */
    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> userRegister(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> model = new HashMap<>();
        Map<String, Object> point = new HashMap<>();//积分用
        UserAgent userAgent = new UserAgent();
        UserAgent selectUserAgent = new UserAgent();
        UserAgent referralUser ;   //推荐人
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        int i = 0;
        try {
            String uname = request.getParameter("uname");
            String name = request.getParameter("name");
            String referralcode = request.getParameter("referralcode");
            selectUserAgent = userService.selectByUserName(uname);
            if (selectUserAgent != null) {
                model.put("code", 400);
                model.put("success", "该账号已注册");
                map.put("result", model);
                return map;
            } else {

                String pwd = request.getParameter("pwd");
                userAgent.setPassword(pwd);
                userAgent.setUsername(uname);
                userAgent.setName(name);
                userAgent.setTel(uname);
                userAgent.setVip(0);
                userAgent.setStatus(1);
                Date date = sdf.parse(time);
                userAgent.setLogindate(date);
                userAgent.setLastlogindate(date);
                if(!"".equals(referralcode)){
                    UserAgent ug = userService.selectUserAgentByCode(referralcode);
                    userAgent.setReferraluid(null != ug ? ug.getUid() : null);
                }
                logger.info("登陆时间    " + date);
                i = userService.insertSelective(userAgent);
                if (i == 0) {
                    model.put("code", 400);
                    model.put("success", "注册失败");
                } else if (i == 1) {
                    model.put("code", 200);
                    model.put("success", "ok");
                    selectUserAgent = userService.selectByUserName(uname);
                    point.put("uid", selectUserAgent.getUid());
                    point.put("referralcode", referralcode);
                    point.put("level","register");
                    userService.updatePoint(point);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.put("code", 400);
            model.put("success", "注册失败");
            map.put("result", model);
            return map;
        }
        map.put("code", 200);
        map.put("message", "ok");
        map.put("result", model);
        return map;
    }

    /**
     * @api {post} /personal/login 登录
     * @apiDescription 登录，连续登陆送积分
     * @apiName login
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     * @apiParam {String} password *明文密码
     *
     * @apiSampleRequest /personal/login
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 用户信息
     * @apiSuccess (success 200) {Number} result.uid 用户ID
     * @apiSuccess (success 200) {Number} result.iid 发票信息ID，一般为null
     * @apiSuccess (success 200) {Number} result.qid 报价单号，一般为null
     * @apiSuccess (success 200) {String} result.username 用户名
     * @apiSuccess (success 200) {String} result.name 姓名
     * @apiSuccess (success 200) {String} result.password 明文密码
     * @apiSuccess (success 200) {Number} result.vip 是否是vip：1-是，0-否
     * @apiSuccess (success 200) {String} result.openid 微信openid
     * @apiSuccess (success 200) {Number} result.point 积分
     * @apiSuccess (success 200) {String} result.tel 手机、电话
     * @apiSuccess (success 200) {Number} result.logindate 上次登录日期，同lastlogindate
     * @apiSuccess (success 200) {String} result.referralcode 推荐码
     * @apiSuccess (success 200) {Number} result.status 状态：-1-删除，0-冻结，1-正常
     * @apiSuccess (success 200) {Number} result.isEngineer 是否是工程师：1-工程师，0-经纪人
     * @apiSuccess (success 200) {Number} result.lastlogindate 上次登录日期，同logindate
     * @apiSuccess (success 200) {String} result.remark 备注
     * @apiSuccess (success 200) {Number} result.referraluid 邀请用户ID
     * @apiSuccess (success 200) {Number} result.referralUname 邀请用户姓名，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *      {
     *          "uid":217,
     *          "iid":null,
     *          "qid":null,
     *          "username":"13711801782",
     *          "name":"李爱琴",
     *          "password":"212555",
     *          "vip":0,
     *          "openid":null,
     *          "point":50,
     *          "tel":"13711801782",
     *          "logindate":1465660800000,
     *          "referralcode":"K9AD",
     *          "status":1,
     *          "isEngineer":null,
     *          "lastlogindate":1465721500000,
     *          "remark":"东莞市钲威电工设备杨晨晓",
     *          "referraluid":0,
     *          "referralUname":null
     *      },
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-用户名或密码错误
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"用户名或密码错误！",
     *      "result":null,
     *      "code":400
     * }
     */
    @RequestMapping("/login")
    @ResponseBody
    public  Map<String, Object> login(HttpServletRequest request, String username, String password, String identifyCode, String status, String Wechatcode) {
        /**status=0,账号密码登录   status=1,手机验证码登录  status=2,微信登录*/
        //加锁
        Lock lock = new ReentrantLock();
        lock.lock();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map_point= new HashMap<>();
        Map<String,Object> point = new HashMap<>();
        UserAgent userAgent = null;
        int code=0;
        String message="";

        if(StringUtils.isEmpty(status)){
            status = "0";
        }

        try {
            if(!status.equals("2")){
                userAgent = userService.selectByUserName(username);
            }

            if(StringUtils.isEmpty(status)){
                throw new RuntimeException("登录类型不能为空！");
            }else if(status.equals("0")){
                //账号密码登录
                if (userAgent != null && password.endsWith(userAgent.getPassword())) {
                    code = 200;
                    message = "ok";
                } else {
                    throw new RuntimeException("用户名或密码错误！");
                }
            }else if(status.equals("1")){
                //手机验证码登录
                Object identifyCodes = request.getSession().getAttribute("identifyCode");
                if(!StringUtils.isEmpty(identifyCodes)){
                    identifyCodes = identifyCodes.toString();
                    if (identifyCode == null) {
                        code  = 500;
                        message = "请输入验证码";
                    }
                    if (!identifyCodes.equals(identifyCode)) {
                        code = 500;
                        message = "验证码不正确";
                    }
                    userAgent = userService.selectByUserName(username);
                    if(userAgent == null){
                        throw new RuntimeException("账号不存在！");
                    }
                    code = 200;
                    message = "ok";
                }else{
                    throw new RuntimeException("验证码不存在！");
                }
            }else if(status.equals("2")){
                //微信登录

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                //获取access_token
                com.alibaba.fastjson.JSONObject accessToken = getAccessToken(Wechatcode);
                String access_token = accessToken.getString("access_token");
                if(StringUtils.isEmpty(access_token)||"40163".equals(accessToken.getString("errcode"))){
                    code = 300;
                    throw new RuntimeException("code失效!");
                }
                String openid = accessToken.getString("openid");
//                String access_token = accessToken.getString("access_token");
                userAgent = userService.selectByOpenid(openid);
                if(userAgent == null){
                    //获取微信用户信息
                    com.alibaba.fastjson.JSONObject username1 = getUsername(access_token, openid);
                    String name = username1.getString("nickname");
                    UserAgent userAgent1 = new UserAgent();
                    userAgent1.setPassword("111111");
                    userAgent1.setUsername(name);
                    userAgent1.setName(name);
                    userAgent1.setTel("");
                    userAgent1.setVip(0);
                    userAgent1.setStatus(1);
                    userAgent1.setOpenid(openid);
                    Date date = sdf.parse(time);
                    userAgent1.setLogindate(date);
                    userAgent1.setLastlogindate(date);

                    int i = userService.insertSelective(userAgent1);
                    if (i == 0) {
                        code = 400;
                        message = "登录失败";
                    } else if (i == 1) {
                        code = 200;
                        message = "ok";
                        UserAgent selectUserAgent = userService.selectByOpenid(openid);
                        userAgent = selectUserAgent;
                        point.put("uid", selectUserAgent.getUid());
                        point.put("referralcode", "");
                        point.put("level","register");
                        userService.updatePoint(point);

                    }
                }else{
//                    map.put("bindingPhone","0");
                    code = 200;
                    message = "ok";
                }
                if(userAgent.getPoint()==0&&StringUtils.isEmpty(userAgent.getTel())){
                    map.put("bindingPhone","1");
                }else{
                    map.put("bindingPhone","0");
                }
            }

            //判断登录时间
            Date curDate=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.format(curDate);
            Date lastDate = userAgent.getLastlogindate();
            if (lastDate!=null){
                long intervalMilli = curDate.getTime() - lastDate.getTime();
                int iday=(int) (intervalMilli / (24 * 60 * 60 * 1000));
                if (iday>1){
                    map_point.put("uid", userAgent.getUid());
                    map_point.put("message", "连续登陆送积分");
                    map_point.put("level","login");
                    userService.updatePoint(map_point);
                }
            }
        } catch (Exception e) {
            userAgent = null;
            code = 400;
            message = e.getMessage();
        }finally {
            lock.unlock();
        }


        List<Menuopen> menuopens = menuopenService.getmenuopenList(new HashMap<String, Object>());
        if(!menuopens.isEmpty()){
            map.put("jifenOpen",menuopens.get(0).getIsOpen());
        }else{
            map.put("jifenOpen","1");
        }
        map.put("code", code);
        map.put("message", message);
        map.put("result", userAgent);
        return map;
    }

    //通过微信code，获取token
    public com.alibaba.fastjson.JSONObject getAccessToken(String code){
        String url = WeChatConf.get_access_token + "?appid="+WeChatConf.LOGIN_APPID+"&secret="+WeChatConf.LOGIN_APPSECRET+"&code="+code+"&grant_type=authorization_code";
        String s = HttpUtil.get(url);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
//        jsonObject.getString("access_token");
        return jsonObject;
    }

    //刷新或续期access_token
//    public String refreshToken(){
//
//    }

    //获取用户信息
    public com.alibaba.fastjson.JSONObject getUsername(String token,String openid){
        String url = WeChatConf.get_user_info+"?access_token="+token+"&openid="+openid;
        String s = HttpUtil.get(url);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
        return jsonObject;
    }

    /**
     * 绑定手机号
     * @param openid
     * @param username
     * @return
     */
    @RequestMapping("/bindingPhone")
    @ResponseBody
    public Map<String,Object> bindingPhone(HttpServletRequest request,String identifyCode,String openid,String username){
        Map<String,Object> map = new HashMap<>();
        int code=0;
        String message="";
        UserAgent userAgent2 = null;

        if(StringUtils.isEmpty(openid)||StringUtils.isEmpty(username)){
            throw new RuntimeException("标识或手机号码不能为空!");
        }

        try {
            //获取手机验证码
            Object identifyCodes = request.getSession().getAttribute("identifyCode");
            if(StringUtils.isEmpty(identifyCodes)){
                throw new RuntimeException("验证码不存在！");
            }
            if(!identifyCodes.equals(identifyCode)){
                throw new RuntimeException("验证码不正确！");
            }
            UserAgent userAgent = userService.selectByUserName(username);
            UserAgent userAgent1 = userService.selectByOpenid(openid);
            if(userAgent == null && userAgent1 == null){
                throw new RuntimeException("账号不存在！");
            }
            if(userAgent == null){
                userAgent2 = userAgent1;
                userAgent1.setUsername(username);
                userAgent1.setPassword(username);
                userAgent1.setTel(username);
                int i = userService.updateByPrimaryKey(userAgent1);
                if(i==0){
                    code = 500;
                    message = "绑定失败";
                }else{
                    code = 200;
                    message = "绑定成功";
                }
            }else{

                if(StringUtils.isEmpty(userAgent.getOpenid())){
                    userAgent.setOpenid(openid);
                    int i = userService.updateByPrimaryKey(userAgent);
                    if(i==0){
                        code = 500;
                        message = "绑定失败";
                    }else{
                        userAgent2 = userAgent;
                        userService.deleteByPrimaryKey(userAgent1.getUid());
                        code = 200;
                        message = "绑定成功";
                    }
                }else{
                    code = 500;
                    message = "绑定失败,该手机号已被绑定";
                }
            }
        } catch (Exception e) {
            code = 400;
            message = e.getMessage();
        }

        map.put("code", code);
        map.put("message", message);
        map.put("result", userAgent2);
        return map;
    }

    /**
     * @api {post} /personal/getUser 获取用户信息
     * @apiDescription 根据用户名获取用户信息
     * @apiName getUser
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     *
     * @apiSampleRequest /personal/getUser
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 用户信息
     * @apiSuccess (success 200) {Number} result.uid 用户ID
     * @apiSuccess (success 200) {Number} result.iid 发票信息ID，一般为null
     * @apiSuccess (success 200) {Number} result.qid 报价单号，一般为null
     * @apiSuccess (success 200) {String} result.username 用户名
     * @apiSuccess (success 200) {String} result.name 姓名
     * @apiSuccess (success 200) {String} result.password 明文密码
     * @apiSuccess (success 200) {Number} result.vip 是否是vip：1-是，0-否
     * @apiSuccess (success 200) {String} result.openid 微信openid
     * @apiSuccess (success 200) {Number} result.point 积分
     * @apiSuccess (success 200) {String} result.tel 手机、电话
     * @apiSuccess (success 200) {Number} result.logindate 上次登录日期，同lastlogindate
     * @apiSuccess (success 200) {String} result.referralcode 推荐码
     * @apiSuccess (success 200) {Number} result.status 状态：-1-删除，0-冻结，1-正常
     * @apiSuccess (success 200) {Number} result.isEngineer 是否是工程师：1-工程师，0-经纪人
     * @apiSuccess (success 200) {Number} result.lastlogindate 上次登录日期，同logindate
     * @apiSuccess (success 200) {String} result.remark 备注
     * @apiSuccess (success 200) {Number} result.referraluid 邀请用户ID
     * @apiSuccess (success 200) {Number} result.referralUname 邀请用户姓名，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *      {
     *          "uid":217,
     *          "iid":null,
     *          "qid":null,
     *          "username":"13711801782",
     *          "name":"李爱琴",
     *          "password":"212555",
     *          "vip":0,
     *          "openid":null,
     *          "point":50,
     *          "tel":"13711801782",
     *          "logindate":1465660800000,
     *          "referralcode":"K9AD",
     *          "status":1,
     *          "isEngineer":null,
     *          "lastlogindate":1465721500000,
     *          "remark":"东莞市钲威电工设备杨晨晓",
     *          "referraluid":0,
     *          "referralUname":null
     *      },
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-获取用户信息错误
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"异常信息",
     *      "result":null,
     *      "code":400
     * }
     */
    @RequestMapping("/getUser")
    @ResponseBody
    public Map<String, Object> getUser(String username) {
        Map <String,Object>resp = new HashMap<>();
        String message="ok";
        Integer code=200;
        UserAgent userAgent = null;
        try {
            userAgent = userService.selectByUserName(username);
            Assert.notNull(userAgent,"用户信息不存在！");
        } catch (Exception e) {
            logger.error("获取用户信息错误",e);
            code = 400;
            message = e.getMessage();
        }

        List<Menuopen> menuopens = menuopenService.getmenuopenList(new HashMap<String, Object>());
        if(!menuopens.isEmpty()){
            resp.put("jifenOpen",menuopens.get(0).getIsOpen());
        }else{
            resp.put("jifenOpen","1");
        }
        resp.put("code", code);
        resp.put("message", message);
        resp.put("result", userAgent);
        return resp;
    }

    /**
     * @api {post} /personal/getDetailOrder 获取订单详情
     * @apiDescription 获取订单详情，包括订单详情、需求详情和预订单列表
     * @apiName getDetailOrder
     * @apiGroup order
     * @apiVersion 1.0.0
     *
     * @apiParam {String} oid *订单ID
     *
     * @apiSampleRequest /personal/getDetailOrder
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 订单信息
     * @apiSuccess (success 200) {Object} result.ordOrder 订单信息
     * @apiSuccess (success 200) {String} result.ordOrder.oid 订单ID
     * @apiSuccess (success 200) {String} result.ordOrder.poid 预订单号
     * @apiSuccess (success 200) {String} result.ordOrder.qid 报价单号
     * @apiSuccess (success 200) {String} result.ordOrder.rid 需求ID
     * @apiSuccess (success 200) {String} result.ordOrder.uid 用户ID
     * @apiSuccess (success 200) {String} result.ordOrder.suid 审核人ID
     * @apiSuccess (success 200) {String} result.ordOrder.remark 备注
     * @apiSuccess (success 200) {Number} result.ordOrder.updatedate 更新时间
     * @apiSuccess (success 200) {Number} result.ordOrder.updateid ？
     * @apiSuccess (success 200) {Number} result.ordOrder.createdate 创建时间
     * @apiSuccess (success 200) {Number} result.ordOrder.status 订单状态：-1-订单已取消，
     * 0-订单已提交，1-订单受理中，2-报价单已生成，3-报价单已确认，5-报价已完成
     * @apiSuccess (success 200) {String} result.ordOrder.stay1 保留字段1
     * @apiSuccess (success 200) {String} result.ordOrder.stay2 保留字段2
     * @apiSuccess (success 200) {String} result.ordOrder.upDateDateString 更新时间String格式，如：2016-06-12 16:56:45
     * @apiSuccess (success 200) {String} result.ordOrder.createdateString 创建时间String格式，如：2016-06-12 16:56:45
     * @apiSuccess (success 200) {Array} result.ordPreOrders 预订单列表
     * @apiSuccess (success 200) {String} result.ordPreOrders.poid 预订单ID
     * @apiSuccess (success 200) {String} result.ordPreOrders.rid 需求ID
     * @apiSuccess (success 200) {Number} result.ordPreOrders.uid 用户ID
     * @apiSuccess (success 200) {String} result.ordPreOrders.oid 订单ID
     * @apiSuccess (success 200) {String} result.ordPreOrders.qid 报价单号
     * @apiSuccess (success 200) {String} result.ordPreOrders.productname 产品名称
     * @apiSuccess (success 200) {Number} result.ordPreOrders.price 产品单价
     * @apiSuccess (success 200) {Number} result.ordPreOrders.quoPrice 报价单价
     * @apiSuccess (success 200) {Number} result.ordPreOrders.qty 预定数量
     * @apiSuccess (success 200) {String} result.ordPreOrders.remark 备注
     * @apiSuccess (success 200) {String} result.ordPreOrders.updatedate 更新时间
     * @apiSuccess (success 200) {String} result.ordPreOrders.updateid ？
     * @apiSuccess (success 200) {Number} result.ordPreOrders.status 订单状态：
     * 0-预订单已生成，1-待确认，2-已确认，5-预订单已完成（生成正式订单）
     * @apiSuccess (success 200) {String} result.ordPreOrders.sortname 品类名称
     * @apiSuccess (success 200) {String} result.ordPreOrders.brandname 品牌名称
     * @apiSuccess (success 200) {String} result.ordPreOrders.modelname 型号
     * @apiSuccess (success 200) {String} result.ordPreOrders.seriesname 系列
     * @apiSuccess (success 200) {String} result.ordPreOrders.deliverytime 发货时间
     * @apiSuccess (success 200) {Number} result.ordPreOrders.stock 库存
     * @apiSuccess (success 200) {Number} result.ordPreOrders.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiSuccess (success 200) {String} result.ordPreOrders.upDateDateString 更新时间String格式，如：2016-06-12 16:56:45
     * @apiSuccess (success 200) {Object} result.requirement 需求信息
     * @apiSuccess (success 200) {Object} result.requirement.reqRequirement 需求信息
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.rid 需求ID
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.psid 销售产品ID
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.uid 用户ID
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.sortname 品类名称
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.brandname 品牌名称
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.modelname 型号
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.price 产品单价
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.qty 预定数量
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.remark 备注
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.createdate 创建时间
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.updatedate 更新时间
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.updateid ？
     * @apiSuccess (success 200) {Number} result.requirement.reqRequirement.status 需求状态：0-生成，1-完成，-1-作废
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.seriesname 系列
     * @apiSuccess (success 200) {String} result.requirement.reqRequirement.fileid 附件fileid
     * @apiSuccess (success 200) {Array} result.requirement.sysFiles 需求附件
     * @apiSuccess (success 200) {String} result.requirement.sysFiles.id 附件ID
     * @apiSuccess (success 200) {String} result.requirement.sysFiles.fileid 附件fileid
     * @apiSuccess (success 200) {String} result.requirement.sysFiles.fileurl 附件文件名
     * @apiSuccess (success 200) {Number} result.requirement.sysFiles.createtime 上传时间
     *
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "result":
     *          {
     *              "ordOrder":
     *                  {
     *                      "oid":"D160500003",
     *                      "poid":null,
     *                      "qid":"Q160500003",
     *                      "rid":"53d99b4b-0a80-44ac-8900-55c7a48805de",
     *                      "uid":"35",
     *                      "suid":"5",
     *                      "remark":null,
     *                      "updatedate":null,
     *                      "updateid":null,
     *                      "createdate":1462240238000,
     *                      "status":3,
     *                      "stay1":null,
     *                      "stay2":null,
     *                      "upDateDateString":null,
     *                      "createdateString":"2016-05-03 09:50:38"
     *                  },
     *              "ordPreOrders":
     *                  [
     *                      {
     *                          "poid":"PD160500007",
     *                          "rid":"53d99b4b-0a80-44ac-8900-55c7a48805de",
     *                          "uid":null,
     *                          "oid":"D160500003",
     *                          "qid":"Q160500003",
     *                          "productname":"test",
     *                          "price":11.00,
     *                          "quoPrice":12.00,
     *                          "qty":12,
     *                          "remark":"",
     *                          "updatedate":null,
     *                          "updateid":null,
     *                          "status":1,
     *                          "sortname":"1",
     *                          "brandname":"22",
     *                          "modelname":"2",
     *                          "seriesname":"22",
     *                          "deliverytime":null,
     *                          "stock":null,
     *                          "ptype":null,
     *                          "upDateDateString":null
     *                      }
     *                  ],
     *              "requirement":
     *                  {
     *                      "reqRequirement":
     *                          {
     *                              "rid":"53d99b4b-0a80-44ac-8900-55c7a48805de",
     *                              "psid":null,
     *                              "uid":35,
     *                              "ptype":3,
     *                              "productname":"",
     *                              "sortname":null,
     *                              "brandname":"",
     *                              "modelname":"",
     *                              "price":null,
     *                              "qty":1,
     *                              "remark":null,
     *                              "createdate":1462240238000,
     *                              "updatedate":null,
     *                              "updateid":null,
     *                              "status":0,
     *                              "seriesname":null,
     *                              "fileid":1605000003
     *                          },
     *                      "sysFiles":
     *                          [
     *                              {
     *                                  "id":67,
     *                                  "fileid":1605000003,
     *                                  "fileurl":"364ec34d-c0f0-4c7d-9ceb-74bcfb92f33e.jpeg",
     *                                  "createtime":1462240238000
     *                              }
     *                          ]
     *                  }
     *          },
     *      "code":200,
     *      "message":"ok"
     * }
     *
     * @apiError (error 400) {Number} code 400-获取错误
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取错误！",
     *      "result":null,
     *      "code":400
     * }
     */
    @RequestMapping("/getDetailOrder")
    @ResponseBody
    public Map<String, Object> getDetailOrder(String oid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        OrdOrderDetailVo ordOrderDetailVo = new OrdOrderDetailVo();
        try {
            ordOrderDetailVo = ordOrderService.selectByOid(oid);
            if (ordOrderDetailVo != null) {
                res.put("ordOrder", ordOrderDetailVo.getOrdOrder());
                res.put("ordPreOrders", ordOrderDetailVo.getOrdPreOrders());
                String rid = ordOrderDetailVo.getOrdOrder().getRid();
                ReqrequirementVo reqrequirementVo = null;
                try {
                    reqrequirementVo = reqrequirementService.selectByRid(rid);
                } catch (Exception e) {
                    //需求单不影响订单的显示,故不做处理
                    logger.error("获取需求单失败", e);
                }
                res.put("requirement", reqrequirementVo);
                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", res);
            } else {
                map.put("code", 400);
                map.put("message", "获取错误！");
                map.put("result", null);
            }
        } catch (Exception e) {
            logger.error("查询订单详情出错:", e);
            map.put("code", 400);
            map.put("message", "获取错误！");
            map.put("result", null);
            return map;
        }

        return map;
    }

    /**
     * @api {post} /personal/getOrderlist 分页获取订单列表
     * @apiDescription 分页获取订单列表
     * @apiName getOrderlist
     * @apiGroup order
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     * @apiParam {String} rows *获取数量，默认10
     * @apiParam {String} page *页码，默认1
     *
     * @apiSampleRequest /personal/getOrderlist
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 订单信息
     * @apiSuccess (success 200) {Number} result.count 订单总数
     * @apiSuccess (success 200) {Array} result.orderList 订单列表
     * @apiSuccess (success 200) {String} result.orderList.oid 订单ID
     * @apiSuccess (success 200) {String} result.orderList.rid 需求ID
     * @apiSuccess (success 200) {String} result.orderList.username 用户名
     * @apiSuccess (success 200) {String} result.orderList.name 姓名
     * @apiSuccess (success 200) {Number} result.orderList.vip 是否是vip：1-是，0-否
     * @apiSuccess (success 200) {Number} result.orderList.status 订单状态：-1-订单已取消，
     * 0-订单已提交，1-订单受理中，2-报价单已生成，3-报价单已确认，5-报价已完成
     * @apiSuccess (success 200) {Number} result.orderList.createdate 创建时间
     * @apiSuccess (success 200) {String} result.orderList.opCount 该订单的预订单数量
     * @apiSuccess (success 200) {String} result.orderList.quoCount 该订单的报价单数量
     * @apiSuccess (success 200) {Number} result.orderList.suid 审核人ID，一般为null
     * @apiSuccess (success 200) {Number} result.orderList.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiSuccess (success 200) {String} result.orderList.createdateString 创建时间String格式，如：2016-06-12 16:56:45
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok！",
     *      "result":
     *      {
     *         "count":1,
     *         "orderList":
     *         [
     *              {
     *                  "oid":"D160600018",
     *                  "rid":"eb156ff2-1a58-4225-bcb2-97abcfe2d3a8",
     *                  "username":"13711801782",
     *                  "name":"李爱琴",
     *                  "vip":0,
     *                  "status":0,
     *                  "createdate":1465721805000,
     *                  "opCount":"0",
     *                  "quoCount":"0",
     *                  "suid":null,
     *                  "ptype":3,
     *                  "createdateString":"2016-06-12 16:56:45"
     *              }
     *          ]
     *      },
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-获取订单出错
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取订单出错！",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/getOrderlist")
    @ResponseBody
    public Map<String, Object> getOrder(HttpServletRequest request, String username, Integer rows, Integer page) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> model = new HashMap();
        List<OrdOrderVo> result = new ArrayList();
        try {
            if (username == null && "".equals(username)) {
                model.put("code", 400);
                model.put("message", "获取订单出错！");
                model.put("result", result);
                return params;
            } else {
                if (rows == null) {
                    rows = 10;
                }
                if (page == null) {
                    page = 1;
                }
                Pagination pagination = new Pagination(rows, page);
                params.put("pagination", pagination);
                params.put("username", username);

                result = ordOrderService.listPageSelectOrder(params);
                Map<String,Object> orderVo = new HashMap<>();
                orderVo.put("orderList",result);
                orderVo.put("count",pagination.getCount());
                model.put("result", orderVo);
                model.put("code", 200);
                model.put("message", "ok！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            model.put("code", 400);
            model.put("message", "获取订单出错！");
            model.put("result", result);
            return model;
        }
        return model;
    }

    /**
     * 删除预订单
     *
     * @param poid
     * @return
     */
    @RequestMapping("/delDetailOrder")
    @ResponseBody
    public Map<String, Object> delDetailOrder(String poid) {
        Map map = new HashMap();
        return map;
    }

    /**
     * 确认预订单
     *
     * @param poid
     * @return
     */
    @RequestMapping("/affirmDetailOrder")
    @ResponseBody
    public Map<String, Object> affirmDetailOrder(String poid, String qty, String quoPrice) {
        Map map = new HashMap();
        OrdPreOrder ordPreOrder = new OrdPreOrder();
        List result = new ArrayList();
        int i = 0;
        ordPreOrder.setPoid(poid);
        ordPreOrder.setQty(Integer.valueOf(qty));
        ordPreOrder.setQuoPrice(new BigDecimal(quoPrice));
        ordPreOrder.setStatus(2);//设置状态为2，预订单已确认
        try {
            i = ordPreOrderService.updateByPrimaryKeySelective(ordPreOrder);
            if (i == 0) {
                map.put("code", 400);
                map.put("message", "确认预订单失败！");
                map.put("result", result);
            } else if (i == 1) {
                map.put("code", 200);
                map.put("message", "确认预订单成功！");
                map.put("result", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "系统异常！");
            map.put("result", result);
        }

        return map;
    }


    /**
     * @api {post} /personal/identifycode 发送短信验证码
     * @apiDescription 发送短信验证码
     * @apiName identifyCode
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} mobile *手机号码：11位数 ，纯数字，最好做电话号码正则校验，判断电话号码是否合法，需要考虑新号段，包含所有号段
     *
     * @apiSampleRequest /personal/identifycode
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Number} result 短信验证码
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":5632,
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-短信发送失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"失败，请重新发送！",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/identifycode")
    @ResponseBody
    public Map<String, Object> identifyCode(HttpServletRequest request, String mobile) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        List result = new ArrayList();
        //修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后获取
        String apikey = "dc1579f1c0d69634115334a906b8e602";
        /**************** 使用智能匹配模版接口发短信(推荐) *****************/
        //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板）
        int x = (int) (Math.random() * 9000 + 1000);
        logger.info("验证码是： " + x);
        String text = "您的验证码是："+x+"，请在页面中输入以完成下一步操作。关注微信公众号：kunmajidian，工业机电自动化 一站式采购中心。";
        try {

            String info = smsService.sendSms(text, mobile);
            logger.info("短信消息返回消息" + info);
            String[] infoArr = info.split(",");
            String codeMessage = infoArr[1];
            String codeResult = null;
            if(null != codeMessage){
                int i = codeMessage.indexOf("\n");
                codeResult = codeMessage.substring(0,i);
            }else{
                codeResult  = codeMessage;
            }
            if(null != codeResult && "0".equals(codeResult)){
                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", x);
                HttpSession session = request.getSession();
                session.setAttribute("identifyCode", String.valueOf(x));
                logger.info("写入session验证码的值为" + String.valueOf(x));
            } else {
                map.put("code", 400);
                map.put("message", "失败，请重新发送！");
                map.put("result", result);
            }
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "失败，请重新发送！");
            map.put("result", result);
            return map;
        }
        return map;
    }

    /**
     * @api {post} /personal/changepw 更新密码
     * @apiDescription 更新密码
     * @apiName changePassWord
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     * @apiParam {String} pw *新密码（明文）
     * @apiParam {String} identifyCode *短信验证码
     *
     * @apiSampleRequest /personal/changepw
     *
     * @apiSuccess (success 200) {Number} code 200-更新成功
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新成功！",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 500-请输入验证码/验证码不正确/查询用户信息出错/更新密码出错
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新密码出错!",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/changepw")
    @ResponseBody
    public Map<String, Object> changePassWord(HttpServletRequest request, String username, String pw, String identifyCode) {
        Map<String, Object> resp = new HashMap<>();
        Object identifyCodes = request.getSession().getAttribute("identifyCode");
        int code;
        String message;
        try {
            if(identifyCodes != null){
                identifyCodes = identifyCodes.toString();
                if (identifyCode == null) {
                    code  = 500;
                    message = "请输入验证码";
                }
                if (!identifyCodes.equals(identifyCode)) {
                    code = 500;
                    message = "验证码不正确";
                }
                UserAgent userAgent;
                userAgent = userService.selectByUserName(username);
                if (userAgent == null) {
                    code = 500;
                    message = "用户不存在";
                }else{
                    userAgent.setPassword(pw);
                    Integer status = userService.updateByPrimaryKeySelective(userAgent);
                    if (status == 0) {
                        code = 500;
                        message = "更新密码出错";
                    }else{
                        code = 200;
                        message = "更新成功！";
                    }
                }
            }else{
                code = 500;
                message = "验证码不存在";
            }


        } catch (Exception e) {
            code = 500;
            message = e.getMessage();
        } finally {
            request.getSession().removeAttribute("identifyCode");
        }
        resp.put("code", code);
        resp.put("message", message);
        resp.put("result", null);
        return resp;
    }

    /**
     * @api {post} /personal/checkuser 检查用户名是否存在
     * @apiDescription 检查用户名是否存在，注册之前要验证输入的用户名/手机号是否已经存在
     * @apiName checkUser
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     *
     * @apiSampleRequest /personal/checkuser
     *
     * @apiSuccess (success 200) {Number} code 200-该用户未被注册
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"该用户未被注册",
     *      "result":[],
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-该用户已被注册/查询出错
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"该用户已被注册",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Map<String, Object> checkUser(String username) {
        Map<String, Object> map = new HashMap<>();
        List list = new ArrayList();
        UserAgent userAgent = new UserAgent();
        try {
            userAgent = userService.selectByUserName(username);
            if (userAgent == null) {
                map.put("code", 200);
                map.put("message", "该用户未被注册");
                map.put("result", list);
            } else {
                map.put("code", 400);
                map.put("message", "该用户已被注册");
                map.put("result", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "查询出错");
            map.put("result", list);
            return map;
        }

        return map;
    }

    /**
     * @api {post} /personal/selectquo 查询报价单
     * @apiDescription 查询报价单
     * @apiName selectQuo
     * @apiGroup quotation
     * @apiVersion 1.0.0
     *
     * @apiParam {String} qid *报价单ID
     *
     * @apiSampleRequest /personal/selectquo
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 报价单信息
     * @apiSuccess (success 200) {Object} result.quoQuotation 报价单信息
     * @apiSuccess (success 200) {String} result.quoQuotation.qid 报价单ID
     * @apiSuccess (success 200) {String} result.quoQuotation.oid 预订单号
     * @apiSuccess (success 200) {String} result.quoQuotation.custname 客户名（用户名）
     * @apiSuccess (success 200) {Number} result.quoQuotation.vip 是否是vip：0-否，1-是
     * @apiSuccess (success 200) {String} result.quoQuotation.remark 备注
     * @apiSuccess (success 200) {Number} result.quoQuotation.createid 生成报价单人ID
     * @apiSuccess (success 200) {Number} result.quoQuotation.createdate 创建时间
     * @apiSuccess (success 200) {Number} result.quoQuotation.uid 客户ID
     * @apiSuccess (success 200) {Number} result.quoQuotation.status 报价单状态：0-报价单生成，1-报价单发送，2-报价单返回，3-报价单完成，-1-作废
     * @apiSuccess (success 200) {Number} result.quoQuotation.backdate 客户返回时间
     * @apiSuccess (success 200) {String} result.quoQuotation.bank 银行名称
     * @apiSuccess (success 200) {String} result.quoQuotation.account 银行账号
     * @apiSuccess (success 200) {String} result.quoQuotation.tax 税号
     * @apiSuccess (success 200) {String} result.quoQuotation.invoicetitle 发票抬头
     * @apiSuccess (success 200) {String} result.quoQuotation.companytel 公司电话
     * @apiSuccess (success 200) {String} result.quoQuotation.receivertel 收件人电话
     * @apiSuccess (success 200) {String} result.quoQuotation.receiver 收件人姓名
     * @apiSuccess (success 200) {String} result.quoQuotation.address 地址
     * @apiSuccess (success 200) {String} result.quoQuotation.backdateString 客户返回时间String格式，如：2016-06-12 16:56:45
     * @apiSuccess (success 200) {String} result.quoQuotation.createdateString 更新时间String格式，如：2016-06-12 16:56:45
     * @apiSuccess (success 200) {Number} result.quoQuotation.iid 发票id
     * @apiSuccess (success 200) {Object} result.ordPreOrder 预订单信息
     * @apiSuccess (success 200) {String} result.ordPreOrder.poid 预订单ID
     * @apiSuccess (success 200) {String} result.ordPreOrder.rid 需求ID
     * @apiSuccess (success 200) {Number} result.ordPreOrder.uid 用户ID
     * @apiSuccess (success 200) {String} result.ordPreOrder.oid 订单ID
     * @apiSuccess (success 200) {String} result.ordPreOrder.qid 报价单号
     * @apiSuccess (success 200) {String} result.ordPreOrder.productname 产品名称
     * @apiSuccess (success 200) {Number} result.ordPreOrder.price 产品单价
     * @apiSuccess (success 200) {Number} result.ordPreOrder.quoPrice 报价单价
     * @apiSuccess (success 200) {Number} result.ordPreOrder.qty 预定数量
     * @apiSuccess (success 200) {String} result.ordPreOrder.remark 备注
     * @apiSuccess (success 200) {String} result.ordPreOrder.updatedate 更新时间
     * @apiSuccess (success 200) {String} result.ordPreOrder.updateid ？
     * @apiSuccess (success 200) {Number} result.ordPreOrder.status 订单状态：0-生成，1-待确认，2-已确认，5-生成正式订单
     * @apiSuccess (success 200) {String} result.ordPreOrder.sortname 品类名称
     * @apiSuccess (success 200) {String} result.ordPreOrder.brandname 品牌名称
     * @apiSuccess (success 200) {String} result.ordPreOrder.modelname 型号
     * @apiSuccess (success 200) {String} result.ordPreOrder.seriesname 系列
     * @apiSuccess (success 200) {String} result.ordPreOrder.deliverytime 产品货期
     * @apiSuccess (success 200) {Number} result.ordPreOrder.stock 库存
     * @apiSuccess (success 200) {Number} result.ordPreOrder.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiSuccess (success 200) {String} result.ordPreOrder.upDateDateString 更新时间String格式，如：2016-06-12 16:56:45
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"查询成功",
     *      "result":
     *          {
     *              "quoQuotation":
     *                  {
     *                      "qid":"Q170300009",
     *                      "oid":"D170300028",
     *                      "custname":null,
     *                      "vip":0,
     *                      "remark":"",
     *                      "createid":9,
     *                      "createdate":1489656739000,
     *                      "uid":460,
     *                      "status":5,
     *                      "backdate":1489705902000,
     *                      "bank":"交通银行无锡马山支行",
     *                      "account":"322000670010010001085",
     *                      "tax":"",
     *                      "invoicetitle":"测试发票"
     *                      "companytel":"",
     *                      "receivertel":"18206196183",
     *                      "receiver":"张爱君",
     *                      "address":"无锡市滨湖区马山湖山路58号",
     *                      "backdateString":"2017-03-17 07:11:42",
     *                      "createdateString":"2017-03-16 17:32:19"
     *                      "iid":"222",
     *                  },
     *              "ordPreOrder":
     *                  [
     *                      {
     *                          "poid":"PD170300014",
     *                          "rid":"b0e4a179-fb2a-445e-acba-fe9a0c2c46a6",
     *                          "uid":null,
     *                          "oid":"D170300028",
     *                          "qid":"Q170300009",
     *                          "productname":"丹佛斯比例阀",
     *                          "price":11000.00,
     *                          "quoPrice":11000.00,
     *                          "qty":1,
     *                          "remark":"丹佛斯比例阀，",
     *                          "updatedate":null,
     *                          "updateid":null,
     *                          "status":5,
     *                          "sortname":"液压类",
     *                          "brandname":"丹佛斯",
     *                          "modelname":"018Z6967",
     *                          "seriesname":"丹佛斯比例阀",
     *                          "deliverytime":"4周",
     *                          "stock":null,
     *                          "ptype":null,
     *                          "upDateDateString":null
     *                  }
     *              ]
     *          },
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-查询失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"查询失败",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/selectquo")
    @ResponseBody
    public Map<String, Object> selectQuo(String qid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        QuoQuotationVo quoQuotationVo = new QuoQuotationVo();
        List result = new ArrayList();
        try {
            quoQuotationVo = quoQuotationService.selectByqId(qid);
            if (quoQuotationVo != null) {
                //result.add(quoQuotationVo);
                model.put("code", 200);
                model.put("message", "查询成功");
                model.put("result", quoQuotationVo);
            } else {
                model.put("code", 400);
                model.put("message", "查询失败");
                model.put("result", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            model.put("code", 400);
            model.put("message", "查询失败");
            model.put("result", result);
        }
        return model;
    }

    /**
     * @api {post} /personal/updatequo 更新报价单
     * @apiDescription 更新报价单（报价确认），设置报价单状态为1，设置订单状态为3，设置公司、税号、签收人等信息。*请求参数以Json格式提交。
     * @apiName updateQuo
     * @apiGroup quotation
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} iid *发票ID
     * @apiParam {String} qid *报价单ID
     * @apiParam {String} uid *用户ID
     * @apiParam {String} jianyi 建议/备注
     * @apiParam {String} ordpreorderid 预订单
     * @apiParam {String} ordpreorderid.poid *预订单ID
     * @apiParam {String} ordpreorderid.price *产品单价
     * @apiParam {String} ordpreorderid.qty *预定数量
     * @apiParam {String} ordpreorderid.quoPrice *报价单价
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *     "iid": "213",
     *     "qid": "Q160300010",
     *     "uid": "825",
     *     "jianyi": "",
     *     "ordpreorderid": [
     *          {
     *              "poid": "PD160400009",
     *              "price": "100.00",
     *              "qty": "11",
     *              "quoPrice": "99.00"
     *          },
     *          {
     *              "poid": "PD160400010",
     *              "price": "200.00",
     *              "qty": "20",
     *              "quoPrice": "199.00"
     *          }
     *     ]
     * }
     *
     * @apiSampleRequest /personal/updatequo
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":233,
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-修改报价单失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"修改报价单失败!",
     *      "result":null,
     *      "code":400
     * }
     */
    @RequestMapping("/updatequo")
    @ResponseBody
    public Map<String, Object> updateQuo(@RequestBody QuoQuoVo quoQuoVo) {
        Map<String, Object> resp = new HashMap<>();
        int code;
        String message;
        try {
            List<OrdPreOrder> preOrders = new ArrayList<>();
            int iid = quoQuoVo.getIid();
            List list = quoQuoVo.getOrdpreorderid();
            JSONArray jsonArray = new JSONArray(list);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = JSONObject.fromObject(jsonArray.getJSONObject(i));
                QuoPriceVo o = (QuoPriceVo) JSONObject.toBean(jsonObject, QuoPriceVo.class);
                OrdPreOrder ordPreOrder = new OrdPreOrder();
                ordPreOrder.setPoid(o.getPoid());
                if (o.getQuoPrice() != null) {
                    ordPreOrder.setQuoPrice(new BigDecimal(o.getQuoPrice()));
                }
                if (o.getQty() != null) {
                    ordPreOrder.setQty(Integer.valueOf(o.getQty()));
                }
                preOrders.add(ordPreOrder);
            }
            QuoQuotationVo quoQuotationVo = new QuoQuotationVo();
            QuoQuotation quotation = new QuoQuotation();
            quotation.setQid(quoQuoVo.getQid());
            quotation.setUid(quoQuoVo.getUid());
            quotation.setRemark(quoQuoVo.getJianyi());
            quotation.setStatus(Constant.QUOTATION_CONFIRMED);

            quoQuotationVo.setOrdPreOrder(preOrders);
            quoQuotationVo.setQuoQuotation(quotation);
            int i = quoQuotationService.updateBySelective(quoQuotationVo);

            Invoice invoice = invoiceService.selectByPrimaryKey(iid);
            quotation.setIid(invoice.getIid());
            quotation.setBank(invoice.getBank());
            quotation.setAccount(invoice.getAccount());
            quotation.setTax(invoice.getTax());
            quotation.setCompanytel(invoice.getCompanytel());
            quotation.setReceivertel(invoice.getReceivertel());
            quotation.setReceiver(invoice.getReceiver());
            quotation.setAddress(invoice.getAddress());
            quotation.setBackdate(new Date());
            quotation.setInvoicetitle(invoice.getTitlename());

            quoQuotationService.updateByPrimaryKeySelective(quotation);
            OrdOrder ordordertmp = new OrdOrder();
            ordordertmp.setStatus(Constant.ORDER_STATUS_QUOTATION_CONFIRM);   //报价确认中

            ordordertmp.setOid(quoQuotationService.selectByPrimaryKey(quoQuoVo.getQid()).getOid());
            ordOrderService.updateByPrimaryKeySelective(ordordertmp);

            if (i == 1) {
                code = 200;
                message = "ok";
                logger.info("修改报价单成功");
            } else {
                logger.info("修改报价单失败");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            logger.info(e.toString());
            code = 400;
            message = "修改报价单失败!";
            logger.info("修改报价单失败");
        }
        resp.put("code", code);
        resp.put("message", message);
        resp.put("result", quoQuoVo.getIid());
        return resp;
    }

    /**
     * @api {post} /personal/updatequo2 更新报价单2
     * @apiDescription 更新报价单（报价确认），设置报价单状态为1，设置订单状态为3，设置公司、税号、签收人等信息。
     * @apiName updateQuo2
     * @apiGroup quotation
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如下requestBody-Example
     *
     * @apiParamExample {json} requestBody-Example:
     *
     * {
     *     "iid": "213",
     *     "qid": "Q160300010",
     *     "uid": "825",
     *     "jianyi": "",
     *     "ordpreorderid": (
     *          {
     *              "poid": "PD160400009",
     *              "price": "100.00",
     *              "qty": "11",
     *              "quoPrice": "99.00"
     *          },
     *          {
     *              "poid": "PD160400010",
     *              "price": "200.00",
     *              "qty": "20",
     *              "quoPrice": "199.00"
     *          }
     *     )
     * }
     *
     * @apiSampleRequest /personal/updatequo2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-修改报价单失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"修改报价单失败!",
     *      "result":null,
     *      "code":400
     * }
     */
    @RequestMapping("/updatequo2")
    @ResponseBody
    public Map<String, Object> updateQuo(@RequestParam String requestBody) {
        if(requestBody.contains("(")||requestBody.contains(")")){
            requestBody = requestBody.replaceAll("\\(","[");
            requestBody = requestBody.replaceAll("\\)","]");
        }
        Map<String, Object> resp = new HashMap<>();
        try {
            QuoQuoVo quoQuoVo = com.alibaba.fastjson.JSONObject.parseObject(requestBody, QuoQuoVo.class);
            resp = this.updateQuo(quoQuoVo);
        } catch (Exception e) {
            resp.put("code", 400);
            resp.put("message", "修改报价单失败!");
        }
        return resp;
    }

    /**
     * @api {post} /personal/applyvip VIP申请
     * @apiDescription VIP申请，*请求参数以Json格式提交。
     * @apiName applyVip
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} uid *用户ID
     * @apiParam {String} username *用户登录名
     * @apiParam {String} content *内容（熟悉品牌：$brand+ &&熟悉产品：$product）
     * @apiParam {String} truename *姓名
     * @apiParam {String} tel *电话
     * @apiParam {String} knowbrand *熟悉品牌
     * @apiParam {String} area *所属区域
     * @apiParam {Number} workdate *从业年限
     * @apiParam {Number} applytype *申请VIP类型：1-产品经理人，2-服务工程师
     * @apiParam {String} file1 *上传身份证正面的fileid
     * @apiParam {String} file2 *上传身份证反面的fileid
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *     "uid": "825",
     *     "username": "13911111111",
     *     "content": "熟悉品牌：西门子&&熟悉产品：西门子",
     *     "truename": "张三",
     *     "tel": "13911111111",
     *     "knowbrand": "西门子",
     *     "area": "江苏无锡",
     *     "workdate": 7,
     *     "applytype": 1,
     *     "file1": "1607000088"
     *     "file2": "1607000087"
     * }
     *
     * @apiSampleRequest /personal/applyvip
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-提交失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"提交失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/applyvip")
    @ResponseBody
    public Map<String, Object> applyVip(@RequestBody Vip vip) {
        System.out.println("输出结构---getFile1---"+vip.getFile1());
        System.out.println("输出结构--getFile2----"+vip.getFile2());

        Map<String, Object> map = new HashMap<>();
        int i = 0;
        try {
            if (vip.getUid() != null) {
                //根据file1查询
                if(null != vip.getFile1()){
                    List<SysFile> SysFile = sysFileService.selectByFileId(Integer.valueOf(vip.getFile1()));
                    if(SysFile.size()>0){
                        vip.setFile1(SysFile.get(0).getFileurl());
                        vip.setFile2(SysFile.get(1).getFileurl());
                    }else{
                        //不做处理
                    }
                }
                i = vipService.insertSelective(vip);
                if (i == 0) {
                    map.put("code", 400);
                    map.put("message", "提交失败");
                    map.put("result", "");
                } else if (i == 1) {
                    map.put("code", 200);
                    map.put("message", "ok");
                    map.put("result", "");
                }
            } else {
                map.put("code", 400);
                map.put("message", "提交失败");
                map.put("result", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            map.put("code", 400);
            map.put("message", "提交失败");
            map.put("result", "");
        }
        return map;
    }

    /**
     * @api {post} /personal/applyvip2 VIP申请2
     * @apiDescription VIP申请
     * @apiName applyVip2
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如下requestBody-Example
     *
     * @apiParamExample {json} requestBody-Example:
     *
     * {
     *     "uid": "825",
     *     "username": "13911111111",
     *     "content": "熟悉品牌：西门子&&熟悉产品：西门子",
     *     "truename": "张三",
     *     "tel": "13911111111",
     *     "knowbrand": "西门子",
     *     "area": "江苏无锡",
     *     "workdate": 7,
     *     "applytype": 1,
     *     "file1": "1607000088"
     *     "file2": "1607000087"
     * }
     *
     * @apiSampleRequest /personal/applyvip2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-提交失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"提交失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/applyvip2")
    @ResponseBody
    public Map<String, Object> applyVip2(@RequestParam String requestBody) {
        Map<String, Object> map = new HashMap<>();
        try {
            Vip vip = com.alibaba.fastjson.JSONObject.parseObject(requestBody, Vip.class);
            map = this.applyVip(vip);
        } catch (Exception e) {
            map.put("code", 400);
            map.put("message", "提交失败");
            map.put("result", "");
        }
        return map;
    }

    /**
     * @api {post} /personal/addinvoice 添加发票
     * @apiDescription 添加发票，*请求参数以Json格式提交。
     * @apiName addInvoice
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam (success 200) {String} custname *客户姓名
     * @apiParam (success 200) {String} titlename *抬头
     * @apiParam (success 200) {String} bank 开户行
     * @apiParam (success 200) {String} account *开户行账号
     * @apiParam (success 200) {String} tax *税号
     * @apiParam (success 200) {String} companytel 公司电话
     * @apiParam (success 200) {String} receiver *签收人
     * @apiParam (success 200) {String} receivertel *签收人电话
     * @apiParam (success 200) {String} address *收件地址
     * @apiParam (success 200) {Number} uid *用户ID
     * @apiParam (success 200) {Number} isdefault *是否默认：0-否，1-是，默认0
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *     "custname": "张三",
     *     "titlename": "坤玛机电有限公司",
     *     "bank": "中国工商银行哈尔滨融汇支行",
     *     "account": "350007140900688895",
     *     "tax": "91230199558282596E",
     *     "companytel": "0451-51876738",
     *     "receiver": "李春华",
     *     "receivertel": "13936690395",
     *     "address": "哈尔滨市哈南工业新城核心区松花路70号",
     *     "uid": 825,
     *     "isdefault": 0
     * }
     *
     * @apiSampleRequest /personal/addinvoice
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "code":200,
     *      "result":2
     * }
     *
     * @apiError (error 400) {Number} code 400-添加发票失败
     * @apiError (error 400) {String} message 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新发票信息失败",
     *      "code":400,
     *      "result":""
     * }
     */
    @RequestMapping("/addinvoice")
    @ResponseBody
    public Map<String, Object> addInvoice(@RequestBody Invoice invoice) throws Exception{
        Map<String, Object> resp = new HashMap<>();
        int code;
        String message;
        try {
            if (invoice.getUid() == null) {
                throw new RuntimeException("添加发票失败");
            } else {

                int i = invoiceService.insertSelective(invoice);
                if(i > 0){
                    code = 200;
                    message = "ok";
                    resp.put("result","");
                }else{
                    code = 400;
                    message = "添加发票失败";
                    resp.put("result","");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            code = 400;
            message = "添加发票失败";
        }
        resp.put("code", code);
        resp.put("message", message);
        return resp;
    }

    /**
     * @api {post} /personal/addinvoice2 添加发票2
     * @apiDescription 添加发票
     * @apiName addInvoice2
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如下requestBody-Example
     *
     * @apiParamExample {json} requestBody-Example:
     *
     * {
     *     "custname": "张三",
     *     "titlename": "坤玛机电有限公司",
     *     "bank": "中国工商银行哈尔滨融汇支行",
     *     "account": "350007140900688895",
     *     "tax": "91230199558282596E",
     *     "companytel": "0451-51876738",
     *     "receiver": "李春华",
     *     "receivertel": "13936690395",
     *     "address": "哈尔滨市哈南工业新城核心区松花路70号",
     *     "uid": 825,
     *     "isdefault": 0
     * }
     *
     * @apiSampleRequest /personal/addinvoice2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "code":200,
     *      "result":233
     * }
     *
     * @apiError (error 400) {Number} code 400-添加发票失败
     * @apiError (error 400) {String} message 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新发票信息失败",
     *      "code":400
     * }
     */
    @RequestMapping("/addinvoice2")
    @ResponseBody
    public Map<String, Object> addInvoice2(@RequestParam String requestBody) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Invoice invoice = com.alibaba.fastjson.JSONObject.parseObject(requestBody, Invoice.class);
            resp = this.addInvoice(invoice);
            resp.put("result",resp.get("result"));
        } catch (Exception e) {
            logger.info(e.toString());
            resp.put("code", 400);
            resp.put("message", "添加发票失败");
        }
        return resp;
    }

    /**
     * @api {post} /personal/selectinvoice 查询发票
     * @apiDescription 根据用户ID获取发票列表
     * @apiName selectInvoice
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiParam {String} uid *用户ID
     *
     * @apiSampleRequest /personal/selectinvoice
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容
     * @apiSuccess (success 200) {Number} result.iid 发票ID
     * @apiSuccess (success 200) {String} result.custname 客户姓名
     * @apiSuccess (success 200) {String} result.titlename 抬头
     * @apiSuccess (success 200) {String} result.bank 开户行
     * @apiSuccess (success 200) {String} result.account 开户行账号
     * @apiSuccess (success 200) {String} result.tax 税号
     * @apiSuccess (success 200) {String} result.companytel 公司电话
     * @apiSuccess (success 200) {String} result.receiver 签收人
     * @apiSuccess (success 200) {String} result.receivertel 签收人电话
     * @apiSuccess (success 200) {String} result.address 收件地址
     * @apiSuccess (success 200) {Number} result.uid 用户ID
     * @apiSuccess (success 200) {Number} result.isdefault 是否默认：0-否，1-是
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *          [
     *              {
     *                  "iid":213,
     *                  "custname":"李春华",
     *                  "titlename":"哈尔滨电缆（集团）有限公司",
     *                  "bank":"中国工商银行哈尔滨融汇支行",
     *                  "account":"350007140900688895",
     *                  "tax":"91230199558282596E",
     *                  "companytel":"0451-51876738",
     *                  "receiver":"李春华",
     *                  "receivertel":"13936690395",
     *                  "address":"哈尔滨市哈南工业新城核心区松花路70号",
     *                  "uid":452,
     *                  "isdefault":0
     *              }
     *          ],
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-查询失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"查询失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/selectinvoice")
    @ResponseBody
    public Map<String, Object> selectInvoice(String uid) {
        Map<String, Object> map = new HashMap<>();
        List<Invoice> invoices = new ArrayList<>();
        try {
            invoices = invoiceService.selectAllByUid(Integer.valueOf(uid));
            if (invoices != null) {
                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", invoices);
            } else {
                map.put("code", 400);
                map.put("message", "查询失败");
                map.put("result", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            map.put("code", 400);
            map.put("message", "查询失败");
            map.put("result", "");
        }
        return map;
    }

    /**
     * @api {post} /personal/delinvoice 删除发票
     * @apiDescription 根据发票ID删除发票
     * @apiName delInvoice
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiParam {String} iid *发票ID
     *
     * @apiSampleRequest /personal/delinvoice
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-删除发票失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"删除发票失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/delinvoice")
    @ResponseBody
    public Map<String, Object> delInvoice(String iid) {
        Map<String, Object> model = new HashMap<>();
        try {
            int i = invoiceService.deleteByPrimaryKey(Integer.valueOf(iid));
            if (i == 0) {
                model.put("code", 400);
                model.put("message", "删除发票失败");
                model.put("result", "");
            } else if (i == 1) {
                model.put("code", 200);
                model.put("message", "ok");
                model.put("result", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            model.put("code", 400);
            model.put("message", "删除发票失败");
            model.put("result", "");
        }
        return model;
    }

    /**
     * @api {post} /personal/updateinvoice 更新发票
     * @apiDescription 更新发票
     * @apiName updateInvoice
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam (success 200) {Number} iid *发票ID
     * @apiParam (success 200) {String} custname *客户姓名
     * @apiParam (success 200) {String} titlename *抬头
     * @apiParam (success 200) {String} bank 开户行
     * @apiParam (success 200) {String} account *开户行账号
     * @apiParam (success 200) {String} tax *税号
     * @apiParam (success 200) {String} companytel 公司电话
     * @apiParam (success 200) {String} receiver *签收人
     * @apiParam (success 200) {String} receivertel *签收人
     * @apiParam (success 200) {String} address *收件地址
     * @apiParam (success 200) {Number} uid *用户ID
     * @apiParam (success 200) {Number} isdefault *是否默认：0-否，1-是
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *      "iid": 213
     *      "custname": "张三",
     *      "titlename": "坤玛机电有限公司",
     *      "bank": "中国工商银行哈尔滨融汇支行",
     *      "account": "350007140900688895",
     *      "tax": "91230199558282596E",
     *      "companytel": "0451-51876738",
     *      "receiver": "李春华",
     *      "receivertel": "13936690395",
     *      "address": "哈尔滨市哈南工业新城核心区松花路70号",
     *      "uid": 825,
     *      "isdefault": 0
     * }
     *
     * @apiSampleRequest /personal/updateinvoice
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 60710-发票ID为空或未知的发票ID，60711-更新发票信息失败（更新记录为0），60712-更新发票信息失败（功能异常）
     * @apiError (error) {String} message 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新发票信息失败",
     *      "code":60712
     * }
     */
    @RequestMapping("/updateinvoice")
    @ResponseBody
    public Map<String, Object> updateInvoice(@RequestBody Invoice invoice) {
        Map<String, Object> resp = new HashMap<>();
        int code;
        String message;
        try {
            if (invoice.getIid() == null) {
                code = 60710;
                message = "未知的发票ID";
            } else {
                int i = invoiceService.updateByPrimaryKeySelective(invoice);
                if (i == 1) {
                    code = 200;
                    message = "ok";
                } else {
                    code = 60711;
                    message = "更新发票信息失败";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            code = 60712;
            message = "更新发票信息失败";
        }
        resp.put("code", code);
        resp.put("message", message);
        return resp;
    }

    /**
     * @api {post} /personal/updateinvoice2 更新发票2
     * @apiDescription 更新发票
     * @apiName updateInvoice2
     * @apiGroup invoice
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如下requestBody-Example
     *
     * @apiParamExample {json} requestBody-Example:
     *
     * {
     *      "iid": 213
     *      "custname": "张三",
     *      "titlename": "坤玛机电有限公司",
     *      "bank": "中国工商银行哈尔滨融汇支行",
     *      "account": "350007140900688895",
     *      "tax": "91230199558282596E",
     *      "companytel": "0451-51876738",
     *      "receiver": "李春华",
     *      "receivertel": "13936690395",
     *      "address": "哈尔滨市哈南工业新城核心区松花路70号",
     *      "uid": 825,
     *      "isdefault": 0
     * }
     *
     * @apiSampleRequest /personal/updateinvoice2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 60710-发票ID为空或未知的发票ID，60711-更新发票信息失败（更新记录为0），60712-更新发票信息失败（功能异常）
     * @apiError (error) {String} message 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新发票信息失败",
     *      "code":60712
     * }
     */
    @RequestMapping("/updateinvoice2")
    @ResponseBody
    public Map<String, Object> updateInvoice2(@RequestParam String requestBody) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Invoice invoice = com.alibaba.fastjson.JSONObject.parseObject(requestBody, Invoice.class);
            resp = this.updateInvoice(invoice);
        } catch (Exception e) {
            logger.info(e.toString());
            resp.put("code", 60712);
            resp.put("message", "更新发票信息失败");
        }
        return resp;
    }

    /**
     * @api {get} /personal/selectMessage 分页获取消息列表
     * @apiDescription 根据用户ID，分页获取消息列表
     * @apiName SelectMessage
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {String} uid *用户ID
     * @apiParam {Number} rows *获取数量，默认10
     * @apiParam {Number} page *页码，默认1
     *
     * @apiSampleRequest /personal/selectMessage
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 消息信息
     * @apiSuccess (success 200) {Number} result.mbid 消息批次ID
     * @apiSuccess (success 200) {String} result.message 消息内容
     * @apiSuccess (success 200) {Number} result.senddate 发送日期
     * @apiSuccess (success 200) {Number} result.mtype 消息类型：1-系统，2-人工
     * @apiSuccess (success 200) {Number} result.status 消息状态：0-删除，1-未删除
     * @apiSuccess (success 200) {Number} result.sender 是否是vip：1-是，0-否
     * @apiSuccess (success 200) {Number} result.receiver 接收人 大部分时间=用户ID
     * @apiSuccess (success 200) {Number} result.isread 已读状态：0-未读，1-已读
     * @apiSuccess (success 200) {String} result.name 未知，一般为null
     * @apiSuccess (success 200) {String} result.flag 未知，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *          [
     *              {
     *                  "mbid":712,
     *                  "message":"D170300011订单已完成。",
     *                  "senddate":1488788376000,
     *                  "mtype":0,
     *                  "status":1,
     *                  "msgid":752,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              },
     *              {
     *                  "mbid":711,
     *                  "message":"D170300011订单，报价单已生成。",
     *                  "senddate":1488788335000,
     *                  "mtype":0,
     *                  "status":1,
     *                  "msgid":751,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              },
     *              {
     *                  "mbid":710,
     *                  "message":"用户注册赠送50积分",
     *                  "senddate":1488786323000,
     *                  "mtype":1,
     *                  "status":1,
     *                  "msgid":750,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              }
     *          ],
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-获取订单出错
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"查询发生异常",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/selectMessage")
    @ResponseBody
    public Map<String, Object> SelectMessage(@RequestParam String uid, @RequestParam Integer rows, @RequestParam Integer page) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> resp = new HashMap<>();
        List<MessageVo> messages = new ArrayList<>();
        int code;
        String msg;
        if (rows == null) {
            rows = 10;
        }
        if (page == null) {
            page = 1;
        }
        Pagination pagination = new Pagination(rows, page);
        params.put("pagination", pagination);
        params.put("receiver", uid);
        try {
            // TODO
            messages = messageService.listPageSelectForUser(params);
            code = 200;
            msg = "ok";
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            code = 400;
            msg = "查询发生异常";
        }
        resp.put("code", code);
//        resp.put("count",messag)
        resp.put("message", msg);
        resp.put("result", messages);
        return resp;
    }


    /**
     * @api {get} /personal/getMessagelist 分页获取消息列表,PC端分页使用
     * @apiDescription 根据用户ID，获取分页获取消息列表
     * @apiName getMessagelist
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户ID
     * @apiParam {Number} rows *获取数量，默认10
     * @apiParam {Number} page *页码，默认1
     *
     * @apiSampleRequest /personal/selectMessage
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 消息信息
     * @apiSuccess (success 200) {Number} result.mbid 消息批次ID
     * @apiSuccess (success 200) {String} result.message 消息内容
     * @apiSuccess (success 200) {Number} result.senddate 发送日期
     * @apiSuccess (success 200) {Number} result.mtype 消息类型：1-系统，2-人工
     * @apiSuccess (success 200) {Number} result.status 消息状态：0-删除，1-未删除
     * @apiSuccess (success 200) {Number} result.sender 是否是vip：1-是，0-否
     * @apiSuccess (success 200) {Number} result.receiver 接收人 大部分时间=用户ID
     * @apiSuccess (success 200) {Number} result.isread 已读状态：0-未读，1-已读
     * @apiSuccess (success 200) {String} result.name 未知，一般为null
     * @apiSuccess (success 200) {String} result.flag 未知，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *          [
     *              {
     *                  "mbid":712,
     *                  "message":"D170300011订单已完成。",
     *                  "senddate":1488788376000,
     *                  "mtype":0,
     *                  "status":1,
     *                  "msgid":752,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              },
     *              {
     *                  "mbid":711,
     *                  "message":"D170300011订单，报价单已生成。",
     *                  "senddate":1488788335000,
     *                  "mtype":0,
     *                  "status":1,
     *                  "msgid":751,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              },
     *              {
     *                  "mbid":710,
     *                  "message":"用户注册赠送50积分",
     *                  "senddate":1488786323000,
     *                  "mtype":1,
     *                  "status":1,
     *                  "msgid":750,
     *                  "sender":0,
     *                  "receiver":null,
     *                  "isread":0,
     *                  "name":null,
     *                  "flag":null
     *              }
     *          ],
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-获取订单出错
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {Array} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"查询发生异常",
     *      "result":[],
     *      "code":400
     * }
     */
    @RequestMapping("/getMessagelist")
    @ResponseBody
    public Map<String, Object> getMessagelist(HttpServletRequest request, String uid, Integer rows, Integer page) {
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> messageVo = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            List<MessageVo> result = new ArrayList();
            if (uid == null && "".equals(uid)) {
                resultMap.put("code", 400);
                resultMap.put("message", "获取消息出错！");
                resultMap.put("result", result);
                return resultMap;
            } else {
                if (rows == null) {
                    rows = 10;
                }
                if (page == null) {
                    page = 1;
                }
                Pagination pagination = new Pagination(rows, page);
                params.put("pagination", pagination);
                params.put("receiver", uid);

                result = messageService.listPageSelectForUser(params);
                messageVo.put("messageList",result);
                messageVo.put("count",pagination.getCount());
                resultMap.put("result", messageVo);
                resultMap.put("code", 200);
                resultMap.put("message", "ok！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            resultMap.put("code", 400);
            resultMap.put("message", "获取消息出错！");
            resultMap.put("result", messageVo);
            return resultMap;
        }
        return resultMap;
    }

    /**
     * @api {post} /personal/updateMsgStatus 设置消息为已读
     * @apiDescription 设置消息为已读
     * @apiName UpdateMsgStatus
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {String} mSgid *消息ID
     *
     * @apiSampleRequest /personal/updateMsgStatus
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-修改状态失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"修改状态失败，请重试",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("updateMsgStatus")
    @ResponseBody
    public Map<String, Object> UpdateMsgStatus(String mSgid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        int i = 0;
        Message message = new Message();
        message.setMsgid(Integer.valueOf(mSgid));
        message.setIsread(1);
        try {
            i = messageService.updateByPrimaryKeySelective(message);
            if (i == 0) {
                map.put("code", 400);
                map.put("message", "修改状态失败，请重试");
                map.put("result", "");
            } else if (i == 1) {
                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", "");
            }
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "修改状态失败，请重试");
            map.put("result", "");

        }
        return map;

    }

    /**
     * @api {post} /personal/suggest 反馈意见
     * @apiDescription 反馈意见，*请求参数以Json格式提交。
     * @apiName Suggest
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} suggest *建议内容
     * @apiParam {Number} uid *用户ID
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *      "uid": 825,
     *      "suggest": "您的建议内容"
     * }
     *
     * @apiSampleRequest /personal/suggest
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-发送建议失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"发送建议失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/suggest")
    @ResponseBody
    public Map<String, Object> Suggest(@RequestBody Suggest suggest) {
        Map<String, Object> map = new HashMap<>();
        int i = 0;
        try {
            i = suggestService.insertSelective(suggest);
            if (i == 0) {
                map.put("code", 400);
                map.put("message", "发送建议失败");
                map.put("result", "");
            } else if (i == 1) {
                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", "");
            }
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "发送建议失败");
            map.put("result", "");
        }
        return map;
    }

    /**
     * @api {post} /personal/suggest2 反馈意见2
     * @apiDescription 反馈意见。
     * @apiName Suggest2
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如：{"uid": 825,"suggest": "您的建议内容"}
     *
     * @apiSampleRequest /personal/suggest2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {String} result 响应内容，一般为空
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":"",
     *      "code":200
     * }
     *
     * @apiError (error 400) {Number} code 400-发送建议失败
     * @apiError (error 400) {String} message 响应信息
     * @apiError (error 400) {String} result 响应内容，一般为空
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"发送建议失败",
     *      "result":"",
     *      "code":400
     * }
     */
    @RequestMapping("/suggest2")
    @ResponseBody
    public Map<String, Object> Suggest2(@RequestParam String requestBody) {
        Map<String, Object> map = new HashMap<>();
        try {
            Suggest suggest = com.alibaba.fastjson.JSONObject.parseObject(requestBody, Suggest.class);
            map = Suggest(suggest);
        } catch (Exception e) {
            map.put("code", 400);
            map.put("message", "发送建议失败");
            map.put("result", "");
        }
        return map;
    }


    /**
     * @api {get} /personal/getJobList 获取我的任务列表（PC端用)
     * @apiDescription 根据用户id获取该用户的任务列表
     * @apiName getJobList
     * @apiGroup personal
     * @apiVersion 2.0.0
     *
     * @apiParam {Number} uid 用户主键ID，必填
     * @apiParam {Number} rows 当前页数据个数，必填
     * @apiParam {Number} page 当前页数，必填
     *
     * @apiSampleRequest /personal/getJobList
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {JSONArray} result 响应内容,任务列表集合
     * @apiSuccess (success 200) {String} result.jid 任务主键
     * @apiSuccess (success 200) {String} result.name 任务名称
     * @apiSuccess (success 200) {String} result.subhead 副标题
     * @apiSuccess (success 200) {Date} result.startTime 开始时间
     * @apiSuccess (success 200) {Date} result.endTime 结束时间
     * @apiSuccess (success 200) {String} result.address 地址
     * @apiSuccess (success 200) {String} result.tags 标签
     * @apiSuccess (success 200) {String} result.description 内容
     * @apiSuccess (success 200) {BigDecimal} result.price 价格
     * @apiSuccess (success 200) {Number} result.status  状态：0 待领取；1 待审核；2 审核通过；4 任务关闭；5 任务完成
     *                                                   关于我的任务的几个状态：默认（待审核）
     *                                                                      if status = 2 （审核通过）
     *                                                                      else if status = 4 （任务已下架）
     *                                                                      else if status = 5 （任务已完成）
     *                                                                      else
     *                                                                          if userJobStatus = 1 (放弃任务)
     *                                                                          if examineStatus = 1 （审核不通过）
     * @apiSuccess (success 200) {Date} result.createTime 创建时间
     * @apiSuccess (success 200) {Date} result.modifyTime 修改时间
     * @apiSuccess (success 200) {Number} result.isDelete 删除判断
     * @apiSuccess (success 200) {Number} result.examineId 用户任务主键id
     * @apiSuccess (success 200) {String} result.receiveName 接手任务用户名称
     * @apiSuccess (success 200) {String} result.receiveMobile 接手任务用户手机号
     * @apiSuccess (success 200) {String} result.receiveCardNo 接手任务用户身份证
     * @apiSuccess (success 200) {Date} result.receiveTime 接手任务时间
     * @apiSuccess (success 200) {String} result.examineRemark 接手任务用户审核备注
     * @apiSuccess (success 200) {Number} result.userJobStatus 0-领取任务，1-放弃任务
     * @apiSuccess (success 200) {Number} result.examineStatus 0-审核通过，1-审核不通过
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":
     *         [{
     *             "jid": "R180800001",
     *             "name": "测试",
     *             "subhead": "测试二号",
     *             "startTime": null,
     *             "endTime": null,
     *             "address": "智慧路13号",
     *             "tags": "",
     *             "description": "任务描述",
     *             "price": 22,
     *             "status": 1,
     *             "createTime": 1533895281000,
     *             "modifyTime": 1533896122000,
     *             "isDelete": null,
     *             "examineId": 11,
     *             "receiveName": null,
     *             "receiveMobile": null,
     *             "receiveCardNo": null,
     *             "receiveTime": null,
     *             "examineRemark": null,
     *             "userJobStatus": 0
     *         }],
     *
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 400-获取任务列表信息失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {String} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"获取任务管理信息失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/getJobList")
    @ResponseBody
    public  Map<String, Object> getJobList(HttpServletRequest request, Integer rows, Integer page,Integer uid) {
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> job = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            List<Job> result = new ArrayList();
            if (uid == null && "".equals(uid)) {
                resultMap.put("code", 400);
                resultMap.put("message", "获取任务列表出错！");
                resultMap.put("result", result);
                return resultMap;
            } else {
                if (rows == null) {
                    rows = 10;
                }
                if (page == null) {
                    page = 1;
                }
                Pagination pagination = new Pagination(rows, page);
                params.put("pagination", pagination);
                params.put("uid", uid);

                result = jobService.listPageSelectUserJob(params);
                job.put("jobList",result);
                job.put("count",pagination.getCount());
                resultMap.put("result", job);
                resultMap.put("code", 200);
                resultMap.put("message", "ok！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
            resultMap.put("code", 400);
            resultMap.put("message", "获取任务列表出错！");
            resultMap.put("result", job);
            return resultMap;
        }
        return resultMap;
    }

    /**
     * @api {post} /personal/register2 注册2
     * @apiDescription 注册，成功后做登录处理和送积分（H5端）
     * @apiName userRegister2
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} uname *用户名/手机号码
     * @apiParam {String} name *姓名
     * @apiParam {String} pwd *明文密码
     * @apiParam {String} referralcode 邀请码
     *
     * @apiSampleRequest /personal/register
     *
     * @apiSuccess (success 200) {Object} result 响应内容
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *              "code":200,
     *              "result":null,
     *              "message":"ok"
     * }
     *
     * @apiError (error 400) {Object} result 响应内容
     * @apiError (error 400) {Number} result.code 400-注册失败/该账号已注册
     * @apiError (error 400) {String} result.success 响应信息
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "result":
     *          {
     *              "code":400,
     *              "success":"注册失败"
     *          }
     * }
     */
    @RequestMapping("/register2")
    @ResponseBody
    public Map<String, Object> userRegister2(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> model = new HashMap<>();
        Map<String, Object> point = new HashMap<>();//积分用
        UserAgent userAgent = new UserAgent();
        UserAgent selectUserAgent = new UserAgent();
        UserAgent referralUser ;   //推荐人
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        int i = 0;
        try {
            String uname = request.getParameter("uname");
            String name = request.getParameter("name");
            String referralcode = request.getParameter("referralcode");
            selectUserAgent = userService.selectByUserName(uname);
            if (selectUserAgent != null) {
                map.put("code", 400);
                map.put("message", "该账号已注册");
                map.put("result", null);
                return map;
            } else {

                String pwd = request.getParameter("pwd");
                userAgent.setPassword(pwd);
                userAgent.setUsername(uname);
                userAgent.setName(name);
                userAgent.setTel(uname);
                userAgent.setVip(0);
                userAgent.setStatus(1);
                Date date = sdf.parse(time);
                userAgent.setLogindate(date);
                userAgent.setLastlogindate(date);
                if(!"".equals(referralcode)){
                    UserAgent ug = userService.selectUserAgentByCode(referralcode);
                    userAgent.setReferraluid(null != ug ? ug.getUid() : null);
                }
                logger.info("登陆时间    " + date);
                i = userService.insertSelective(userAgent);
                if (i == 0) {
                    map.put("code", 400);
                    map.put("message", "注册失败");
                    map.put("result", null);
                    return map;
                } else if (i == 1) {
                    selectUserAgent = userService.selectByUserName(uname);
                    point.put("uid", selectUserAgent.getUid());
                    point.put("referralcode", referralcode);
                    point.put("level","register");
                    userService.updatePoint(point);
                    map.put("code", 200);
                    map.put("message", "ok");
                    map.put("result", null);
                    return map;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 400);
            map.put("success", "注册失败");
            map.put("result", null);
            return map;
        }
        map.put("code", 200);
        map.put("message", "ok");
        map.put("result", null);
        return map;
    }

    /**
     * 获取我的积分
     * @param request
     * @param uid
     * @return
     */
    @RequestMapping("/getMyPoint")
    @ResponseBody
    public   Map<String, Object> getMyPoint(HttpServletRequest request,Integer uid) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> pointMap = new HashMap<>();
        Integer code = 400;
        String message = "查询出错";
        double point = 0.0;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("uid", uid);

            UserAgent userAgent = userService.selectByPrimaryKey(uid);
            if(null != userAgent){
                point = userAgent.getPoint();
            }
            pointMap.put("point",point);
            result.put("result",pointMap);
            code = 200;
            message = "返回成功";
        } catch (Exception e) {
            logger.error("GetJob Error:", e);
            code = 500;
            message ="系统错误";

        }
        result.put("result",pointMap);
        result.put("code",code);
        result.put("message",message);
        return result;
    }

    /**
     * @api {post} /personal/changepw2 更新密码（a安卓）
     * @apiDescription 更新密码
     * @apiName changePassWord2
     * @apiGroup personal
     * @apiVersion 1.0.0
     *
     * @apiParam {String} username *用户名
     * @apiParam {String} pw *新密码（明文）
     *
     * @apiSampleRequest /personal/changepw2
     *
     * @apiSuccess (success 200) {Number} code 200-更新成功
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容，一般为null
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新成功！",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 500-请输入验证码/验证码不正确/查询用户信息出错/更新密码出错
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"更新密码出错!",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/changepw2")
    @ResponseBody
    public Map<String, Object> changePassWord2(HttpServletRequest request, String username, String pw) {
        Map<String, Object> resp = new HashMap<>();
        int code;
        String message;
        try {
            UserAgent userAgent;
            userAgent = userService.selectByUserName(username);
            if (userAgent == null) {
                code = 500;
                message = "用户不存在";
            }else{
                userAgent.setPassword(pw);
                Integer status = userService.updateByPrimaryKeySelective(userAgent);
                if (status == 0) {
                    code = 500;
                    message = "更新密码出错";
                }else{
                    code = 200;
                    message = "更新成功！";
                }
            }

        } catch (Exception e) {
            code = 500;
            message = e.getMessage();
        } finally {
            request.getSession().removeAttribute("identifyCode");
        }
        resp.put("code", code);
        resp.put("message", message);
        resp.put("result", null);
        return resp;
    }


}

package com.planet.wechat.Utils;

/**
 * 微信配置信息
 * Created by junhua on 2016/4/13.
 */
public class WeChatConf {

    //公众号APPID
    public static String APPID = "wx981784dca1f133d1";

    //公众号APPSECRET
    public static String APPSECRET = "14b5a074a16922654816a71dbd6ebe3b";

    //微信服务器Token
    public static String TOKEN = "rongan";

    //商户ID
    public static String MCHID = "1327809201";

    //商户支付密钥Key。审核通过后，在微信发送的邮件中查看
    public static String KEY = "yihaohuapu01yihaohuapu01yihaohua";

    //异步回调地址
    public static String NOTIFY_URL = "http://fortest.tunnel.qydev.com/WeChatPay/pay-notify";

    //微信统一下单接口
    public static String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //微信支付页面url
    public static String url = "http://fortest.tunnel.qydev.com/mobile/index";

    //微信jspai_ticket
    public static String jsapi_ticket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    //微信登录APPID
    public static String LOGIN_APPID = "wx4a858b70aab13755";

    //微信登录APPSECRET
    public static String LOGIN_APPSECRET = "0d465087ab430140e744856ed4dd8b61";

    //获取微信登录token
    public static String get_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token";

    //用来刷新token有效期
    public static String refresh_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    //获取微信用户信息
    public static String get_user_info = "https://api.weixin.qq.com/sns/userinfo";

}

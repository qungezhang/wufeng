package com.planet.point.domain;

import com.planet.common.Constant;

/**
 * @author aiveily
 * @ClassName:PointEnum
 * @Description:能量球积分
 * @date 2018/8/8
 */
public enum PointEnum {

    // 根据能量球的优先级设置： 注册 > 认证 > 交易 > 邀请码注册 > 转发 > 默认

    register_point(Constant.POINT_BALLOON_REGISTER,200),   //注册积分气泡200

    anth_point(Constant.POINT_BALLOON_AUTH,300),  //认证300

    trade_point(Constant.POINT_BALLOON_TRADE,0.01),    //交易；此处为交易额的1%。根据交易订单计算

    code_register_point(Constant.POINT_BALLOON_CODE_REGISTER,10), //邀请码被注册积分气泡10

    share_point(Constant.POINT_BALLOON_SHARE,1),   //转发1

    brand_point(Constant.POINT_BALLOON_BRAND,0.5)    //登录品牌积分气泡0.5
    ;


    private int type;   //类型

    private double point;   //积分

    private PointEnum(int type, double point) {
        this.type = type;
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}

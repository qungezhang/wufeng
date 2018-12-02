package com.planet.user.domain;

import java.math.BigDecimal;
import java.util.Date;

public class UserAgent {
    private Integer uid;

    private Integer iid;

    private String qid;

    private String username;

    private String name;

    private String password;

    private Integer vip;

    private String openid;

    private Double point;

    private String tel;

    private Date logindate;

    private String referralcode;

    private Integer status;

    private Integer isEngineer;

    private Date lastlogindate;

    private String remark;

    private int referraluid;

    private String ReferralUname;

    private BigDecimal orderAmount;//未提现时的订单积累总额

    private Integer serviceNum;//未提现时的维修次数积攒总数

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Integer getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(Integer serviceNum) {
        this.serviceNum = serviceNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastlogindate() {
        return lastlogindate;
    }

    public void setLastlogindate(Date lastlogindate) {
        this.lastlogindate = lastlogindate;
    }
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid == null ? null : qid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Date getLogindate() {
        return logindate;
    }

    public void setLogindate(Date logindate) {
        this.logindate = logindate;
    }

    public String getReferralcode() {
        return referralcode;
    }

    public void setReferralcode(String referralcode) {
        this.referralcode = referralcode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsEngineer() {
        return isEngineer;
    }

    public void setIsEngineer(Integer isEngineer) {
        this.isEngineer = isEngineer;
    }

    public int getReferraluid() {
        return referraluid;
    }

    public void setReferraluid(int referraluid) {
        this.referraluid = referraluid;
    }

    public String getReferralUname() {
        return ReferralUname;
    }

    public void setReferralUname(String referralUname) {
        ReferralUname = referralUname;
    }
}
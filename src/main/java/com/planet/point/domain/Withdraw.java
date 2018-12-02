package com.planet.point.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aiveily
 * @ClassName:Withdraw
 * @Description:提现表
 * @date 2018/8/13
 */
public class Withdraw {

    /**
     * id
     */
    private String wid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 提现类型
     * 1-支付宝 2-微信
     * {@link com.planet.common.Constant}
     */
    private Integer type;

    /**
     * 提现账号
     */
    private String account;

    /**
     * 兑换的积分
     */
    private Double exchangePoint;

    /**
     * 申请提现时间
     */
    private Date createTime;

    /**
     * 提现金额
     */
    private BigDecimal amount;

    /**
     * 提现状态
     */
    private Integer status;

    /**
     * 转账时间
     */
    private Date transferTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提现消耗的类型
     */
    private Integer withdrawConsumeType;

    /**
     * 提现之前的消耗金额
     */
    private BigDecimal withdrawBeforeAmount;


    //非数据库中字段
    /**
     * 用户积分
     */
    private Double point;


    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(Double exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Integer getWithdrawConsumeType() {
        return withdrawConsumeType;
    }

    public void setWithdrawConsumeType(Integer withdrawConsumeType) {
        this.withdrawConsumeType = withdrawConsumeType;
    }

    public BigDecimal getWithdrawBeforeAmount() {
        return withdrawBeforeAmount;
    }

    public void setWithdrawBeforeAmount(BigDecimal withdrawBeforeAmount) {
        this.withdrawBeforeAmount = withdrawBeforeAmount;
    }
}

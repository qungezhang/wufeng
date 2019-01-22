package com.planet.job.domain;

import java.util.Date;

/**
 * @author aiveily
 * @ClassName:UserJob
 * @Description:用户任务表
 * @date 2018/8/10
 */
public class UserJob {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 任务表ID
     */
    private String jobId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 身份证
     */
    private String cardNo;

    /**
     * 接受任务时间
     */
    private Date createTime;

    /**
     * 审核状态：0领取，1放弃
     * {@link com.planet.common.Constant}
     */
    private Integer status;

    /**
     * 放弃任务时间
     */
    private Date loseTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     * 0待审核；1审核通过；2审核失败
     * {@link com.planet.common.Constant}
     */
    private Integer examineStatus;

    /**
     * 审核备注
     */
    private String examineRemark;

    /**
     * 审核时间
     */
    private Date examineTime;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(Date loseTime) {
        this.loseTime = loseTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getExamineStatus() {
        return examineStatus;
    }

    public void setExamineStatus(Integer examineStatus) {
        this.examineStatus = examineStatus;
    }

    public String getExamineRemark() {
        return examineRemark;
    }

    public void setExamineRemark(String examineRemark) {
        this.examineRemark = examineRemark;
    }

    public Date getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(Date examineTime) {
        this.examineTime = examineTime;
    }
}

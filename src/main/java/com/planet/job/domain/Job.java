package com.planet.job.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aiveily
 * @ClassName:Job
 * @Description:任务表
 * @date 2018/8/9
 */
public class Job {

    /**
     * jid
     */
    private String jid;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 副标题
     */
    private String subhead;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 地址
     */
    private String address;

    /**
     * 标签
     */
    private String tags;

    /**
     * 内容
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 状态
     * 0 - 默认
     * 1 - 待审核
     * 2 - 审核通过
     * 3 - 任务完成
     * 4 - 审核不通过
     * 5 - 订单关闭
     * {@link com.planet.common.Constant}
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否被删除
     */
    private Integer isDelete;

    private String remarks;

    /**
     * 雇主姓名
     */
    private  String employerName;

    /**
     * 雇主手机号
     */
    private String employerMobile;

    /*****不需要插入数据库，查询用到的*******/

    private Integer examineId;  //用户领取任务的id
    private Integer receiveId;  // 领取任务人的id
    private String receiveName; //领取任务的人
    private String receiveMobile; //领取任务人的手机
    private String receiveCardNo; //领取任务人的身份证
    private String receiveTime; //领取任务的时间
    private String examineRemark;   //审核备注
    private Integer examineStatus;  //领取任务被审核的状态
    private Integer userJobStatus;//用户领取任务的状态



    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        int districtIndex = address.indexOf("区");
        int cityIndex = address.indexOf("市");
        int provinceIndex = address.indexOf("省");
        if (districtIndex > 0) {
            String district = address.substring(provinceIndex+1,districtIndex+1);
            return district;
        } else if (cityIndex  > 0 ) {
            String city = address.substring(0,cityIndex+1);
            return city;
        } else if (provinceIndex  > 0 ) {
            String province = address.substring(0,provinceIndex+1);
            return province;
        }
        return "未知";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getExamineId() {
        return examineId;
    }

    public void setExamineId(Integer examineId) {
        this.examineId = examineId;
    }

    public String getExamineRemark() {
        return examineRemark;
    }

    public void setExamineRemark(String examineRemark) {
        this.examineRemark = examineRemark;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getReceiveCardNo() {
        return receiveCardNo;
    }

    public void setReceiveCardNo(String receiveCardNo) {
        this.receiveCardNo = receiveCardNo;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getUserJobStatus() {
        return userJobStatus;
    }

    public void setUserJobStatus(Integer userJobStatus) {
        this.userJobStatus = userJobStatus;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerMobile() {
        return employerMobile;
    }

    public void setEmployerMobile(String employerMobile) {
        this.employerMobile = employerMobile;
    }

    public Integer getExamineStatus() {
        return examineStatus;
    }

    public void setExamineStatus(Integer examineStatus) {
        this.examineStatus = examineStatus;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

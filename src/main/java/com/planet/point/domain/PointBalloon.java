package com.planet.point.domain;

import com.planet.common.util.DateTools;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class PointBalloon {
    private Integer id;

    private Integer type;//类型：1-用户登录摘取（所有用户都有），2-用户单独行为产生的能量球

    private Date createTime;

    private Date endTime;//截止/消失时间

    private String name;//产品、品牌名称或获取积分描述，如：注册

    private String icon;//产品图片或品牌logo

    private String oid;//订单ID

    /**
     * 获取方式
     * {@link com.planet.point.domain.PointEnum}
     */
    private Integer getWay;

    private Double point;//所含积分

    private String remark;//备注

    private Integer uid;//用户ID

    private Integer status;//状态，只针对用户行为产生的气球有用：0-未摘取，1-已摘取

    private Integer energyStatus;//产品，品牌能量状态：0,未产生  1,已产生


    private Integer brandEnergyStatus;  //默认固定球被摘取的状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public double getTime() {
        if (null != createTime && StringUtils.isNotEmpty(createTime.toString())) {
            String dateStr = DateTools.parseDateToString(createTime,"HH:mm:ss");
            String[] dateArry = dateStr.split(":");
            double h = Double.parseDouble(dateArry[0]);
            double m = Double.parseDouble(dateArry[1]);
            double resultTime = h * 60 + m;
            return resultTime;
        }
        return 0;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getGetWay() {
        return getWay;
    }

    public void setGetWay(Integer getWay) {
        this.getWay = getWay;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEnergyStatus() {
        return energyStatus;
    }

    public void setEnergyStatus(Integer energyStatus) {
        this.energyStatus = energyStatus;
    }

    public Integer getBrandEnergyStatus() {
        return brandEnergyStatus;
    }

    public void setBrandEnergyStatus(Integer brandEnergyStatus) {
        this.brandEnergyStatus = brandEnergyStatus;
    }

}
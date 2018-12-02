package com.planet.quoquotation.domain;

/**
 *
 */
public enum QuotationStatus {
    CREATE(0),  //报价单已生成

    CONFIRM(1), //报价单已确认

    FINISHED(2),    //报价单已完成

    SCRAP(-1);  //报价单已废弃

    private Integer value;
    QuotationStatus(Integer value){
        this.value = value;
    }
    public Integer getValue(){
        return value;
    }
}

package com.planet.menuopen.domain;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:29 2018/12/9
 * @Description: Modified by:
 */
public class Menuopen {
    private Integer id;
    private Integer isOpen;
    private String code;
    private Integer forsource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getForsource() {
        return forsource;
    }

    public void setForsource(Integer forsource) {
        this.forsource = forsource;
    }
}

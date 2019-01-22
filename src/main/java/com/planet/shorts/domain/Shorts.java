package com.planet.shorts.domain;

/**
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 *
 * @Author:liwufeng @Date:12:20 2018/12/9
 * @Description: Modified by:
 */
public class Shorts {
    private Integer id;
    private Integer shorttype;
    private String tel;
    private Integer source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShorttype() {
        return shorttype;
    }

    public void setShorttype(Integer shorttype) {
        this.shorttype = shorttype;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}

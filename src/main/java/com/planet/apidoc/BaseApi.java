package com.planet.apidoc;

public class BaseApi {

    /**
     * @apiError 500 操作失败
     * @apiError 400 操作失败
     * @apiError 60710 发票ID为空/未知的发票ID
     * @apiError 60711 更新发票信息失败（更新记录条数为0）
     * @apiError 60712 更新发票信息失败（功能异常）
     * @apiError 60012 获取配置信息失败
     *
     */

    /**
     * @apiSuccess (success 200) {Date} timestamp 时间戳
     * @apiSuccess (success 200) {Integer} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应实体
     */
}

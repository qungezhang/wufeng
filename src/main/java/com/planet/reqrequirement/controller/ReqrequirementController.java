package com.planet.reqrequirement.controller;


import com.planet.common.Constant;
import com.planet.reqrequirement.domain.ReqRequirement;
import com.planet.reqrequirement.service.ReqrequirementService;
import com.planet.sysfile.domain.SysFile;
import com.planet.sysfile.service.SysFileService;
import com.planet.utils.FileOperateUtil;
import com.planet.vo.ReqrequirementVo;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Li on 2016/1/21.
 */
@Controller
@RequestMapping("/req")
public class ReqrequirementController {

    private static final Logger logger = LoggerFactory.getLogger(ReqrequirementController.class);

    @Autowired
    private ReqrequirementService reqrequirementService;

    @Autowired
    private SysFileService sysFileService;


    @RequestMapping("/create1")
    @ResponseBody
    public Map<String, Object> createReq(HttpServletRequest request) throws Exception {
        //初始化状态值
        int i = 0;
        Map<String, Object> map = new HashMap<>();
        ReqRequirement reqRequirement = null;
        String json = request.getParameter("extraFiles");
        if (json == null) {
            request.getParameter("");
            reqRequirement.setPsid(request.getParameter("psid"));
            reqRequirement.setUid(Integer.valueOf(request.getParameter("uid")));
            reqRequirement.setPtype(Integer.valueOf(request.getParameter("pType")));
            reqRequirement.setProductname(request.getParameter("productName"));
            reqRequirement.setSortname(request.getParameter("sortName"));
            reqRequirement.setBrandname(request.getParameter("brandName"));
            reqRequirement.setModelname(request.getParameter("modelName"));
            reqRequirement.setPrice(new BigDecimal(request.getParameter("price")));
            reqRequirement.setQty(Integer.valueOf(request.getParameter("qty")));
            reqRequirement.setRemark(request.getParameter("remark"));
            reqRequirement.setSeriesname(request.getParameter("seriesName"));
            i = reqrequirementService.insertSelective(reqRequirement);
        } else {
            //将jsonArray转换成List
            JSONArray jsonarray = JSONArray.fromObject(json);
            List<File> files = (List<File>) JSONArray.toCollection(jsonarray);
            List<String> urllist = new ArrayList<>();
            urllist = FileOperateUtil.upload(files);
            SysFile sysFile = new SysFile();
            int fileId = sysFileService.insert(sysFile, urllist);
            reqRequirement.setPsid(request.getParameter("psid"));
            reqRequirement.setUid(Integer.valueOf(request.getParameter("uid")));
            reqRequirement.setPtype(Integer.valueOf(request.getParameter("pType")));
            reqRequirement.setProductname(request.getParameter("productName"));
            reqRequirement.setSortname(request.getParameter("sortName"));
            reqRequirement.setBrandname(request.getParameter("brandName"));
            reqRequirement.setModelname(request.getParameter("modelName"));
            reqRequirement.setPrice(new BigDecimal(request.getParameter("price")));
            reqRequirement.setQty(Integer.valueOf(request.getParameter("qty")));
            reqRequirement.setRemark(request.getParameter("remark"));
            reqRequirement.setSeriesname(request.getParameter("seriesName"));
            reqRequirement.setFileid(fileId);
            i = reqrequirementService.insertSelective(reqRequirement);
        }
        map.put("code", i);
        map.put("success", "ok");
        return map;
    }


    @RequestMapping("/index")
    public ModelAndView index(String rid) {
        //init
        Map<String, Object> model = new HashMap<>();
        ReqrequirementVo reqrequirementVo = null;
        try {
            reqrequirementVo = reqrequirementService.selectByRid(rid);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

//        List<SysFile> sysFiles = new ArrayList<>();
//        SysFile f1 = new SysFile();
//        f1.setFileurl("http://www.baidu.com/link?url=DpREbRhh1TBUnJHlK4CwXHwvS24obGrc-XWIhr8REPYgh8QekJL_J48CnwpGYWlrcyUyhFhASHRCZc74dJWt1bhzgCugpy1wMUvXZEhRlE_&wd=&eqid=d57ae93f000099830000000456eb7e52");
//        SysFile f2 = new SysFile();
//        f2.setFileurl("http://www.baidu.com/link?url=DpREbRhh1TBUnJHlK4CwXHwvS24obGrc-XWIhr8REPYgh8QekJL_J48CnwpGYWlrcyUyhFhASHRCZc74dJWt1bhzgCugpy1wMUvXZEhRlE_&wd=&eqid=d57ae93f000099830000000456eb7e52");
//        sysFiles.add(f1);
//        sysFiles.add(f2);
//        reqrequirementVo.setSysFiles(sysFiles);

        model.put("dataModel", reqrequirementVo.getReqRequirement());
        model.put("fileModel", reqrequirementVo.getSysFiles());
        model.put("fileNum", reqrequirementVo.getSysFiles().size());
        return new ModelAndView("/order/requirment2", model);
    }

    /**
     * @api {post} /req/product-req 提交产品需求
     * @apiDescription 提交产品需求，并生成订单。*请求参数以Json格式提交。
     * @apiName productReq
     * @apiGroup reqrequirement
     * @apiVersion 1.0.0
     *
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} psid *销售产品ID
     * @apiParam {Number} uid *当前登录用户ID
     * @apiParam {Number} ptype *产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiParam {String} productname *产品名称
     * @apiParam {String} sortname *品类
     * @apiParam {String} brandname *品牌
     * @apiParam {String} modelname *型号
     * @apiParam {Number} price *单价
     * @apiParam {Number} qty *数量
     * @apiParam {String} remark 描述，可以为空，最大长度200
     * @apiParam {String} seriesname *系列
     * @apiParam {Number} fileid *已上传附件的fileid
     *
     * @apiParamExample {json} Request-Example:
     *
     * {
     *     "psid": "PS180500011",
     *     "uid": 830,
     *     "ptype": 1,
     *     "productname": "西门子变频器 6SL3330-6TE35-5AA3",
     *     "sortname": "交流变频器",
     *     "brandname": "西门子",
     *     "modelname": "",
     *     "price": 2999.00,
     *     "qty": 10,
     *     "remark": "",
     *     "seriesname": "西门子G120系列变频器",
     *     "fileid": null,
     * }
     *
     * @apiSampleRequest /req/product-req
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 500-需求提交失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"需求提交失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/product-req")
    @ResponseBody
    public Map<String, Object> productReq(@RequestBody ReqRequirement requirement) {
        Map<String, Object> result = new HashMap<>();
        String msg = "ok";
        int respCode = 200;
        try {
            if ((requirement.getUid()==null) && ("".equals(requirement.getUid()))) {
                throw new RuntimeException("需求提交失败,请退出后重新提交！");

            }

            requirement.setRid(UUID.randomUUID().toString());
            requirement.setStatus(Constant.REQUIREMENT_STATUS); //需求单已生成
            int code = reqrequirementService.insert(requirement);
            if (code == 0) {
                throw new RuntimeException("需求提交失败！");
            }
        } catch (Exception ex) {
            logger.error("需求提交失败", ex);
            msg = "需求提交失败";
            respCode = 500;
        }
        result.put("code", respCode);
        result.put("message", msg);
        result.put("result", null);
        return result;
    }

    /**
     * @api {post} /req/product-req2 提交产品需求2
     * @apiDescription 提交产品需求，并生成订单。
     * @apiName productReq2
     * @apiGroup reqrequirement
     * @apiVersion 1.0.0
     *
     * @apiParam {String} requestBody *Json字符串内容，如下requestBody-Example
     *
     * @apiParamExample {json} requestBody-Example:
     *
     * {
     *     "psid": "PS180500011",
     *     "uid": 830,
     *     "ptype": 1,
     *     "productname": "西门子变频器 6SL3330-6TE35-5AA3",
     *     "sortname": "交流变频器",
     *     "brandname": "西门子",
     *     "modelname": "",
     *     "price": 2999.00,
     *     "qty": 10,
     *     "remark": "",
     *     "seriesname": "西门子G120系列变频器",
     *     "fileid": null,
     * }
     *
     * @apiSampleRequest /req/product-req2
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":null,
     *      "code":200
     * }
     *
     * @apiError (error) {Number} code 500-需求提交失败
     * @apiError (error) {String} message 响应信息
     * @apiError (error) {Object} result 响应内容，一般为null
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"需求提交失败",
     *      "result":null,
     *      "code":500
     * }
     */
    @RequestMapping("/product-req2")
    @ResponseBody
    public Map<String, Object> productReq2(@RequestParam String requestBody) {
        Map<String, Object> result = new HashMap<>();
        try {
            ReqRequirement reqRequirement = com.alibaba.fastjson.JSONObject.parseObject(requestBody, ReqRequirement.class);
            result = this.productReq(reqRequirement);
        } catch (Exception ex) {
            result.put("code", 500);
            result.put("message", "需求提交失败");
            result.put("result", null);
        }
        return result;
    }

    /**
     * @api {post} /req/req-file-upload-app 上传产品需求附件（安卓使用）
     * @apiDescription 提交产品需求，上传产品需求附件，VIP申请上传身份证，同时上传到七牛云盘
     * @apiName reqFileUploadByApp
     * @apiGroup reqrequirement
     * @apiVersion 2.0.0
     *
     * @apiParam {File} file *附件
     *
     * @apiSampleRequest /req/req-file-upload-app
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Number} result 已上传附件的fileid
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":1807000004,
     *      "code":200
     * }
     *
     * @apiError (error 500) {Number} code 500-文件上传失败
     * @apiError (error 500) {String} message 文件上传失败
     * @apiError (error 500) {Number} result 响应内容，一般为-1
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"文件上传失败",
     *      "result":-1,
     *      "code":500
     * }
     */
    @RequestMapping("/req-file-upload-app")
    @ResponseBody
    public Map<String,Object> reqFileUploadByApp(HttpServletRequest request){
        Map<String, Object> resp = new HashMap<>();
        int code = 200;
        String msg = "ok";
        int fileListId = -1;
        try{
            List<String> urlLists = FileOperateUtil.uploadByApp(request);
            fileListId = sysFileService.insert(new SysFile(), urlLists);
        }catch (Exception e){
            logger.error("上传文件失败:",e);
            code = 500;
            msg = "文件上传失败";
        }
        resp.put("code", code);
        resp.put("message",msg);
        resp.put("result", fileListId);
        return resp;
    }

    /**
     * @api {post} /req/req-file-upload 上传产品需求附件
     * @apiDescription 提交产品需求，上传产品需求附件，VIP申请上传身份证，同时上传到七牛云盘
     * @apiName reqFileUpload
     * @apiGroup reqrequirement
     * @apiVersion 1.0.0
     *
     * @apiParam {File} file *附件
     *
     * @apiSampleRequest /req/req-file-upload
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Number} result 已上传附件的fileid
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":1807000004,
     *      "code":200
     * }
     *
     * @apiError (error 500) {Number} code 500-文件上传失败
     * @apiError (error 500) {String} message 文件上传失败
     * @apiError (error 500) {Number} result 响应内容，一般为-1
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"文件上传失败",
     *      "result":-1,
     *      "code":500
     * }
     */
    @RequestMapping("/req-file-upload")
    @ResponseBody
    public Map<String,Object> reqFileUpload(HttpServletRequest request){
        Map<String, Object> resp = new HashMap<>();
        int code = 200;
        String msg = "ok";
        int fileListId = -1;
        try{
            List<String> urlLists = FileOperateUtil.upload(request);
            fileListId = sysFileService.insert(new SysFile(), urlLists);
        }catch (Exception e){
            logger.error("上传文件失败:",e);
            code = 500;
            msg = "文件上传失败";
        }
        resp.put("code", code);
        resp.put("message",msg);
        resp.put("result", fileListId);
        return resp;
    }

    @RequestMapping("/getRequirement")
    @ResponseBody
    public Map<String, Object> getRequirement(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        List result = new ArrayList();
        String rid = null;
        ReqRequirement reqRequirement = new ReqRequirement();
        rid = request.getParameter("rid");
        try {
            reqRequirement = reqrequirementService.selectByPrimaryKey(rid);
            if (reqRequirement != null) {

                map.put("code", 200);
                map.put("message", "ok");
                map.put("result", reqRequirement);
            } else {
                map.put("code", 400);
                map.put("message", "获取错误！");
                map.put("result", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 400);
            map.put("message", "获取错误！");
            map.put("result", result);
            return map;
        }

        return map;
    }


}

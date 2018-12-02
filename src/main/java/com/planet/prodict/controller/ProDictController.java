package com.planet.prodict.controller;

import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.prodict.domain.ProDict;
import com.planet.prodict.service.ProDictService;
import com.planet.vo.ProDictVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Li on 2016/1/25.
 */
@Controller
@RequestMapping("/dict")
public class ProDictController {

    private static final Logger logger = LoggerFactory.getLogger(ProDictController.class);

    @Autowired
    private ProDictService proDictService;

    /**
     * @api {get} /dict/dictlist 分页获取产品品类/品牌/系列
     * @apiDescription 分页获取产品品类、品牌、系列
     * @apiName DictList
     * @apiGroup dict
     * @apiVersion 1.0.0
     *
     * @apiParam {String} type *类型：1-品类，2-品牌，3-系列
     * @apiParam {String} pre 名称首字母
     * @apiParam {Number} page *页码
     * @apiParam {Number} rows *获取的数量
     * @apiParam {Number} parentId 上级ID
     *
     * @apiSampleRequest /dict/dictlist
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容
     * @apiSuccess (success 200) {Number} result.did ID
     * @apiSuccess (success 200) {Number} result.type 类型：1-品类，2-品牌，3-系列
     * @apiSuccess (success 200) {String} result.dictname 名称
     * @apiSuccess (success 200) {String} result.pre 名称首字母
     * @apiSuccess (success 200) {Number} result.parentid 上级ID
     * @apiSuccess (success 200) {String} result.imgurl 图片
     * @apiSuccess (success 200) {Number} result.sn 排序，越小越靠前
     * @apiSuccess (success 200) {String} result.bindinfo 如果type=1，bindinfo=$dictname；
     * type=2，bindinfo=$did-$dictname；
     * type=3，bindinfo=$did-$brandname-$dictname
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":[
     *          {
     *              "did":486,
     *              "type":1,
     *              "dictname":"交流接触器",
     *              "pre":"J",
     *              "parentid":0,
     *              "imgurl":"c6667591-78fc-4a59-b31c-5d227a9a36c2.jpg",
     *              "sn":11,
     *              "bindinfo":"交流接触器"
     *          }
     *      ],
     *      "code":200
     * }
     */
    @RequestMapping("/dictlist")
    @ResponseBody
    public Map<String, Object> DictList(
            @RequestParam(value = "type", required = true) final String type,
            @RequestParam(value = "pre", required = false) final String pre,
            @RequestParam(value = "page", required = true) final Integer page,
            @RequestParam(value = "rows", required = true) final Integer rows,
            @RequestParam(value = "parentId", required = false) final Integer parentId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> model = new HashMap<>();

        Pagination pagination = new Pagination(rows, page);
        param.put("pagination", pagination);
        param.put("type", Integer.valueOf(type));
        param.put("pre", pre);
        param.put("parentId", parentId);
        List<ProDictVo> proDicts = proDictService.listPageProDict(param);
        logger.info(proDicts.toString());
        model.put("code", 200);
        model.put("message", "ok");
        model.put("result", proDicts);
        return model;
    }

    /**
     * @api {get} /dict/dictlistByPre 根据就首字母区间获取品牌品牌
     * @apiDescription 分页获取产品品类、品牌、系列
     * @apiName dictlistByPre
     * @apiGroup dict
     * @apiVersion 1.0.0
     *
     * @apiParam {String} pre 名称首字母
     * @apiParam {Number} page *页码
     * @apiParam {Number} rows *获取的数量
     * @apiParam {Number} parentId 上级ID
     *
     * @apiSampleRequest /dict/dictlistByPre
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} message 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容
     * @apiSuccess (success 200) {Number} result.did ID
     * @apiSuccess (success 200) {String} result.dictname 名称
     * @apiSuccess (success 200) {String} result.pre 名称首字母，区间，以-拼接。如："A-G"
     * @apiSuccess (success 200) {Number} result.parentid 上级ID
     * @apiSuccess (success 200) {String} result.imgurl 图片
     * @apiSuccess (success 200) {Number} result.sn 排序，越小越靠前
     * @apiSuccess (success 200) {String} result.bindinfo 如果type=1，bindinfo=$dictname；
     * type=2，bindinfo=$did-$dictname；
     * type=3，bindinfo=$did-$brandname-$dictname
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "message":"ok",
     *      "result":[
     *          {
     *              "did":486,
     *              "type":1,
     *              "dictname":"交流接触器",
     *              "pre":"J",
     *              "parentid":0,
     *              "imgurl":"c6667591-78fc-4a59-b31c-5d227a9a36c2.jpg",
     *              "sn":11,
     *              "bindinfo":"交流接触器"
     *          }
     *      ],
     *      "code":200
     * }
     */
    @RequestMapping("/dictlistByPre")
    @ResponseBody
    public Map<String, Object> DictListByPre(
            @RequestParam(value = "pre", required = false) final String pre,
            @RequestParam(value = "page", required = true) final Integer page,
            @RequestParam(value = "rows", required = true) final Integer rows,
            @RequestParam(value = "parentId", required = false) final Integer parentId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        String pre1 = null;
        String pre2 = null;
        if (null != pre) {
            String[] preArr = pre.split("-");
            pre1 = preArr[0];
            pre2 = preArr[1];
        }
        Pagination pagination = new Pagination(rows, page);
        param.put("pagination", pagination);
        param.put("type", 2);
        param.put("pre1", pre1);
        param.put("pre2", pre2);
        param.put("parentId", parentId);
        List<ProDictVo> proDicts = proDictService.listPageProDictByPre(param);
        logger.info(proDicts.toString());
        model.put("code", 200);
        model.put("message", "ok");
        model.put("result", proDicts);
        return model;
    }

    @RequestMapping("/toProdict")
    public ModelAndView index(HttpServletRequest request) {
        //init
        Map<String, Object> model = new HashMap<String, Object>();
        String type = request.getParameter("type");
        model.put("type", type);
        return new ModelAndView("/dictlist/prodict", model);
    }

    /**
     * get dic List
     *
     * @return
     */
    @RequestMapping("/proDictList")
    @ResponseBody
    public Map getUserList(HttpServletRequest request, int page, int rows) {
        //init
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        String type = null;
        String dicname = null;
        List<ProDictVo> proDicts = null;
        Pagination pagination = null;

        try {
            pagination = new Pagination(rows, page);
            type = request.getParameter("type");
            dicname = request.getParameter("dicname");
            if (type == null) {
                throw new RuntimeException("类型错误");
            }
            map.put("pagination", pagination);
            map.put("type", type);
            map.put("dictname", dicname);
            proDicts = proDictService.listPageProDict(map);

        } catch (Exception e) {
            logger.error(e.getMessage());
            proDicts = null;
        }
        model.put("rows", proDicts);
        model.put("total", pagination.getCount());
        return model;
    }

    @RequestMapping("/editDic")
    @ResponseBody
    public Map editDic(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> model = new HashMap<String, Object>();
        int success = 0;
        String msg = "修改失败";
        String dicname = null;
        String pre = null;
        String sn = null;
        String did = null;

        ProDict proDict = new ProDict();
        try {
            did = request.getParameter("did");
            pre = request.getParameter("pre");
            sn = request.getParameter("sn");
            dicname = request.getParameter("dicname");
            proDict.setDid(Integer.parseInt(did));
            proDict.setPre(pre);
            proDict.setSn(Integer.parseInt(sn));
            proDict.setDictname(dicname);
            success = proDictService.updateByPrimaryKeySelective(proDict, request);
            if (success == 1) {
                msg = "修改成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
            msg = "修改失败";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


    @RequestMapping("/deleteDic")
    @ResponseBody
    public Map deleteDic(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        int success = 0;
        String msg = "删除失败";
        String did = null;
        try {
            did = request.getParameter("did");
            success = proDictService.deleteByPrimaryKey(Integer.parseInt(did));
            if (success == 1) {
                msg = "删除成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
            msg = "删除失败";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


    @RequestMapping("/addDic")
    @ResponseBody
    public Map addDic(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        String msg = "添加失败";
        int success = 0;
        String dicname = null;
        String pre = null;
        String sn = null;
        String type = null;
        String parentid = null;
        ProDict proDict = new ProDict();
        try {
            pre = request.getParameter("pre");
            sn = request.getParameter("sn");
            dicname = request.getParameter("dicname");
            type = request.getParameter("type");

            if ("1".equals(type)) {
                parentid = "0";
            } else {
                parentid = request.getParameter("parentId");
            }

            proDict.setPre(pre.toUpperCase());
            proDict.setSn(Integer.parseInt(sn));
            proDict.setDictname(dicname);
            proDict.setType(Integer.parseInt(type));
            proDict.setParentid(Integer.parseInt(parentid));
            success = proDictService.insertSelective(proDict, request);
            if (success == 1) {
                msg = "添加成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            success = 0;
            msg = "添加失败";
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

    /**
     * @api {get} /dict/pre 根据类型获取列表
     * @apiDescription 根据类型获取列表
     * @apiName PreInfo
     * @apiGroup dict
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} type *类型：1-品类，2-品牌，3-系列
     *
     * @apiSampleRequest /dict/pre
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} success 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容
     * @apiSuccess (success 200) {Number} result.did ID
     * @apiSuccess (success 200) {Number} result.type 类型：1-品类，2-品牌，3-系列
     * @apiSuccess (success 200) {String} result.dictname 名称
     * @apiSuccess (success 200) {String} result.pre 名称首字母
     * @apiSuccess (success 200) {Number} result.parentid 上级ID
     * @apiSuccess (success 200) {String} result.imgurl 图片
     * @apiSuccess (success 200) {Number} result.sn 排序，越小越靠前
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "success":"ok",
     *      "result":[
     *          {
     *              "did":102,
     *              "type":1,
     *              "dictname":"变压器&电抗器",
     *              "pre":"B",
     *              "parentid":0,
     *              "imgurl":"17d55d04-9a41-4e67-b173-a36d805c05a4变压器.png",
     *              "sn":7
     *          },
     *          {
     *              "did":71,
     *              "type":1,
     *              "dictname":"触摸屏",
     *              "pre":"C",
     *              "parentid":0,
     *              "imgurl":"64abe74b-959a-490d-8562-4f3272aab11a设备_触屏开关.png",
     *              "sn":9
     *          }
     *      ],
     *      "code":200
     * }
     */
    @RequestMapping("/pre")
    @ResponseBody
    public Map<String, Object> PreInfo(int type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        List<ProDict> proDicts = new ArrayList<>();
        map.put("type", type);
        proDicts = proDictService.selectPreInfo(map);
        model.put("code", 200);
        model.put("success", "ok");
        model.put("result", proDicts);
        return model;
    }


    /**
     * put data for combobox
     *
     * @return
     */
    @RequestMapping("/listDidAndDicName")
    @ResponseBody
    public List getDidAndDictName(Integer type, @RequestParam(required = false) Integer parentId) {

        Map<String, Object> map = new HashMap<>();

        List<ProDict> data = null;
        try {
            map.put("type", type);
            if (parentId != null)
                map.put("parentid", parentId);
            data = proDictService.selectProall(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}

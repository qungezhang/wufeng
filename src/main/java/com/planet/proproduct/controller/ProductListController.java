package com.planet.proproduct.controller;

import com.planet.common.mybatis.plugins.page.Pagination;
import com.planet.proproduct.domain.ProProduct;
import com.planet.proproduct.service.ProProductService;
import com.planet.vo.ProductAllVo;
import com.planet.vo.ProductDetailVo;
import com.planet.vo.ProductListBgVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品
 * Created by Li on 2016/1/24.
 */
@Controller
@RequestMapping("/product")
public class ProductListController {

    private Logger logger = LoggerFactory.getLogger(ProductListController.class);

    @Autowired
    private ProProductService proProductService;

    /**
     * @api {get} /product/list 获取产品列表
     * @apiDescription 分页获取产品列表
     * @apiName ProductList
     * @apiGroup product
     * @apiVersion 1.0.0
     * @apiParam {Number} type *类型：1-特价，2-推荐，3-普通
     * @apiParam {Number} page *页码
     * @apiParam {Number} rows *获取的数量
     * @apiParam {Number} keywords 关键词，模糊匹配项目名称
     *
     * @apiSampleRequest /product/list
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} success 响应信息
     * @apiSuccess (success 200) {Object} result 响应内容
     * @apiSuccess (success 200) {Number} result.count 产品总数
     * @apiSuccess (success 200) {Array} result.projectList 产品列表
     * @apiSuccess (success 200) {String} result.projectList.psid 销售产品ID
     * @apiSuccess (success 200) {String} result.projectList.pid 产品ID
     * @apiSuccess (success 200) {Number} result.projectList.stock 库存
     * @apiSuccess (success 200) {Number} result.projectList.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     * @apiSuccess (success 200) {Number} result.projectList.price 产品价格
     * @apiSuccess (success 200) {String} result.projectList.remark 销售备注
     * @apiSuccess (success 200) {Number} result.projectList.saledate 上架时间
     * @apiSuccess (success 200) {Number} result.projectList.status 状态：-1-删除，1-正常
     * @apiSuccess (success 200) {String} result.projectList.stay1 保留字段1
     * @apiSuccess (success 200) {String} result.projectList.stay2 保留字段2
     * @apiSuccess (success 200) {Number} result.projectList.sn 排序
     * @apiSuccess (success 200) {String} result.projectList.productname 产品名称
     * @apiSuccess (success 200) {Number} result.projectList.sortid 品类ID
     * @apiSuccess (success 200) {Number} result.projectList.brandid 品牌ID
     * @apiSuccess (success 200) {Number} result.projectList.seriesid 系列ID
     * @apiSuccess (success 200) {String} result.projectList.imgurl 图片
     * @apiSuccess (success 200) {String} result.projectList.imgurl2 推荐图片
     * @apiSuccess (success 200) {String} result.projectList.modelname 型号名称
     * @apiSuccess (success 200) {String} result.projectList.describemodel 大段描述内容
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "success":"ok",
     *      "result":[
     *          {
     *              "psid":"PS180500011",
     *              "pid":"P180500010",
     *              "stock":1,
     *              "ptype":1,
     *              "price":null,
     *              "remark":null,
     *              "saledate":1526400000000,
     *              "status":null,
     *              "stay1":null,
     *              "stay2":null,
     *              "sn":0,
     *              "productname":"富士变频器 FRN3. 7F1S-4C 3.7KW",
     *              "sortid":null,
     *              "brandid":null,
     *              "seriesid":null,
     *              "imgurl":"55d8454d-3696-4115-8236-02f73e359308.png",
     *              "imgurl2":null,
     *              "modelname":null,
     *              "describemodel":"大段描述内容..."
     *          },
     *          {
     *              "psid":"PS170700004",
     *              "pid":"P170700004",
     *              "stock":2,
     *              "ptype":1,
     *              "price":3800.00,
     *              "remark":null,
     *              "saledate":1500393600000,
     *              "status":null,
     *              "stay1":null,
     *              "stay2":null,
     *              "sn":0,
     *              "productname":"派克变频器AC10 10G-45-0320-BF 15KW",
     *              "sortid":null,
     *              "brandid":null,
     *              "seriesid":null,
     *              "imgurl":"5121de9e-1469-42af-b919-7580f2ee6e64.png",
     *              "imgurl2":null,
     *              "modelname":null,
     *              "describemodel":"大段描述内容..."
     *          }
     *      ],
     *      "code":200
     * }
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> ProductList(int page, int rows, int type, @RequestParam(value = "keywords", required = false) String keywords) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        List<ProductAllVo> productAllVos = new ArrayList<>();
        Pagination pagination = new Pagination(rows, page);
        map.put("pagination", pagination);
        map.put("ptype", type);
        map.put("productname", keywords);
        productAllVos = proProductService.listPageselectProductSaleList(map);
        Map<String,Object> resultmap = new HashMap<>();
        resultmap.put("productList",productAllVos);
        resultmap.put("count",pagination.getCount());
        model.put("code", 200);
        model.put("success", "ok");
        model.put("result", resultmap);
        return model;
    }

    /**
     * @api {get} /product/detail 获取产品信息
     * @apiDescription 根据销售产品ID获取产品信息，销售产品ID（psid）有别于产品ID（pid）
     * @apiName ProductDetail
     * @apiGroup product
     * @apiVersion 1.0.0
     *
     * @apiParam {String} psid *销售产品ID，非产品ID
     *
     * @apiSampleRequest /product/detail
     *
     * @apiSuccess (success 200) {Number} code 响应码
     * @apiSuccess (success 200) {String} success 响应信息
     * @apiSuccess (success 200) {Array} result 响应内容
     * @apiSuccess (success 200) {String} result.psid 销售产品ID
     * @apiSuccess (success 200) {Number} result.brandid 品牌ID
     * @apiSuccess (success 200) {String} result.modelname 型号名称
     * @apiSuccess (success 200) {String} result.brandname 品牌名称
     * @apiSuccess (success 200) {String} result.imgurl 图片
     * @apiSuccess (success 200) {String} result.imgurl2 推荐图片
     * @apiSuccess (success 200) {String} result.describemodel 大段描述内容
     * @apiSuccess (success 200) {String} result.pid 产品ID
     * @apiSuccess (success 200) {Number} result.seriesid 系列ID
     * @apiSuccess (success 200) {String} result.seriesname 系列名称
     * @apiSuccess (success 200) {Number} result.sortid 品类ID
     * @apiSuccess (success 200) {String} result.sortname 品类名称
     * @apiSuccess (success 200) {Number} result.stock 库存
     * @apiSuccess (success 200) {String} result.productname 产品名称
     * @apiSuccess (success 200) {Number} result.price 产品价格
     * @apiSuccess (success 200) {Number} result.saledate 上架时间
     * @apiSuccess (success 200) {Number} result.ptype 产品类型：1-特价产品，2-推荐产品，3-普通产品
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "success":"ok",
     *      "result":[
     *          {
     *              "psid":"PS160300018",
     *              "brandid":81,
     *              "modelname":null,
     *              "brandname":"富士",
     *              "imgurl":"5db06ee2-cd6a-4925-99e7-282bf6bcb2eb09富士.png",
     *              "imgurl2":null,
     *              "describemodel":"大段描述内容...",
     *              "pid":"P160300015",
     *              "seriesid":94,
     *              "seriesname":"富士其它系列变频器",
     *              "sortid":67,
     *              "sortname":"交流变频器",
     *              "stock":1,
     *              "productname":"富士变频器 FRN1.5EIS-7C  无说明书无包装",
     *              "price":600.00,
     *              "saledate":1458748800000,
     *              "ptype":1
     *          }
     *      ],
     *      "code":200
     * }
     */
    @RequestMapping("/detail")
    @ResponseBody
    public Map<String, Object> ProductDetail(String psid) throws Exception {
        Map<String, Object> map = new HashMap();
        Map model = new HashMap<>();
        List<ProductDetailVo> detailVos = new ArrayList();
        map.put("psid", psid);
        detailVos = proProductService.selectProductDetail(map);
        model.put("code", 200);
        model.put("success", "ok");
        model.put("result", detailVos);
        return model;
    }

    @RequestMapping("/dict")
    public Map<String, Object> ProductDict(int type, int tag, String pre) {
        Map map = new HashMap();
        List list = new ArrayList();

        return map;
    }


    /**
     * list request of customer service system
     *
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listProduct")
    @ResponseBody
    public Map listProduct(HttpServletRequest request, int page, int rows) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        List<ProductListBgVo> productListBgVos = null;
        Pagination pagination = null;

        String pid = null;
        String productName = null;
        String brandid = null;
        String seriesid = null;
        String sortid = null;
        String guigeid = null;
        try {

            pid = request.getParameter("pid");
            productName = request.getParameter("productName");
            sortid = request.getParameter("sortId");
            seriesid = request.getParameter("seriesId");
            brandid = request.getParameter("brandId");
            guigeid = request.getParameter("guigeId");

            if (!"".equals(pid))
                map.put("pid", pid);
            if (!"".equals(productName))
                map.put("productname", productName);
            if (!"".equals(sortid))
                map.put("sortid", sortid);
            if (!"".equals(seriesid))
                map.put("seriesid", seriesid);
            if (!"".equals(brandid))
                map.put("brandid", brandid);
            if(!"".equals(guigeid)){
                map.put("guigeid",guigeid);
            }


            pagination = new Pagination(rows, page);
            map.put("pagination", pagination);
            productListBgVos = proProductService.listPageSelectProProduct(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        model.put("rows", productListBgVos);
        model.put("total", pagination.getCount());
        return model;
    }


    /**
     * enter into product list page of customer service system
     *
     * @return
     */
    @RequestMapping("/toProduct")
    public ModelAndView toProductList() {
        return new ModelAndView("/product/product");
    }

    /**
     * add prodect request of customer service system
     * -@RequestParam("file") MultipartFile[] fileList,
     *
     * @return
     */
    @RequestMapping("/addProduct")
    @ResponseBody
    public Map addProduct(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        String msg = "添加失败";
        int success = 0;
        BigDecimal price = null;
        String productName = null;
        Integer sortId = null;
        Integer seriesId = null;
        Integer brandId = null;
        String describemodel = null;
        Integer guigeId = null;

        try {

            productName = request.getParameter("productName");
            describemodel = request.getParameter("describemodel");
            sortId = Integer.parseInt(request.getParameter("sortId"));
            String priceTemp = request.getParameter("price");
            price =(null!= priceTemp &&!"".equals(priceTemp))? new BigDecimal(priceTemp) : new BigDecimal(0);
            seriesId = Integer.parseInt(request.getParameter("seriesId"));
            brandId = Integer.parseInt(request.getParameter("brandId"));
            guigeId = Integer.parseInt(request.getParameter("guigeId"));


            ProProduct proProduct = new ProProduct();
            proProduct.setProductname(productName);
            proProduct.setDescribemodel(describemodel);
            proProduct.setPrice(price);
            proProduct.setBrandid(brandId);
            proProduct.setSortid(sortId);
            proProduct.setSeriesid(seriesId);
            proProduct.setGuigeid(guigeId);
            proProduct.setPid(String.valueOf(System.currentTimeMillis()));

            success = proProductService.insertSelective(proProduct, request);

            if (success == 1) {
                msg = "添加成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            msg = "添加失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


    /**
     * edit prodect request of customer service system
     *
     * @return
     */
    @RequestMapping("/editProduct")
    @ResponseBody
    public Map editProduct(HttpServletRequest request) {
        //init
        Map<String, Object> model = new HashMap<>();
        String msg = "修改失败";
        int success = 0;
        String pid = null;
        BigDecimal price = null;
        String productName = null;
        Integer sortId = null;
        Integer seriesId = null;
        Integer brandId = null;
        String describemodel = null;
        Integer guigeId = null;

        try {

            pid = request.getParameter("pid");
            productName = request.getParameter("productName");
            sortId = Integer.parseInt(request.getParameter("sortId"));
            price =new BigDecimal(request.getParameter("price"));
            seriesId = Integer.parseInt(request.getParameter("seriesId"));
            brandId = Integer.parseInt(request.getParameter("brandId"));
            describemodel = request.getParameter("describemodel");
            guigeId = Integer.parseInt(request.getParameter("guigeId"));

            ProProduct proProduct = new ProProduct();

            proProduct.setPid(pid);
            proProduct.setProductname(productName);
            proProduct.setPrice(price);
            proProduct.setBrandid(brandId);
            proProduct.setSortid(sortId);
            proProduct.setSeriesid(seriesId);
            proProduct.setGuigeid(guigeId);
            proProduct.setDescribemodel(describemodel);
            success = proProductService.updateByPrimaryKeySelective(proProduct, request);
            if (success == 1) {
                msg = "修改成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "修改失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }


    /**
     * remove prodect request of customer service system
     *
     * @return
     */
    @RequestMapping("/removeProduct")
    @ResponseBody
    public Map removeProduct(HttpServletRequest request) {
        //init
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        String msg = "删除失败";
        int success = 0;
        String pid = null;
        try {
            pid = request.getParameter("pid");
            success = proProductService.deleteByPrimaryKey(pid);
            if (success == 1) {
                msg = "删除成功";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = "删除失败";
            success = 0;
        }
        model.put("msg", msg);
        model.put("success", success);
        return model;
    }

}

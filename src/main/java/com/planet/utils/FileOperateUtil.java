package com.planet.utils;


import com.planet.utils.OSS.QiNiuFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * Created by Li on 2016/1/15.
 */

public class FileOperateUtil {
    public static String FILEDIR = "/opt/file/";
    private static final Logger logger = LoggerFactory.getLogger(FileOperateUtil.class);

    /**
     * 安卓上传附件
     * 由于安卓使用Map传值方式，与h5表单不同，因此额外创建方法读取值
     *
     * @param request
     * @throws IOException
     */
    public static List<String> uploadByApp(HttpServletRequest request) {
        MultipartHttpServletRequest mRequest = null;
        MultiValueMap<String, MultipartFile> fileMap = null;
        List urllists = new ArrayList();
        try {
            mRequest = (MultipartHttpServletRequest) request;
            if ("".equals(mRequest) && mRequest == null) {
                return urllists;
            } else {
                fileMap = mRequest.getMultiFileMap();
                Map<String, MultipartFile> map = new HashMap<>();
                map.put("files[0]",fileMap.get("file").get(0));
                map.put("files[1]",fileMap.get("file").get(1));
                urllists = QiNiuFileSystem.QiNiuuploadFile(map);
            }
        } catch (Exception e) {
            logger.info(String.valueOf(e));
            return urllists;
        }
        return urllists;
    }

    /**
     * 上传
     *
     * @param request
     * @throws IOException
     */
    public static List<String> upload(HttpServletRequest request) {
        MultipartHttpServletRequest mRequest = null;
        Map<String, MultipartFile> fileMap = null;
        List urllists = new ArrayList();
        try {
            mRequest = (MultipartHttpServletRequest) request;
            if ("".equals(mRequest) && mRequest == null) {
                return urllists;
            } else {
                fileMap = mRequest.getFileMap();
                urllists = QiNiuFileSystem.QiNiuuploadFile(fileMap);
            }
        } catch (Exception e) {
            logger.info(String.valueOf(e));
            return urllists;
        }
        return urllists;
    }

    /**
     * File上传（重载方法）
     *
     * @param list
     * @return
     */
    public static List<String> upload(List list) {
        List urllists = new ArrayList();
        try {
            urllists = QiNiuFileSystem.QiNiuuploadFile(list);
        } catch (Exception e) {
            logger.info(String.valueOf(e));
            return urllists;
        }
        return urllists;
    }







}

package com.planet.user.service;

import com.planet.common.Constant;
import com.planet.common.util.DateTools;
import com.planet.message.service.MessageBatchService;
import com.planet.point.domain.PointEnum;
import com.planet.point.domain.PointBalloon;
import com.planet.point.domain.PointLog;
import com.planet.point.service.PointBalloonService;
import com.planet.user.dao.UserAgentMapper;
import com.planet.user.domain.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Li on 2016/1/19.
 */
@Service
@Transactional
public class UserServiceimpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);

    @Autowired
    private UserAgentMapper userAgentMapper;

    @Autowired
    private MessageBatchService messageBatchService;

    @Autowired
    private PointBalloonService pointBalloonService;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer uid) {
        int i = 0;
        i = userAgentMapper.deleteByPrimaryKey(uid);
        if (i==0){
            logger.info("删除用户失败");
        }else if (i==1){
            logger.info("删除用户成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insert(UserAgent userAgent) throws Exception {
        int i = 0;
        boolean flag = true;
        String referralcode = null;
        //通过下面的方法生成唯一的推荐码
        while(flag){
            UserAgent userAgent1  = null;
            referralcode =  generateWord();//生成4位随机数
            userAgent1 =  userAgentMapper.selectByReferralCode(referralcode);
            if(userAgent1==null){
                logger.info("生成推荐码"+referralcode);
                break;

            }
        }
        userAgent.setReferralcode(referralcode);
        i = userAgentMapper.insert(userAgent);
        if (i == 0) {
            logger.info("插入失败");
        } else if (i == 1) {
            logger.info("插入成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertSelective(UserAgent userAgent) throws Exception {
        int i = 0;
        boolean flag = true;
        String referralcode = null;
        //通过下面的方法生成唯一的推荐码
        while(flag){
            UserAgent userAgent1  = null;
            referralcode =  generateWord().toUpperCase();//生成4位随机数
            userAgent1 =  userAgentMapper.selectByReferralCode(referralcode);
            if(userAgent1==null){
                logger.info("推荐码"+referralcode);
                break;
            }
        }
        userAgent.setReferralcode(referralcode);
        i = userAgentMapper.insertSelective(userAgent);
        if (i == 0) {
            logger.info("插入失败");
        } else if (i == 1) {
            logger.info("插入成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserAgent selectByPrimaryKey(Integer uid) throws Exception {
        return   userAgentMapper.selectByPrimaryKey(uid);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(UserAgent userAgent) throws Exception {
        int i = 0;
        i = userAgentMapper.updateByPrimaryKeySelective(userAgent);
        if (i == 0) {
            logger.info("更新失败");
        } else if (i == 1) {
            logger.info("更新成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateByPrimaryKey(UserAgent userAgent) throws Exception {
        int i = 0;
        i = userAgentMapper.updateByPrimaryKey(userAgent);
        if (i == 0) {
            logger.info("更新失败");
        } else if (i == 1) {
            logger.info("更新成功");
        }
        return i;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<UserAgent> listPageselectUserAgent(Map map) {
        List<UserAgent> userAgents=null;
        userAgents=userAgentMapper.listPageselectUserAgent(map);
        return userAgents;
    }

    /**
     * 根据手机号查询用户信息
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserAgent selectByUserName(String username) throws Exception {
        return userAgentMapper.selectByUserName(username);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updatePoint(Map<String, Object> map) throws Exception {
        int i = 0;

        Integer uid = Integer.valueOf(map.get("uid").toString());

        String type = map.get("level").toString();

        // VIP通过送积分
        if ("vip".equals(type)) {
            this.createPointBalloon(uid,"用户认证", PointEnum.anth_point.getType(),PointEnum.anth_point.getPoint(),null);
        }

        //注册送积分
        if ("register".equals(type)){
            this.createPointBalloon(uid,"用户注册",PointEnum.register_point.getType(),PointEnum.register_point.getPoint(),null);

            // 推荐人增加积分
            String  referralcode = map.get("referralcode").toString();
            if(referralcode!=null&&!"".equals(referralcode)){
                UserAgent userAgent1  = null;
                userAgent1 =  userAgentMapper.selectByReferralCode(referralcode);
                if (userAgent1!=null) {
                    this.createPointBalloon(userAgent1.getUid(),"邀请码被注册",PointEnum.code_register_point.getType(),PointEnum.code_register_point.getPoint(),null);
                }
            }
        }

        if ("order".equals(type)) {
            Double trade_point = 0.0;
            String amount = map.get("orderAmount").toString();
            if(null != amount){
                trade_point = Double.parseDouble(amount) * PointEnum.trade_point.getPoint();
            }
            String oid = map.get("oid").toString();
            this.createPointBalloon(uid, "订单交易", PointEnum.trade_point.getType(), trade_point,oid);
        }

        //转发送积分
        if ("userShare".equals(type)) {
            Map<String,Object> shareMap = new HashMap<>();
            shareMap.put("uid",uid);
            shareMap.put("getWay",PointEnum.share_point.getType());
            int num = pointBalloonService.findByCurr(shareMap);
            if (num < 5) {
                this.createPointBalloon(uid, "用户分享", PointEnum.share_point.getType(), PointEnum.share_point.getPoint(),null);
            }
        }

        return i;
    }

    /**
     * @Description 创建能量积分球
     * @param uid 用户id
     * @param name 名称
     * @param type 产生的方式
     * @param point 产生的积分
     * @param oid 订单id
     * @author aiveily
     * @create 2018/8/14 15:09
     */
    public void createPointBalloon(int uid,String name,Integer type,Double point,String oid) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("getWay", Constant.POINT_BALLOON_BRAND);
        PointBalloon pb = pointBalloonService.randGetUrlByPointBalloon(map);
        PointBalloon pointBalloon = new PointBalloon();
        pointBalloon.setType(2);
//        pointBalloon.setCreateTime(new Date());
        pointBalloon.setEndTime(new Date());
        pointBalloon.setName(name);
        pointBalloon.setGetWay(type);
        pointBalloon.setPoint(point);
        pointBalloon.setRemark(name +"产生" + point + "积分");
        pointBalloon.setUid(uid);
        pointBalloon.setStatus(0);
        if (null != pb) {
            pointBalloon.setIcon(pb.getIcon());
        }
        pointBalloon.setEnergyStatus(1);
        if(null != oid){
            pointBalloon.setOid(oid);
        }
        int i = pointBalloonService.insertByPrimaryKeySelective(pointBalloon);
        System.out.println(i);
    }


    @Override
    public List<Map> selectAllUserAgentToVo(Map<String, Object> map) throws Exception {
        List<Map> userAgentVoList = new ArrayList<>();
        List<UserAgent> userAgent = userAgentMapper.selectAllUser(map);
        if(null!=userAgent&&userAgent.size()>0){
            for(UserAgent ua :userAgent){
                Map vo=new HashMap();
                vo.put("username",ua.getUsername());
                vo.put("name", ua.getName());
                vo.put("vip",ua.getVip() == 1 ? "是" : "否");
                vo.put("point",ua.getPoint());
                vo.put("tel",ua.getTel());
                vo.put("status",ua.getStatus()==1?"正常":"停用");
                vo.put("remark",ua.getRemark());
                vo.put("logindate",null!= ua.getLogindate() ? DateTools.parseDateToString(ua.getLogindate(), "yyyy-MM-dd hh:mm:ss EE") :"");
                userAgentVoList.add(vo);
            }
        }
        return userAgentVoList;
    }

    @Override
    public UserAgent selectUserAgentByCode(String referralcode) throws Exception {
        return userAgentMapper.selectByReferralCode(referralcode);
    }

    @Override
    public List<UserAgent> listPageUserAgentAndReferralUname(Map map) throws Exception {
        return userAgentMapper.listPageUserAgentAndReferralUname(map);
    }


    //生成4位随机数
    private String generateWord() {
        String[] beforeShuffle = new String[] { "2", "3", "4", "5", "6", "7",
                "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(4, 8);
        logger.info("推荐码"+result);
        return result;
    }

    public List<UserAgent> getPointRanking() throws Exception {
        return userAgentMapper.getPointRanking();
    }

    public Integer findUserRank(Integer uid) {
        return userAgentMapper.findUserRank(uid);
    }

    @Override
    public UserAgent selectByOpenid(String openid) {
        return userAgentMapper.selectByOpenid(openid);
    }
}

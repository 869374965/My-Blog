package com.my.blog.website.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.blog.website.service.ITbkService;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TbkServiceImpl implements ITbkService {
    private final String appkey="24711622";
    private final String secret="351c55c6b58f79360948333e84ad7b1f";
    private final String url="http://gw.api.taobao.com/router/rest";
    private final Long adzoneId=158958578L;
    private Map<String, Object> resultMap;
    Gson gson =new Gson();


    @Override
    public Map<String, Object> getProductList(TbkItemGetRequest req) throws Exception {
        resultMap=new HashMap<String,Object>();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
//		req.setCat(cat);
        //TODO: 添加查询条件
        TbkItemGetResponse rsp = client.execute(req);
        resultMap.put("data", rsp.getBody());
        return resultMap;
    }
    @Override
    public List<Map<String, Object>> getCouponProductList(TbkDgItemCouponGetRequest req) throws Exception {
        resultMap=new HashMap<String,Object>();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        req.setAdzoneId(adzoneId);
        TbkDgItemCouponGetResponse rsp = client.execute(req);
        resultMap.put("data", rsp.getBody());
        Map<String,Object> dataMap = gson.fromJson(rsp.getBody(), new TypeToken<Map<String,Object>>(){}.getType());
        dataMap = (Map<String, Object>) dataMap.get("tbk_dg_item_coupon_get_response");
        dataMap = (Map<String, Object>) dataMap.get("results");
        List<Map<String,Object>> list = (List<Map<String, Object>>) dataMap.get("tbk_coupon");
        return list;
    }
    @Override
    public Map<String, Object> getShopping(TbkShopGetRequest req) throws Exception {
        resultMap = new HashMap<String,Object>();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        req.setQ("女装");
        req.setSort("commission_rate_des");
		/*req.setIsTmall(false);
		req.setStartCredit(1L);
		req.setEndCredit(20L);
		req.setStartCommissionRate(2000L);
		req.setEndCommissionRate(123L);
		req.setStartTotalAction(1L);
		req.setEndTotalAction(100L);
		req.setStartAuctionCount(123L);
		req.setEndAuctionCount(200L);*/
        req.setPlatform(1L);
        req.setPageNo(1L);
        req.setPageSize(20L);
        req.setFields("user_id,shop_title,shop_type,seller_nick,pict_url,shop_url");
        TbkShopGetResponse rsp = client.execute(req);
        resultMap.put("data", rsp.getBody());
        return resultMap;
    }
    /**
     * 淘口令生成接口
     * @return
     * @throws Exception
     */
    @Override
    public Map<String,Object> taoCode(TbkTpwdCreateRequest req) throws Exception{
        resultMap = new HashMap<String,Object>();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
//        req.setText("长度大于5个字符");
//        req.setUrl("https://uland.taobao.com/");
//        req.setLogo("https://uland.taobao.com/");
        req.setExt("{}");
        TbkTpwdCreateResponse rsp = client.execute(req);
        resultMap.put("data", rsp.getBody());
        return resultMap;
    }
    /**
     * 淘抢购接口
     * @return
     * @throws Exception
     */
    @Override
    public Map<String,Object> taoQiangGou(String pageNo,String pageSize,String startTime,String endTime) throws Exception{
        resultMap = new HashMap<String,Object>();
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        TbkJuTqgGetRequest req=new TbkJuTqgGetRequest();
        req.setPageNo(Long.parseLong(pageNo));
        req.setPageSize(Long.parseLong(pageSize));
        req.setAdzoneId(adzoneId);
        req.setFields("click_url,pic_url,reserve_price,zk_final_price,total_amount,sold_num,title,category_name,start_time,end_time");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        req.setStartTime(df.parse(startTime));
        req.setEndTime(df.parse(endTime));
        TbkJuTqgGetResponse  rsp = client.execute(req);
        resultMap.put("data", rsp.getBody());
        return resultMap;
    }
    //获取几天后的时间
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

}

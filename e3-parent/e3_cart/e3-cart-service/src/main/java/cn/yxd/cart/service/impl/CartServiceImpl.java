package cn.yxd.cart.service.impl;

import cn.yxd.cart.service.CartService;
import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.mapper.TbItemMapper;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Value(value = "${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public E3Result addItem(Long userId, Long itemId, Integer num) {
        //将用户的Cart添加进redis缓存
        //添加的redis的缓存的数据类型hset key：用户的id filed：ItemId value：TbItem
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        //判断商品是否存在
        if(hexists){
            String hget = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
            //如果存在，则进行商品数量得相加
            tbItem.setNum(tbItem.getNum()+num);
            jedisClient.hset(REDIS_CART_PRE + ":" + userId,itemId + "",JsonUtils.objectToJson(tbItem));
            return E3Result.ok();
        }
        //不存在，则根据商品的id查询数据库
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //设置商品数量及其图片
        tbItem.setNum(num);
        String image = tbItem.getImage();
        if(StringUtils.isNoneBlank(image)){
            String[] split = image.split(",");
            tbItem.setImage(split[0]);
        }
        jedisClient.hset(REDIS_CART_PRE + ":" + userId,itemId + "",JsonUtils.objectToJson(tbItem));
        //将商品信息返回
        return E3Result.ok();

    }

    @Override
    public E3Result mergeCart(Long userId, List<TbItem> tbItem) {
        //对商品列表进行合并
        for (TbItem t:tbItem){
            addItem(userId,t.getId(),t.getNum());
            //判断商品是否存在
            //如果存在，则进行商品数量的相加
            //如果不存在，则进行查询数据库
            //返回成功
        }

        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCart(Long userId) {
        //根据用户的id查询商品列表
        List<String> json = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> list=new ArrayList<>();
      if (json.size()>0&&json!=null){
          for (String s:json){
              TbItem tbItem = JsonUtils.jsonToPojo(s, TbItem.class);
              list.add(tbItem);
          }
          return  list;
      }
        return new ArrayList<>();
    }

    @Override
    public E3Result deleteCart(Long userId, Long itemId) {
        //更具redis中的key 和 filed删除对应的商品列表
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
        //根据缓存的key查询信息
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (json!=null){
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            //更新商品的数量
            tbItem.setNum(num);
            //重新写入redis中
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
            return  E3Result.ok();
        }
        return E3Result.build(400,"更新商品数量写入redis缓存失败");
    }

    @Override
    public E3Result deleteAllCart(Long userId) {
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }


}

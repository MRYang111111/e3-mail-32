package cn.yxd.orders.service.impl;

import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.util.E3Result;
import cn.yxd.mapper.TbOrderItemMapper;
import cn.yxd.mapper.TbOrderMapper;
import cn.yxd.mapper.TbOrderShippingMapper;
import cn.yxd.orders.pojo.OrdersQueryVo;
import cn.yxd.orders.service.OrdersService;
import cn.yxd.pojo.TbOrder;
import cn.yxd.pojo.TbOrderItem;
import cn.yxd.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private JedisClient jedisClient;

    //生成order_id的key
    @Value(value = "${ORDER_ID_GEN_KEY}")
    private  String ORDER_ID_GEN_KEY;

    //redos中保存TbOrder的初始化值：
    @Value(value = "${ORDER_ID_GEN_KEY_START}")
    private  String ORDER_ID_GEN_KEY_START;

//
//    #redis中自动生成TbOrderItem的id
//            TBORDERITEM_ID=TBORDERITEM_ID
    @Value(value = "${TBORDERITEM_ID}")
    private  String TBORDERITEM_ID;


    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Override
    public E3Result createOrders(OrdersQueryVo ordersQueryVo) {
        //赋予key一个初始值
        if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_GEN_KEY_START);
        }
        //使用redis额inre方法自动的生成order_id
        String order_id = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        //补全TbOrder的属性
        ordersQueryVo.setOrderId(order_id);
        ordersQueryVo.setCreateTime(new Date());
        ordersQueryVo.setUpdateTime(new Date());
        //status` int(10) DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
        //新生成的订单都是未付款的
        ordersQueryVo.setStatus(1);
        //向TbOrder表中插入数据
        tbOrderMapper.insert(ordersQueryVo);
        //向TbOrderItem表插入数据
        List<TbOrderItem> orderItems = ordersQueryVo.getOrderItems();
        for(TbOrderItem tbOrderItem:orderItems){
            //补全属性
            String id = jedisClient.incr(TBORDERITEM_ID).toString();
            tbOrderItem.setId(id);
            tbOrderItem.setOrderId(order_id);
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //向Tbshopping表中插入数据
        TbOrderShipping orderShipping = ordersQueryVo.getOrderShipping();
        orderShipping.setOrderId(order_id);
        orderShipping.setUpdated(new Date());
        orderShipping.setCreated(new Date());
        tbOrderShippingMapper.insert(orderShipping);
        //返回一个订单的id
        return E3Result.ok(order_id);
    }
}

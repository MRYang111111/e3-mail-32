package cn.yxd.orders.service;

import cn.yxd.common.util.E3Result;
import cn.yxd.orders.pojo.OrdersQueryVo;

public interface OrdersService {
    //提交订单向OrderQueryVo包装类中插入数据
    public E3Result createOrders(OrdersQueryVo ordersQueryVo);
}

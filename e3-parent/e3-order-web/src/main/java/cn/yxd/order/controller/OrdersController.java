package cn.yxd.order.controller;

import cn.yxd.cart.service.CartService;
import cn.yxd.common.util.E3Result;

import cn.yxd.orders.pojo.OrdersQueryVo;
import cn.yxd.orders.service.OrdersService;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbUser;
import cn.yxd.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class OrdersController {
    @Autowired
    private CartService cartService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OrdersService ordersService;
    //订页面的展示
    @RequestMapping(value = "/order/order-cart")
    public String showOrders(HttpServletRequest request) {
        /**
         * 确认订单后跳转到丁丹页面
         *
         */
        //取出用户的id
        TbUser  user = (TbUser) request.getAttribute("user");
        //根据用户的id取出收货列表
        //取出静态的数据
        //取支付方式
        //静态数据
        //根据用户的id查询商品列表
        List<TbItem> cart = cartService.getCart(user.getId());
        //对jsp进行传值
        request.setAttribute("cartList",cart);
        //跳转到逻辑视图
        return "order-cart";
    }

    @RequestMapping(value = "/order/create")
    public String getOrdersInfo(OrdersQueryVo querVo,HttpServletRequest request){

        //取出用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //补全queryVo的user属性
        querVo.setUserId(user.getId());
        querVo.setBuyerNick(user.getUsername());
        //调用服务端
        E3Result e3Result = ordersService.createOrders(querVo);
        //删除购物车里面的内容
        if(e3Result.getStatus()==200){
            cartService.deleteAllCart(user.getId());
        }
        //将信息传递到页面
        request.setAttribute("orderId",querVo.getOrderId());
        request.setAttribute("payment",querVo.getPayment());
        //返回成功
        return  "success";

    }

}

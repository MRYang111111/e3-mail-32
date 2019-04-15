package cn.yxd.cart.controller;

import cn.yxd.cart.service.CartService;
import cn.yxd.common.util.CookieUtils;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbUser;
import cn.yxd.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    /***
     * a)	使用拦截器实现。
     b)	Cookie中取token
     c)	取不到token跳转到登录页面
     d)	取到token，根据token查询用户信息。
     e)	如果没有用户信息，登录过期跳转到登录页面
     f)	取到用户信息，放行。

     */

    @Autowired
    private ItemService itemService;
    @Value(value = "${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;
    @Autowired
    private CartService castService;
    /**
     * 商品购物车的管理,返回商品添加成功的画面，
     */
    //参数：商品的id，商品的数量num
    @RequestMapping(value = "/cart/add/{itemId}")
    public  String addCartItem(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        //在添加购物车之前，判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //如果user不为空：则说明用户已经登入
        if(user!=null){
            //调用服务端，将数据戏写入到redis缓存
            E3Result e3Result = castService.addItem(user.getId(), itemId, num);
            return "cartSuccess";

        }
        //如果为空，那么就将数据写入到cookie中
        //根据商品的cookie取出商品的信息
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品的cookie是否存在
        boolean flag=false;
     for (TbItem tbItem:cartList){
         //判断他们的标准是他们的id是否相等
         if (tbItem.getId()==itemId.longValue()){

             //如果存在则将商品属数量相加
             tbItem.setNum(tbItem.getNum()+num);
             flag=true;
             break;
         }
     }
     if(!flag){
         //如果不存在，则将根据商品额id查询数据库，查询出TbItem
         TbItem tbItem = itemService.selectItemById(itemId);
         //设置商品数量
         tbItem.setNum(1);
         String image = tbItem.getImage();
         if(StringUtils.isNoneBlank(image)){
             String[] split = image.split(",");
             tbItem.setImage(split[0]);
         }
         //把商品添加进购物车列表
         cartList.add(tbItem);
     }
         //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),CART_COOKIE_EXPIRE,true);

        //返回一个逻辑视图，购物车添加成功的页面
        return "cartSuccess";

    }
    //从商品中取出商品列表
    private List<TbItem> getCartListFromCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, "cart",true);
        if (StringUtils.isNoneBlank(json)){
            List<TbItem> tbItemList = JsonUtils.jsonToList(json, TbItem.class);
            return  tbItemList;
        }
        return  new ArrayList<>();
    }
    //从cookie中取出商品列表
    @RequestMapping(value = "/cart/cart")
    public String getCartList(HttpServletRequest request,HttpServletResponse response){
        /***
         * 在展示购物车列表是判断用户是否登入
         *
         */
        //判断用户是否登入
        TbUser user = (TbUser) request.getAttribute("user");
        //从cookie中取出商品信息
        List<TbItem> cartListFromCookie = getCartListFromCookie(request);
        //用户已经的登入
        if (user!=null){
            //如果不为空，那么将调用服务端的域cookie中的商品列表进行合并
            castService.mergeCart(user.getId(),cartListFromCookie);
            //把cookie中的列表删除
            CookieUtils.deleteCookie(request,response,"cart");
            //重服务端取出商品列表
            cartListFromCookie=castService.getCart(user.getId());
        }
        //取出服务端的商品列表
        //传递给页面
        request.setAttribute("cartList",cartListFromCookie);
        //返回逻辑视图
        return  "cart";

    }
    //更新购物车列表
    ///cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".html"
    @RequestMapping(value = "/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartList(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
        //登入判断
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            castService.updateCartNum(user.getId(),itemId,num);
            return  E3Result.ok();
        }
        //从cookie查询商品列表
        List<TbItem> list = getCartListFromCookie(request);
        //遍历商品列表
        for (TbItem tbItem:list){
            if(tbItem.getId()==itemId.longValue()){
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //继续写入cookie中
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(list),CART_COOKIE_EXPIRE,true);
        //放回成功
        return  E3Result.ok();

    }
    //删除购物车列表
    ///cart/delete/${cart.id}.html
    @RequestMapping(value = "/cart/delete/{itemId}")
    public  String deleteCartById(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //登入判断
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            castService.deleteCart(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        //        //从cookie中取出商品信息
        List<TbItem> list = getCartListFromCookie(request);
        //根据商品的id查询商品信息
        for (TbItem tbItem:list){
            if(tbItem.getId().longValue()==itemId){
                //遍历商品列表，如果id相等从list中移除
                list.remove(tbItem);
                //删除后跳出循环，否则会抛异常
                break;
            }
        }
        //从新写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(list),CART_COOKIE_EXPIRE,true);
        //从定向到逻辑视图
        return "redirect:/cart/cart.html";

    }


}

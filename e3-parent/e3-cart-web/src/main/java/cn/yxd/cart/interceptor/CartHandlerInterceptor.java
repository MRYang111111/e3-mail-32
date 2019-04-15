package cn.yxd.cart.interceptor;

import cn.yxd.cart.service.CartService;
import cn.yxd.common.util.CookieUtils;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbUser;
import cn.yxd.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Action;

public class CartHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;


    @Autowired
    private CartService cartService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
//        //前处理
//        //重cookie中取出token，
//        String token = CookieUtils.getCookieValue(request, "token");
//        //如果token不存在，则需要从定向到登入页面
//        if(StringUtils.isBlank(token)){
//           //放行
//            return  true;
//        }
//        //如果token存在，需要判断token是否过期
//        E3Result userByToken = tokenService.getUserByToken(token);
//        if (userByToken.getStatus()==200){
//            //如果没过期则需要，取出用户信息，保存到request域
//            TbUser  user = (TbUser) userByToken.getData();
//            request.setAttribute("user",user);
//            //判断购物车是否有东西，合并购物车
//            String cart = CookieUtils.getCookieValue(request, "cart", true);
//            if(StringUtils.isNoneBlank(cart)){
//                cartService.mergeCart(user.getId(),JsonUtils.jsonToList(cart,TbItem.class));
//            }
//            return  true;
//
//        }else {
//
//            return  true;
        // 前处理，执行handler之前执行此方法。
        //返回true，放行	false：拦截
        //1.从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //2.如果没有token，未登录状态，直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //3.取到token，需要调用sso系统的服务，根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //4.没有取到用户信息。登录过期，直接放行。
        if (e3Result.getStatus() != 200) {
            return true;
        }
        //5.取到用户信息。登录状态。
        TbUser user = (TbUser) e3Result.getData();
        //6.把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行
        request.setAttribute("user", user);
        return true;

        }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //后处理，在ModdelAndView方法执行之前

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //完成处理，在ModelAndView完成之后

    }
}

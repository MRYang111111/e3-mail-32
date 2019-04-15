package cn.yxd.orders.interceptor;

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

public class LoginInterceptor implements HandlerInterceptor {
    /*

    preHandle：预处理回调方法，实现处理器的预处理（如登录检查），第三个参数为响应的处理器（如我们上一章的Controller实现）；
     返回值：true表示继续流程（如调用下一个拦截器或处理器）；
                 false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
postHandle：后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。

afterCompletion：整个请求处理完毕回调方法，即在视图渲染完毕时回调（响应返回到视图后），如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中preHandle返回true的拦截器的afterCompletion。

     */
    @Autowired
    private TokenService tokenService;

    @Value(value = "${SSO_URL}")
    private String SSO_URL;

    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前处理
        //重cookie中取出token，
        String token = CookieUtils.getCookieValue(request, "token");
        //如果token不存在，则需要从定向到登入页面
        if (StringUtils.isBlank(token)) {
            //跳转到登入界面，用户登入成功后，跳转到请求的url,比如说是从登入的界面重定向到请求额url
            response.sendRedirect(SSO_URL + "/page/login?redirect" + request.getRequestURI());
            return false;
        }
        //如果token存在，需要判断token是否过期
        E3Result userByToken = tokenService.getUserByToken(token);
        if (userByToken.getStatus() == 200) {
            //如果没过期则需要，取出用户信息，保存到request域
            TbUser user = (TbUser) userByToken.getData();
            request.setAttribute("user", user);
            //判断购物车是否有东西，合并购物车
            String cart = CookieUtils.getCookieValue(request, "cart", true);
            if (StringUtils.isNoneBlank(cart)) {
                cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cart, TbItem.class));
            }
            return true;

        }
        //如果已经过期，则需要用户从新登入
            //跳转到登入界面，用户登入成功后，跳转到请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect" + request.getRequestURI());
            return false;
    }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

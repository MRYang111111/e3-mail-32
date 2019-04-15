package cn.yxd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionResolver implements HandlerExceptionResolver {
    //创建日志对象
    Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //写入日志文件
        logger.error("系统发生错误!!!");
        //发邮件
        //返回结果页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("error","系统发生异常，请稍后重试");
        modelAndView.setViewName("error/exception.jsp");
        return modelAndView;
    }
}

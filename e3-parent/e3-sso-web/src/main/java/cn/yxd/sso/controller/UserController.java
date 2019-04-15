package cn.yxd.sso.controller;

import cn.yxd.common.util.CookieUtils;
import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbUser;
import cn.yxd.sso.service.LoginService;
import cn.yxd.sso.service.RegisterService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private LoginService loginService;
    //返回视图
    @RequestMapping(value = "/page/register")
    public  String returnPage(){
        return  "register";
    }
    ///user/check/"+escape($("#regName").val())+"/1?r="
    //用户的注册验证
    @RequestMapping(value = "/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkdata(@PathVariable String param,@PathVariable Integer type ){
        E3Result e3Result = registerService.checkData(param, type);
        return  e3Result;
    }
    //用户的注册
    @RequestMapping(value = "/user/register")
    @ResponseBody
    public  E3Result userRegister(TbUser tbUser){
        E3Result e3Result = registerService.userRegister(tbUser);
        return  e3Result;
    }
    //返回登入色逻辑视图
    @RequestMapping(value = "/page/login")
    public  String returnLogin(String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return  "login";

    }
    //用户登入的实现
    @RequestMapping(value = "/user/login")
    @ResponseBody
    public  E3Result userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){
        E3Result e3Result = loginService.loginUser(username, password);
        if(e3Result.getStatus()==200){
            //获取token
            String  token = (String) e3Result.getData();
            //将token写入到session中
            CookieUtils.setCookie(request,response,"token",token);

        }
        return  e3Result;
    }
    @RequestMapping(value = "/user/logout")
    public String userLoginOut(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.setCookie(request,response,null,null);
        return  "login";
    }


}

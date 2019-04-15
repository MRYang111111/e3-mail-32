package cn.yxd.sso.controller;

import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {
    //获取token信息查询redis
    @Autowired
    private TokenService tokenService;
    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String  getUserByToken(@PathVariable String token,String callback){
        //在判断结果是否为jsonp
        E3Result e3Result = tokenService.getUserByToken(token);
        if (StringUtils.isNoneBlank(callback)){
            return  callback+"("+ JsonUtils.objectToJson(e3Result)+")";
        }
        return  JsonUtils.objectToJson(e3Result);

    }
}

package cn.yxd.sso.service;

import cn.yxd.common.util.E3Result;

public interface LoginService {
    //用户登入的验证
    //参数：用户名，密码
    public E3Result loginUser(String username,String password);
}


package cn.yxd.sso.service;

import cn.yxd.common.util.E3Result;

public interface TokenService {
    //根据token取出用户信息
    public E3Result getUserByToken(String token);
}

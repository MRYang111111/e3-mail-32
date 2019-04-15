package cn.yxd.sso.service;

import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbUser;

public interface RegisterService {
    //用户注册的管理
    public E3Result checkData(String param, Integer type);
    //用户的注册
    public E3Result userRegister(TbUser user);

}

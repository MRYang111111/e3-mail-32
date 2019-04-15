package cn.yxd.sso.service.impl;

import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.mapper.TbUserMapper;
import cn.yxd.pojo.TbUser;
import cn.yxd.pojo.TbUserExample;
import cn.yxd.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class LoginServiceImpl implements LoginService{
    /**
     * 用户的登入管理
     * @param username
     * @param password
     * @return
     */
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbUserMapper tbUserMapper;
    @Value(value = "${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result loginUser(String username, String password) {
        //1.根据用户的密码和用户名查询数据库是否正确
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //根据用户名查询数据库
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        //2.如果不正确，则将返回用户信息没有查到
        if(list.size()==0||list==null){
            E3Result.build(400,"用户名或密码错误");
        }
        //3.如果正确，则生成token
        String token= UUID.randomUUID().toString();
        //取出用户信息
        TbUser tbUser = list.get(0);
        //比对密码
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser)){
            E3Result.build(400,"用户名或密码错误");
        }
        tbUser.setPassword(null);
        //4.将生成色token写入到redis缓存中
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
        //5设置session的过期时间
        jedisClient.expire("SESSION:"+token,SESSION_EXPIRE);
        //5.将token返回，在表现出写入cookie

        return E3Result.ok(token);
    }
}

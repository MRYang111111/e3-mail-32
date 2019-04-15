package cn.yxd.sso.service.impl;

import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.pojo.TbUser;
import cn.yxd.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    /**
     * 根据输出的token查询redis取出token信息
     * @param token
     * @return
     */
    @Value(value = "${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    private TbUser tbUser=null;
    @Override
    public E3Result getUserByToken(String token) {

        try{
            //根据redis取出用户信息
            String json= jedisClient.get("SESSION:" + token);
            if(StringUtils.isNoneBlank(json)){
                //如果用户信息在redis中没有过期，说明存在，则将用户信息添加进E3Result中
                 tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
                return E3Result.ok(tbUser);
            }else {
                //如果不存在则提示用户信息以及过期
                E3Result.build(201,"用户信息已经过期，请重新登入");
            }
            //重置过期时间
            jedisClient.expire("SESSION:" + token,SESSION_EXPIRE);
            //返回结果
        }catch (Exception e){
            e.printStackTrace();
        }
        return E3Result.ok(tbUser);
    }
}

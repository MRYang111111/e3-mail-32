package cn.yxd.sso.service.impl;

import cn.yxd.common.util.E3Result;
import cn.yxd.mapper.TbUserMapper;
import cn.yxd.pojo.TbUser;
import cn.yxd.pojo.TbUserExample;
import cn.yxd.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
/*
用户注册的判断
如果已经注册返回false
没有就返回true
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Override
    public E3Result checkData(String param, Integer type) {
        TbUserExample example=new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //type=1：用户名 type=2：手机 type=3：邮箱

        /**
         * 根据传入的参数查询用户是否已经注册
         */
        if(type==1){
            criteria.andUsernameEqualTo(param);
        }else  if(type==2){
            criteria.andPhoneEqualTo(param);
        }else  if(type==3){
            criteria.andEmailEqualTo(param);
        }else {
            E3Result.build(400,"数据类型错误");
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        //如果已经存在则返回false
        if(list.size()>0&&list!=null){
            return  E3Result.ok(false);
        }
        //不存在返回true

        return E3Result.ok(true);
    }

    @Override
    public E3Result userRegister(TbUser user) {
        //数据有效性得验证
        if("".equals(user.getUsername())||"".equals(user.getPhone())||"".equals(user.getPhone())){
            return  E3Result.build(400,"用户数据不完整");
        }
        E3Result e3Result = checkData(user.getUsername(), 1);
        if(!(boolean)e3Result.getData()){
            return  E3Result.build(400,"用户名已经被占用");

        }
        e3Result=checkData(user.getPhone(),2);
        if(!(boolean)e3Result.getData()){
            return  E3Result.build(400,"用户名已经被占用");
        }
        //补全pojo属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行加密
        String password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);

        //将用户的数据添加到数据库
        tbUserMapper.insert(user);
        //返回ok
        return E3Result.ok();

    }
}

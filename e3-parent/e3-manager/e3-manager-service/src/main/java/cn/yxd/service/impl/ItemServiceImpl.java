package cn.yxd.service.impl;

import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.IDUtils;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.mapper.TbItemDescMapper;
import cn.yxd.pojo.TbItemDesc;
import cn.yxd.pojo.TbItemDescExample;
import cn.yxd.service.ItemService;
import cn.yxd.mapper.TbItemMapper;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemExample;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
@Service
@Transactional
public class ItemServiceImpl implements ItemService{
    /*
    商品管理信息

     */
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private  TbItemDescMapper tbItemDescMapper;
   @Autowired
    private Destination queueDestination;
   @Autowired
   private JedisClient jedisClient;

   @Value(value = "${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

   @Value(value = "${ITEM_INFO_EXPIRE}")
   private  Integer ITEM_INFO_EXPIRE;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Override
    public TbItem selectItemById(Long itemId) {
        try {
            //查询缓存
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            String s = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNoneBlank(s)){
                TbItem tbItem1 = JsonUtils.jsonToPojo(s, TbItem.class);
                return  tbItem1;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemExample tbItem=new TbItemExample();
        //创建查询条件
        TbItemExample.Criteria criteria = tbItem.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = tbItemMapper.selectByExample(tbItem);
        //判断是否集合为空
        if (list!=null&&list.size()>0){

            try{
                //把数据缓存到数据中
                jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE",JsonUtils.objectToJson(list.get(0)));
                jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_INFO_EXPIRE);

            }catch (Exception e){
            e.printStackTrace();
            }

        }
        return list.get(0);
    }

    @Override
    public EasyUiDataGridResult selectItemList(Integer page, Integer rows) {
        //设置分页数据
        PageHelper.startPage(page,rows);
        //设置查询对象
        TbItemExample tbItemExample=new TbItemExample();
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        if (tbItemList!=null&&tbItemList.size()>0){

            //获取分页信息
            PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(tbItemList);
            //创建工具类对象
            EasyUiDataGridResult easyUiDataGridResult=new EasyUiDataGridResult();
            easyUiDataGridResult.setTotal(pageInfo.getTotal());
            easyUiDataGridResult.setRows(tbItemList);
            return  easyUiDataGridResult;
        }

        return null;
    }

    @Override
    public E3Result addItem(TbItem  tbItem, String desc) {

        //向商品表Tb_item中和商品描述表Tb_Desc中插入数据
        //采用工具类,随机生成id
        final long id = IDUtils.genItemId();
        //插入数据
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setUpdated(new Date());
        tbItem.setCreated(new Date());
        //调用dao的方法
        tbItemMapper.insert(tbItem);
        //向TB_desc表中插入数据
        TbItemDesc tbItemDesc=new TbItemDesc();
        //补全属性
        tbItemDesc.setItemId(id);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        //调用dao的方法
        tbItemDescMapper.insert(tbItemDesc);
        //发送消息：商品表插入信息后同步到所索引库
        jmsTemplate.send(queueDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(id+"");
            }
        });
        //向数据中添加缓存


        return E3Result.ok();
    }

    @Override
    public TbItemDesc selectTbItemDescById(Long id) {
        TbItemDescExample tbItemDescExample=new TbItemDescExample();
        TbItemDescExample.Criteria criteria = tbItemDescExample.createCriteria();
        criteria.andItemIdEqualTo(id);
        List<TbItemDesc> list = tbItemDescMapper.selectByExample(tbItemDescExample);

        return list.get(0);
    }

    @Override
    public E3Result updateItem(TbItem tbItem, String desc) {
        //设置参数
        TbItem tbItem1=new TbItem();
        tbItem1 .setId(tbItem.getId());

        tbItem1.setTitle(tbItem.getTitle());
        tbItem1.setSellPoint(tbItem.getSellPoint());
        tbItem1.setPrice(tbItem.getPrice());
        tbItem1.setNum(tbItem.getNum());
        tbItem1.setBarcode(tbItem.getBarcode());
        tbItem1.setCid(tbItem.getCid());
        tbItem1.setStatus((byte) 1);
        tbItem1.setCreated(new Date());
        tbItem1.setUpdated(new Date());
        tbItemMapper.updateByPrimaryKey(tbItem1);
        //更改描述表（TbItemDesc）
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.updateByPrimaryKey(tbItemDesc);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteTbItemsByIds(String ids) {
        //遍历对象
      if(StringUtils.isNoneBlank(ids)){
          String[] split = ids.split(",");
          for (String  s:split){
              Long l=Long.valueOf(s);
              tbItemMapper.deleteByPrimaryKey(l);
          }
      }

        return E3Result.ok();
    }

    @Override
    public E3Result updateStatue1(String ids) {
        String[] split = ids.split(",");
        for (String  s:split){
            Long l=Long.valueOf(s);
           tbItemMapper.updateStaues1(l);
        }
        return E3Result.ok();
    }
    @Override
    public E3Result updateStatue2(String ids) {
        String[] split = ids.split(",");
        for (String  s:split){
            Long l=Long.valueOf(s);
            tbItemMapper.updateStaues2(l);
        }
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getDescById(Long itemid) {
        //查询缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemid + ":DESC");
            if(StringUtils.isNoneBlank(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(REDIS_ITEM_PRE + ":" + itemid + ":DESC", TbItemDesc.class);
                return  tbItemDesc;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemid);
        //添加缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE+":"+itemid+":DESC",JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE+":"+itemid+":DESC",ITEM_INFO_EXPIRE);
        }catch (Exception e){
           e.printStackTrace();
        }
        return  tbItemDesc;
    }

}

package cn.yxd.content.service.impl;

import cn.yxd.common.jedis.JedisClient;
import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.common.util.JsonUtils;
import cn.yxd.content.service.ContentService;
import cn.yxd.mapper.TbContentMapper;
import cn.yxd.pojo.TbContent;
import cn.yxd.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    //注入Redis缓存
    @Autowired
    private JedisClient jedisClient;
    //Redis的Hset中的key
    @Value(value = "${Content_List}")
    private String Content_List;
    /**
     * 对TBContent中的表进行分页
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUiDataGridResult selectContentPage(Integer page, Integer rows) {
        //开始分页
        PageHelper.startPage(page,rows);
        //查看分页条件
        TbContentExample tbContentExample=new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
        if (tbContents.size()>0&&tbContents!=null){
            PageInfo<TbContent> pageInfo=new PageInfo<TbContent>(tbContents);
          //实例化对象
            EasyUiDataGridResult easyUiDataGridResult=new EasyUiDataGridResult();
            //设置总记录数
            easyUiDataGridResult.setTotal(pageInfo.getTotal());
            easyUiDataGridResult.setRows(tbContents);
            return  easyUiDataGridResult;

        }

        return null;
    }

    @Override
    public E3Result addContent(TbContent tbContent) {
        /**
         * 对Content表进行添加
         *
         */
        tbContent.setUpdated(new Date());
        tbContent.setCreated(new Date());
        tbContentMapper.insert(tbContent);
        //添加缓存同步，删除缓存中对应德数据
//        jedisClient.hdel(Content_List,tbContent.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public E3Result deleteContent(String id) {
        //删除
        if(StringUtils.isNoneBlank(id)){
            String[] split = id.split(",");
            for (String s:split){
                Long l=Long.valueOf(s);
                tbContentMapper.deleteByPrimaryKey(l);
            }
        }
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentList(Long categoryId) {

        //
        //向redis中添加缓存：
        //Key：cid
        //Value：内容列表。需要把java对象转换成json。
        /****
         * 向业务中添加缓存
         *
         * 1查询缓存，如果查询到缓存就把他添加进缓存中
         * 2.查询不到缓存，泽进行数据库的查询
         * 3.把查询的结果添加进缓存中
         * 4返回
         *
         *
         *
         */
        try {
            //查询缓存
            String json = jedisClient.hget(Content_List, categoryId + "");
            if (StringUtils.isNoneBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return   list;
            }
        }catch (Exception e){
             e.printStackTrace();
        }
        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        try {
            //向缓存中添加数据
            if (list.size()>0&&list!=null){
            jedisClient.hset(Content_List,categoryId+"", JsonUtils.objectToJson(list));
                return  list;
            }else {
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  list;
    }
}

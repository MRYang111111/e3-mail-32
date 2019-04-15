package cn.yxd.content.service.impl;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.pojo.EasyUiTheeNode;
import cn.yxd.common.util.E3Result;
import cn.yxd.content.service.ContentCategoryService;
import cn.yxd.mapper.TbContentCategoryMapper;
import cn.yxd.pojo.TbContent;
import cn.yxd.pojo.TbContentCategory;
import cn.yxd.pojo.TbContentCategoryExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    //注入dao
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

//查询树形节点
    @Override
    public List<EasyUiTheeNode> getContentCategory(Long parentId) {
        //查询ContentCategory的TbContentCategory
        TbContentCategoryExample tbContentCategoryExample=new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //根据参数进行查询
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        List<EasyUiTheeNode> list=new ArrayList<>();
        //遍历对象，把他装进集合
        for (TbContentCategory t:tbContentCategories){
            EasyUiTheeNode easyUiTheeNode=new EasyUiTheeNode();
            easyUiTheeNode.setId(t.getId());
            easyUiTheeNode.setState(t.getIsParent()?"closed":"open");
            easyUiTheeNode.setText(t.getName());
            list.add(easyUiTheeNode);
        }
        if(list!=null&&list.size()>0){
            return  list;
        }
        return null;
    }
//添加叶子节点
    @Override
    public E3Result addContentCategory(Long parentId, String name) {
       ;//id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目ID',
      //创建一个表对应德pojo属性
        TbContentCategory tbContentCategory= new TbContentCategory();
        //设置pojo的属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setCreated(new Date());
        //is_parent` tinyint(1) DEFAULT '1' COMMENT '该类目是否为父类目，1为true，0为false',
       tbContentCategory.setIsParent(false);
       //  `status` int(1) DEFAULT '1' COMMENT '状态。可选值:1(正常),2(删除)',
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        //插入到数据库
        tbContentCategoryMapper.insert(tbContentCategory);
        //判断一个父节点isParent，如果不是true，改为true
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回一个E3Result
        return  E3Result.ok(tbContentCategory);

    }

    //对请求的Ztree的叶子节点进行删除
    @Override
    public E3Result deleteContentCategoryById(Long id) {
        //判断是否是叶子节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        if(tbContentCategory.getIsParent()){
            tbContentCategory.setIsParent(false);
            tbContentCategoryMapper.deleteByPrimaryKey(id);
        }else if(!tbContentCategory.getIsParent()){
            tbContentCategoryMapper.deleteByPrimaryKey(id);
        }
        return E3Result.ok();
    }




}

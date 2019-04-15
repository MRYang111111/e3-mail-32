package cn.yxd.service.impl;

import cn.yxd.common.pojo.EasyUiTheeNode;
import cn.yxd.mapper.TbItemCatMapper;
import cn.yxd.pojo.TbItemCat;
import cn.yxd.pojo.TbItemCatExample;
import cn.yxd.pojo.TbItemExample;
import cn.yxd.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Override
    public List<EasyUiTheeNode> getEasyUiTreeNode(Long parentId) {
        //根据parentId查询子节点
        TbItemCatExample example=new TbItemCatExample();
        //建立查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //设置查询条件
         criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);
        ///创建结果返回对象
        List<EasyUiTheeNode> list=new ArrayList<>();
        for(TbItemCat tbItemCat:tbItemCats){
            EasyUiTheeNode easyUiTheeNode=new EasyUiTheeNode();
            easyUiTheeNode.setId(tbItemCat.getId());
            easyUiTheeNode.setText(tbItemCat.getName());
            easyUiTheeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加到列表
            list.add(easyUiTheeNode);

        }
        //返回结果
        return list;
    }
}

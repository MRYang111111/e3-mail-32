package cn.yxd.service;

import cn.yxd.common.pojo.EasyUiTheeNode;

import java.util.List;

public interface ItemCatService {
    //根据商品的parentId查询子节点
    public List<EasyUiTheeNode> getEasyUiTreeNode(Long parentId);

}

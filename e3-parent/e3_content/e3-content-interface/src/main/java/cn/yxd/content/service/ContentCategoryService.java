package cn.yxd.content.service;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.pojo.EasyUiTheeNode;
import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbContent;

import java.util.List;


public interface ContentCategoryService {
    //实现树形界面
    public List<EasyUiTheeNode> getContentCategory(Long parentId);
        //添加商品分类插入到数据库(TbItemCategory)
    public E3Result addContentCategory(Long parentId,String name);
    //根据叶子节点的id删除叶子节点
    public E3Result deleteContentCategoryById(Long id);
//    //对content表中的内容进行分页
//    public EasyUiDataGridResult selectContentPage(Integer page ,Integer rows);
}

package cn.yxd.content.service;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //对content表中的内容进行分页
    public EasyUiDataGridResult selectContentPage(Integer page , Integer rows);
    //对TbContent表进行添加
    public E3Result addContent(TbContent tbContent);
    //对TbContent进行删除
    public E3Result deleteContent(String id);
    //使用查询content表，参数内容分类的:CategoryId ,返回值List<TbContent>
    public List<TbContent> getContentList(Long categoryId);




}

package cn.yxd.search.mapper;


import cn.yxd.common.pojo.SearchItem;

import java.util.List;

public interface ItemListMapper {
    //导入到索引库
    public List<SearchItem> getItemList();
    //根据商品的id查询商品信息
    public  SearchItem getItemListById(Long id);
}

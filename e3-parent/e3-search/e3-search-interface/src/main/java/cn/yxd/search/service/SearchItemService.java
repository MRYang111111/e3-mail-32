package cn.yxd.search.service;

import cn.yxd.common.pojo.SearchResult;
import cn.yxd.common.util.E3Result;

public interface SearchItemService {
    //向solr中添加数据
    public E3Result getImportItemSearch();


}

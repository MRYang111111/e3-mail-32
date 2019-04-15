package cn.yxd.search.service;

import cn.yxd.common.pojo.SearchResult;

public interface SearchService {
    //使用solr实现搜索服务
    public SearchResult getSearchReault(String keyword, Integer page, Integer rows);
}

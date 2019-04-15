package cn.yxd.search.service.impl;

import cn.yxd.common.pojo.SearchResult;
import cn.yxd.search.dao.SearchDao;
import cn.yxd.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;
    @Override
    public SearchResult getSearchReault(String keyword, Integer page, Integer rows) {
        //创建查询的对象
        SolrQuery solrQuery=new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(keyword);
        solrQuery.setStart((page-1)*rows);
        solrQuery.setRows(rows);
        //设置默认的搜索域
        solrQuery.set("df","item_title");
        //设置高亮的显示
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<em style=\\\"color:red\\\">");
        solrQuery.setHighlightSimplePost("</em>");
        //执行查询
        SearchResult searchResult = searchDao.getSearchResult(solrQuery);
        //设置分页,计算总页数
        Long count = searchResult.getRecourdCount();
        Long totalPage=count/rows;
        if(count%rows>0){
            totalPage++;

        }
        searchResult.setTotalPages(totalPage);

        //返回结果集
        return searchResult;
    }
}

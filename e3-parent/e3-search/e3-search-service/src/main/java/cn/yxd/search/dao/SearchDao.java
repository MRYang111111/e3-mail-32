package cn.yxd.search.dao;

import cn.yxd.common.pojo.SearchItem;
import cn.yxd.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品列表的DAo
 */
@Repository
public class SearchDao implements Serializable{
    @Autowired
    private SolrServer solrServer;
    private     SearchResult searchResult=null;


    //参数：String keyWord
    //      int page
    //      int rows
    //书写一个方法，获取把查询搜索的服务分装到pojo中
    public SearchResult getSearchResult(SolrQuery solrQuery){
        try {
            searchResult=new SearchResult();
            //根据查询条件查询索引库
            QueryResponse queryResponse = solrServer.query(solrQuery);
            //创建查询文档对象
            SolrDocumentList results = queryResponse.getResults();
            //取出总记录数,并设置SearchREsult的参数
            Long numFound = results.getNumFound();
            searchResult.setRecourdCount(numFound);
            //取出商品列表，注意要取出高亮
            List<SearchItem>  searchItems=new ArrayList<>();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            for (SolrDocument solrDocument:results){
                //设置和分装到商品列表中
                SearchItem searchItem=new SearchItem();
                searchItem.setId((String) solrDocument.get("id"));
                searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
                searchItem.setImage((String) solrDocument.get("item_image"));
                searchItem.setPrice((Long) solrDocument.get("item_price"));
                searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
                //取出高亮的结果
                List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
                String title="";
                if(list.size()>0&&list!=null){
                    //如果不能查询到则进行正常显示
                    title=list.get(0);

                }else{
                    searchItem.setTitle((String) solrDocument.get("item_title"));
                }
                //添加到商品列表中
                searchItems.add(searchItem);
            }
            searchResult.setGetItemList(searchItems);
            //返回结果集
            return  searchResult;

        }catch (Exception e){
            e.printStackTrace();

        }
        return   searchResult;

    }

}

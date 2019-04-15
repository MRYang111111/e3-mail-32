package cn.yxd.search.service.impl;


import cn.yxd.common.pojo.SearchItem;
import cn.yxd.common.pojo.SearchResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.search.dao.SearchDao;
import cn.yxd.search.mapper.ItemListMapper;
import cn.yxd.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
@Service
@Transactional
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private ItemListMapper itemListMapper;

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchDao searchDao;
    @Override
    public E3Result getImportItemSearch() {
        try{
            //连接solr服务
            //查询TbItem表中所有得数据
            List<SearchItem> itemList = itemListMapper.getItemList();
            //遍历所有得数据
            for (SearchItem i:itemList){
                //创建查询文档
                SolrInputDocument document=new SolrInputDocument();
                //设置域对象
                document.addField("id",i.getId());
                document.addField("item_title",i.getTitle());
                document.addField("item_sell_point",i.getSell_point());
                document.addField("item_price",i.getPrice());
                document.addField("item_image",i.getImage());
                document.addField("item_category_name",i.getCategory_name());
                //将文档添加进服务
                solrServer.add(document);

            }
           //提交
            solrServer.commit();
            return E3Result.ok();

        }catch(Exception e){
            e.printStackTrace();
            return  E3Result.build(500,"数据导入到solr错误");
        }
    }

}

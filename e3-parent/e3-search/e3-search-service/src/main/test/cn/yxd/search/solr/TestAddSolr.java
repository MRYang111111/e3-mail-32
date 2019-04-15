package cn.yxd.search.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;

public class TestAddSolr {
    //添加商品测试服务
    @Test
    public  void TestSolr() throws  Exception{
        //连接商品服务‘
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.133:8080/solr");
        //添加文档对象,SolrInputDocument。
        SolrInputDocument inputFields=new SolrInputDocument();

        //向文档对象中添加域对象注意一对象要在scame.xml中配置
        inputFields.addField("id","001");
        inputFields.addField("item_title","测试商品服务");
        inputFields.addField("item_price","998");
        solrServer.add(inputFields);
        //提交
        solrServer.commit();

    }
    @Test
    public  void deleteDocument()throws  Exception{
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.133:8080/solr");
        solrServer.deleteByQuery("id:001");
        solrServer.commit();


    }
}

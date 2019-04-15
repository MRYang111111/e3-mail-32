package cn.yxd.e3_search_web.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestSolr {


    //测试solr的查询服务
    @Test
    public void TestQuery() throws SolrServerException {
        //连接solr服务
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        //获取solr的查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q", "*:*");
        solrQuery.setStart(1);
        solrQuery.setRows(10);
        //执行查询获取SolrResponse对象
        QueryResponse query = solrServer.query(solrQuery);
        //取出文档列表，查询出结果的总记录数
        SolrDocumentList results = query.getResults();
        //查询出结果的总记录数
        long numFound = results.getNumFound();
        System.out.println("查询出的总记录数是:" + numFound);
        //根据文档列表查询查询域对象
        for (SolrDocument solrDocument : results) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_category_name"));
        }
        //solr的复杂查询


    }

    //solr的复杂查询，带高亮的
    @Test
    public void testFuZa() throws SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
        //获取solr的查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件

        solrQuery.set("q", "手机");
        //设置分页
        solrQuery.setStart(1);
        solrQuery.setRows(10);

        //开启高亮
        solrQuery.setHighlight(true);
        //设置要查询的高亮域对象
        solrQuery.addHighlightField("item_title");
        //设置高亮的前缀
        solrQuery.setHighlightSimplePre("<em>");
        ///设置高亮的猴嘴
        solrQuery.setHighlightSimplePost("</em>");
        solrQuery.set("df", "item_title");
        //执行查询,获取QueryResponse对象
        QueryResponse query = solrServer.query(solrQuery);
        //获取文档对象
        SolrDocumentList results = query.getResults();
        System.out.println("查询结果总记录数：" + results.getNumFound());
        //获取高亮的结果
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        for (SolrDocument solrDocument : results) {
            System.out.println(solrDocument.get("id"));

            //高亮查询
            //设置高亮显示的域
            //  List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            //判断是否为空，如果为空，则正常的显示title，如果不为空则高亮显示
            String title = null;
            if (list != null && list.size() > 0) {
                System.out.println(list.get(0));
            } else {
                title = (String) solrDocument.get("item_title");
                //System.out.println(solrDocument.get("item_title"));
            }
//            System.out.println(title);
//            System.out.println(solrDocument.get("item_sell_point"));
//            System.out.println(solrDocument.get("item_price"));
//            System.out.println(solrDocument.get("item_category_name"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));

        }

        //对查询出的Item_title进行高亮显示

//使用solrJ实现查询
//        @Test
//        public void queryDocument () throws Exception {
//            //创建一个SolrServer对象
//            SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr");
//            //创建一个查询对象，可以参考solr的后台的查询功能设置条件
//            SolrQuery query = new SolrQuery();
//            //设置查询条件
////		query.setQuery("阿尔卡特");
//            query.set("q", "手机 ");
//            //设置分页条件
//            query.setStart(1);
//            query.setRows(2);
//            //开启高亮
//            query.setHighlight(true);
//            query.addHighlightField("item_title");
//            query.setHighlightSimplePre("<em>");
//            query.setHighlightSimplePost("</em>");
//            //设置默认搜索域
//            query.set("df", "item_title");
//            //执行查询，得到一个QueryResponse对象。
//            QueryResponse queryResponse = solrServer.query(query);
//            //取查询结果总记录数
//            SolrDocumentList solrDocumentList = queryResponse.getResults();
//            System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
//            //取查询结果
//            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
//            for (SolrDocument solrDocument : solrDocumentList) {
//                System.out.println(solrDocument.get("id"));
//                //取高亮后的结果
//                List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
//                String title = "";
//                if (list != null && list.size() > 0) {
//                    //取高亮后的结果
//                    title = list.get(0);
//                } else {
//                    title = (String) solrDocument.get("item_title");
//                }
//                System.out.println(title);
//                System.out.println(solrDocument.get("item_sell_point"));
//                System.out.println(solrDocument.get("item_price"));
//                System.out.println(solrDocument.get("item_image"));
//                System.out.println(solrDocument.get("item_category_name"));
//            }
//
//        }
    }
}












package cn.yxd.search.message;

import cn.yxd.common.pojo.SearchItem;
import cn.yxd.search.mapper.ItemListMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener{
    @Autowired
    private ItemListMapper itemListMapper;
    @Autowired
    private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        /*
         * 当商品添加完成后会发送一个TextMessage，包含有一个id，接收到消息后同步到索引库
         *
         */
        //接收文件的消息，即Item的id
        TextMessage textMessage= (TextMessage) message;
        try {
            String text = textMessage.getText();
            Long id=new Long(text);
            SearchItem searchItem = itemListMapper.getItemListById(id);
            if(searchItem!=null){
                //创建文档对象
                SolrInputDocument document=new SolrInputDocument();
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                //将商品信息同步到索引库
                solrServer.add(document);
                //提交
                solrServer.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

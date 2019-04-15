package cn.yxd.item.pojo;

import cn.yxd.pojo.TbItem;

public class Item extends TbItem {
    /*

    业务逻辑：
1、从url中取参数，商品id
2、根据商品id查询商品信息(tb_item)得到一个TbItem对象，缺少images属性，可以创建一个pojo继承TbItem，添加一个getImages方法。在e3-item-web工程中。

     */

    //构造方法初始化
    public  Item(TbItem tbItem){
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
        this.setSellPoint(tbItem.getSellPoint());
    }
    public  String[] getImages(){
        String image = this.getImage();
        if(null!=image&&!"".equals(image)){
            String[] split = image.split(",");
            return  split;
        }
    return  null;
    }
}

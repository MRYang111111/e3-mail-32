package cn.yxd.common.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable{
//    SELECT a.id,a.image,a.title,a.price,a.sell_point,b.`name` as category_name
//    FROM tb_item a LEFT JOIN tb_item_cat b ON a.cid=b.id
//    WHERE a.`status`=1
    private  String id;
    private String  title;
    private Long price;
    private String sell_point;
    private String category_name;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemList{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", sell_point='" + sell_point + '\'' +
                ", category_name='" + category_name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
    public  String[] getImages(){
        if(image!=null&&!"".equals(image)){
            String[] split = image.split(",");
            return split;
        }else {
            return  null;
        }


    }
}

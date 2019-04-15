package cn.yxd.service;

import cn.yxd.common.pojo.EasyUiDataGridResult;
import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {
    //根据订单的id查询出信息
    public TbItem selectItemById(Long itemId);
    //商品列表的分页查询，注使用插件查询
    public EasyUiDataGridResult selectItemList(Integer page, Integer rows);
    //向TB_item插入数据，和TB_desc表中插入数据
    public E3Result addItem(TbItem tbItem,String desc);
    //根据商品的描述查询出商品信息
    public TbItemDesc selectTbItemDescById(Long id);
//    public  E3Result  getDesc(Long id);
    public  E3Result updateItem(TbItem tbItem,String desc);
    //删除商品列表
    public  E3Result deleteTbItemsByIds(String ids);
    //商品的上架
    public  E3Result updateStatue1(String ids);
    //商品的下架
    public  E3Result updateStatue2(String ids);
    //根据商品的id查询商品描述
    public TbItemDesc getDescById(Long itemid);




}

package cn.yxd.cart.service;

import cn.yxd.common.util.E3Result;
import cn.yxd.pojo.TbItem;

import java.util.List;

public interface CartService {
    //写入redis缓存
    public E3Result addItem(Long userId,Long itemId,Integer num);
    //将cookie和商品列表进行合并
    public E3Result mergeCart(Long userId, List<TbItem> tbItem);
    //取出服务端的商品列表
    public List<TbItem> getCart(Long userId);
    //从服务端删除商品列表
    public E3Result deleteCart(Long userId,Long itemId);
    //重服务端更新商品额数量
    public  E3Result updateCartNum(Long userId,Long itemId,Integer num);
    //删除购物和
    public  E3Result deleteAllCart(Long userId);
}

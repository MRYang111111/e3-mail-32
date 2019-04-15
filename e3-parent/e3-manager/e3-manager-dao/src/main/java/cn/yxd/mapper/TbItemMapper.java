package cn.yxd.mapper;

import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TbItemMapper {
    int countByExample(TbItemExample example);

    int deleteByExample(TbItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItem record);

    int insertSelective(TbItem record);

    List<TbItem> selectByExample(TbItemExample example);

    TbItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByExample(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByPrimaryKeySelective(TbItem record);

    int updateByPrimaryKey(TbItem record);
    //修改商品的上架
    void  updateStaues1(Long id);
    //修改商品的下架
    void  updateStaues2(Long id);
}
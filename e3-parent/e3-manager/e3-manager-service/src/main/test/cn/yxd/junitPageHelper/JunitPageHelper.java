package cn.yxd.junitPageHelper;

import cn.yxd.mapper.TbItemMapper;
import cn.yxd.pojo.TbItem;
import cn.yxd.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;


public class JunitPageHelper {
    //测试分页插件


    public static void main(String[] args) {
        //获取配置文件
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        //获取bean对象
        TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
        //设置查询条件
        PageHelper.startPage(1,10);
        TbItemExample tbItemExample=new TbItemExample();
        //执行分页查询
        List<TbItem> tbItemList = itemMapper.selectByExample(tbItemExample);
        //获取分页信息
        PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(tbItemList);
        System.out.println(tbItemList.get(0));
        if (tbItemList.size()>0&&tbItemList!=null){
            System.out.println(tbItemList.get(0));
            System.out.println("总记录数:"+pageInfo.getTotal());
            System.out.println("当前页数"+pageInfo.getPages());
        }

    }


}

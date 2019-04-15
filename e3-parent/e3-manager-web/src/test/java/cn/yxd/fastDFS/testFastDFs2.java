package cn.yxd.fastDFS;

import cn.yxd.common.util.FastDFSClient;
import org.junit.Test;

public class testFastDFs2 {
    @Test
    public  void fun() throws Exception {
        FastDFSClient fastDFSClient=new FastDFSClient("D:/ideaProject/e3-parent/e3-manager-web/src/main/resources/client.conf");
        String s = fastDFSClient.uploadFile("C:/bi.jpg");
        System.out.println(s);


    }



}

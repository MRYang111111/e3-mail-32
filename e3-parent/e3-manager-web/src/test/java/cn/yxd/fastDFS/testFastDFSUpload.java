package cn.yxd.fastDFS;
import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;

public class testFastDFSUpload {
    public static void main(String[] args) throws IOException, MyException {
        //创建一个配置文件，内容是tracker_server=192.168.25.133:22122
        ClientGlobal.init("D:/ideaProject/e3-parent/e3-manager-web/src/main/resources/client.conf");
        //创建一个TrackerClient对象
        TrackerClient client=new TrackerClient();
        //使用TrackClient对象创建一个连接，过去一个TrackService的对象
        TrackerServer trackerServer = client.getConnection();
        //创建一个TrackService对象，为空
        StorageServer storageClien=null;
        ///StorageClient对象，需要两个参数trackerServer,storageClien
        StorageClient storageClient=new StorageClient(trackerServer,storageClien);
        String[] jpgs = storageClient.upload_file("C:/bi.jpg", "jpg", null);
        for (String s:jpgs){
            System.out.println(s);
        }


    }
}

package cn.yxd.controller;

import cn.yxd.common.pojo.PictureCommon;
import cn.yxd.common.util.FastDFSClient;
import cn.yxd.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PictureController {
    //绑定参数
    @Value("${MultipartFile_IMG_URL}")
    private  String MultipartFile_IMG_URL;

    @RequestMapping(value = "/pic/upload")
    @ResponseBody
    public String getPicture(MultipartFile uploadFile){
        //创建图片上传的工具类
        PictureCommon pictureCommon=new PictureCommon();
        try {

            //得到文件的名称
            String originalFilename = uploadFile.getOriginalFilename();
            //获得扩展名
            String extName= originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //创建客户端
            FastDFSClient fastDFSClient=new FastDFSClient("D:/ideaProject/e3-parent/e3-manager-web/src/main/resources/config/client.conf");
            String s= fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //拼接url
            String url=MultipartFile_IMG_URL+s;
            pictureCommon.setError(0);
            pictureCommon.setUrl(url);


        } catch (Exception e) {
            e.printStackTrace();
            pictureCommon.setError(1);
            pictureCommon.setUrl("图片上传错误!!!");
        }
        return   JsonUtils.objectToJson(pictureCommon);
    }


}

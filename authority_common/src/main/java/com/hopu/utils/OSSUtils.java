package com.hopu.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class OSSUtils {
    private static String accessKeyId = "LTAI4GK5yHNHCi3dXeYES1vh";
    private static String accessKeySecret = "pajoG97qtXD8vCk6gsWfjM2TnIPTZp";
    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static String bucketName = "erio";

    /**
     * 上传图片操作
     * @param objectName
     * @param inputStream
     */
    public static void uploadPic(String objectName,InputStream inputStream){
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, objectName, inputStream);
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载图片操作
     * @param objectName
     * @return
     */
    public static BufferedImage downloadPic(String objectName){
//        InputStream content = null;
//        BufferedReader reader = null;
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            InputStream content = ossObject.getObjectContent();
            BufferedImage image = ImageIO.read(content);
//            if (content != null) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                while (true){
//                    String line = reader.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    System.out.println("\n"+line);
//                }
//            }

            ossClient.shutdown();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 所有图片集合
     */
    public static void ListPic(){
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() + ")");
            }
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除图片操作
     * @param objectName
     */
    public static void deletePic(String objectName){
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.deleteObject(bucketName,objectName);
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

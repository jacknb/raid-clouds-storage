package com.adelmo.raid.domain.aliyun;

import com.aliyun.oss.OSSClient;

/**
 * Created by znb on 17-5-8.
 */
public class ALiYunOSSClient {

    // endpoint以杭州为例，其它region请按实际情况填写
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    // accessKey
    private static String accessKeyId = "<yourAccessKeyId>";

    private static String accessKeySecret = "<yourAccessKeySecret>";


    static {
        // 创建OSSClient实例
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 使用访问OSS
        // 关闭client
        client.shutdown();
    }
}

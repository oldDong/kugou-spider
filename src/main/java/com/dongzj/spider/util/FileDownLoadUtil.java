package com.dongzj.spider.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 10:47
 */
public class FileDownLoadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileDownLoadUtil.class);

    /**
     * 文件下载
     *
     * @param url  连接地址
     * @param path 要保存的路径文件名
     * @return
     */
    public static boolean download(String url, String path) {
        boolean flag = false;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(2000)
                .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            for (int i = 0; i < 3; i++) {
                CloseableHttpResponse result = httpClient.execute(httpGet);
                logger.info("status line:{}", result.getStatusLine());
                if (result.getStatusLine().getStatusCode() == 200) {
                    in = new BufferedInputStream(result.getEntity().getContent());
                    File file = new File(path);
                    out = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = in.read(buffer, 0, 1024)) > -1) {
                        out.write(buffer, 0, len);
                    }
                    flag = true;
                    break;
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            httpGet.releaseConnection();
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}

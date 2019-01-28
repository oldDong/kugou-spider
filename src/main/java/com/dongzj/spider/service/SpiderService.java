package com.dongzj.spider.service;

import com.alibaba.fastjson.JSONObject;
import com.dongzj.spider.enums.TopListEnum;
import com.dongzj.spider.util.FileDownLoadUtil;
import com.dongzj.spider.util.HtmlUtil;
import com.dongzj.spider.util.HttpConnectUtil;
import com.dongzj.spider.vo.ResultVo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 10:59
 */
@Service
public class SpiderService {

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    private static String mp3 = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&callback=jQuery191027067069941080546_1546235744250&"
            + "hash=HASH&album_id=0&_=TIME";

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            10, 10, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(500),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 下载文件
     *
     * @param code
     * @param savePath
     * @return
     * @throws IOException
     */
    public ResultVo download(Integer code, String savePath) throws IOException {
        logger.info("准备下载 - code:{}, savePath:{}", code, savePath);
        TopListEnum topListEnum = TopListEnum.getRocketByCode(code);
        if (null == topListEnum) {
            return ResultVo.fail("code error, it seems the code does't exist");
        }
        String topUrl = topListEnum.getUrl();
        Integer length = topListEnum.getLength();

        for (int i = 1; i <= length; i++) {
            String url = topUrl.replace("PAGE", i + "");
            getTitle(url, savePath);
        }
        logger.info("==========下载结束==========");
        return ResultVo.ok(null);
    }

    private void getTitle(String url, String savePath) throws IOException {
        String content = HttpConnectUtil.connect(url, "UTF-8");
        Document doc = HtmlUtil.manage(content);
        Element element = doc.getElementsByClass("pc_temp_songlist").get(0);
        Elements elements = element.getElementsByTag("li");
        for (int i = 0; i < elements.size(); i++) {
            Element item = elements.get(i);
            String title = item.attr("title").trim();
            String link = item.getElementsByTag("a").first().attr("href");
            DownloadJob job = new DownloadJob(link, title, savePath);
            pool.execute(job);
        }
    }

    private static class DownloadJob implements Runnable {

        /**
         * 文件路径
         */
        private String url;

        /**
         * 文件名
         */
        private String name;

        /**
         * 文件存储路径
         */
        private String path;

        public DownloadJob(String url, String name, String path) {
            this.url = url;
            this.name = name;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                String hash = "";
                String content = HttpConnectUtil.connect(url, "UTF-8");
                String regEx = "\"hash\":\"[0-9A-Z]+\"";
                //编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    hash = matcher.group();
                    hash = hash.replace("\"hash\":\"", "");
                    hash = hash.replace("\"", "");
                }
                String item = mp3.replace("HASH", hash);
                item = item.replace("TIME", System.currentTimeMillis() + "");
                logger.info("item:{}", item);
                String mp = HttpConnectUtil.connect(item, "UTF-8");

                mp = mp.substring(mp.indexOf("(") + 1, mp.length() - 3);

                JSONObject json = JSONObject.parseObject(mp);
                String playUrl = json.getJSONObject("data").getString("play_url");

                logger.info("paly_url:{}", playUrl);
                FileDownLoadUtil.download(playUrl, path + name + ".mp3");

                logger.info(name + " =====>下载完成");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("thread:{} download error", Thread.currentThread().getName());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SpiderService spiderService = new SpiderService();
        spiderService.download(2, "/Users/dongzj/Music/kugou/");
    }

}

package com.dongzj.spider.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 10:32
 */
public class HtmlUtil {

    public static Document manage(String html) {
        Document doc = Jsoup.parse(html);
        return doc;
    }

    public static Document manageDirect(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc;
    }

    public static List<String> manageHtmlTag(Document doc, String tag) {
        List<String> list = new ArrayList<>();

        Elements elements = doc.getElementsByTag(tag);
        elements.stream().forEach(element -> list.add(element.html()));
        return list;
    }

    public static List<String> manageHtmlClass(Document doc, String clas) {
        List<String> list = new ArrayList<>();

        Elements elements = doc.getElementsByClass(clas);
        elements.stream().forEach(element -> list.add(element.html()));
        return list;
    }

    public static List<String> manageHtmlKey(Document doc, String key, String value) {
        List<String> list = new ArrayList<>();

        Elements elements = doc.getElementsByAttributeValue(key, value);
        elements.stream().forEach(element -> list.add(element.html()));
        return list;
    }
}

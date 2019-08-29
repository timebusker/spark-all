package com.timebusker.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @DESC:Application
 * @author:timebusker
 * @date:2019/8/29
 */
public class Application {

    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            Document doc;
            try {
                doc = Jsoup.connect("https://www.imooc.com/course/programdetail/pid/" + i).get();
            } catch (IOException e) {
                continue;
            }
            String title = doc.title();
            System.out.println("https://www.imooc.com/course/programdetail/pid/" + i + "：" + title);
        }
        System.out.println("\n\n\n");
        for (int i = 0; i < 200; i++) {
            Document doc;
            try {
                doc = Jsoup.connect("https://coding.imooc.com/class/" + i + ".html").get();
            } catch (IOException e) {
                continue;
            }
            String title = doc.title();
            System.out.println("https://coding.imooc.com/class/" + i + ".html" + "：" + title);
        }
    }
}

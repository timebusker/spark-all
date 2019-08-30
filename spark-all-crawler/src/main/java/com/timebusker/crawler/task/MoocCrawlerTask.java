package com.timebusker.crawler.task;

import com.timebusker.crawler.conf.ZookeeperConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @DESC:IMoocCrawlerTask
 * @author:timebusker
 * @date:2019/8/30
 */
//@Component
public class MoocCrawlerTask {

    private static final Logger logger = LoggerFactory.getLogger(MoocCrawlerTask.class);

    @Autowired
    private ZookeeperConfig zookeeper;

    @Scheduled(fixedRate = 60000)
    public void execute() {
        int i = 0;
        do {
            i++;
            getEmploymentClasses();
            getPracticalCourses();
            getFreeCourses();
        } while (i < 10);
    }

    @Async
    public void getEmploymentClasses() {
        try {
            int i = zookeeper.getNodeData(zookeeper.EMPLOYMENT_CLASSES);
            Document doc = Jsoup.connect("https://class.imooc.com/sc/" + i).get();
            String title = doc.title();
            if (title.equals("慕课网")) {
                return;
            }
            logger.info("https://class.imooc.com/sc/" + i + "\t" + title);
        } catch (IOException e) {
        }
    }

    @Async
    public void getPracticalCourses() {
        try {
            int i = zookeeper.getNodeData(zookeeper.PRACTICAL_COURSES);
            Document doc = Jsoup.connect("https://coding.imooc.com/class/" + i + ".html").get();
            String title = doc.title();
            if (title.equals("慕课网")) {
                return;
            }
            logger.info("https://coding.imooc.com/class/" + i + ".html" + "\t" + title);
        } catch (IOException e) {
        }
    }

    @Async
    public void getFreeCourses() {
        try {
            int i = zookeeper.getNodeData(zookeeper.FREE_COURSES);
            Document doc = Jsoup.connect("https://www.imooc.com/learn/" + i).get();
            String title = doc.title().trim();
            if (title.equals("慕课网")) {
                return;
            }
            logger.info("https://www.imooc.com/learn/" + i + "\t" + title);
        } catch (IOException e) {
        }
    }

}

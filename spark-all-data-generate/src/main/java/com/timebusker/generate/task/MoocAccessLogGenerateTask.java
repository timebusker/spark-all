package com.timebusker.generate.task;

import com.timebusker.generate.utils.FileUtil;
import com.timebusker.generate.utils.KafkaPushMessageUtil;
import com.timebusker.generate.vo.WorkDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @DESC:MoocAccessLogGenerateTask
 * @author:timebusker
 * @date:2019/8/30
 */
//@Component
public class MoocAccessLogGenerateTask extends AbstractBaseTask {

    private static AtomicLong count = new AtomicLong(1l);

    @Autowired
    private KafkaPushMessageUtil kafkaUtil;

    @PostConstruct
    public void init() throws Exception {
        vo = new WorkDataVo(zookeeperUtil.getServer(), zookeeperUtil.getROOT());
        vo.setZkWorkPath(config.getImoocLogZK());
        vo.setEncode(FileUtil.ENCODE_UTF8);
        // 创建工作目录
        zookeeperUtil.createWorkNode(vo);
        // 设置文件后缀
        vo.setSourcePath(config.getImoocLog());
    }

    @Scheduled(fixedRate = 30000)
    public void execute() throws Exception {
        logger.info("当前工作上下文信息是：" + vo.toString());
        // 读取文件内容
        String data = readLineFile(vo);
        // 处理文件内容
//        Thread.sleep(random.nextInt(SLEEP_TIME));
//        long order = (count.get() / MAX_LINE_SIZE);
//        FileUtil.writeFile(new File(PATH + config.getImoocLogZK().toLowerCase() + FILE_NAME + order), data);
//        count.incrementAndGet();
        kafkaUtil.sendMessage("imooc-access", data);
    }
}
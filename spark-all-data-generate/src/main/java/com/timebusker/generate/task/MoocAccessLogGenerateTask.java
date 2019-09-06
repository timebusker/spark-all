package com.timebusker.generate.task;

import com.timebusker.generate.utils.FileUtil;
import com.timebusker.generate.vo.WorkDataVo;
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
@Component
public class MoocAccessLogGenerateTask extends AbstractBaseTask {

    private static AtomicLong count = new AtomicLong(1l);

    @PostConstruct
    public void init() throws Exception {
        vo = new WorkDataVo(zookeeperUtils.getServer(), zookeeperUtils.getROOT());
        vo.setZkWorkPath(config.getImoocLogZK());
        vo.setEncode(FileUtil.ENCODE_UTF8);
        // 创建工作目录
        zookeeperUtils.createWorkNode(vo);
        // 设置文件后缀
        vo.setSourcePath(config.getImoocLog());
    }

    @Scheduled(fixedRate = 1000)
    public void execute() throws Exception {
        logger.info("当前工作上下文信息是：" + vo.toString());
        // 读取文件内容
        String data = readLineFile(vo);
        // 处理文件内容
        Thread.sleep(random.nextInt(SLEEP_TIME));
        long order = (count.get() / MAX_LINE_SIZE);
        FileUtil.writeFile(new File(PATH + config.getImoocLogZK().toLowerCase() + FILE_NAME + order), data);
        count.incrementAndGet();
    }
}
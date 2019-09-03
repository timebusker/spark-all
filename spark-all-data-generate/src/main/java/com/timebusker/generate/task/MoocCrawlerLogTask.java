package com.timebusker.generate.task;

import com.timebusker.generate.utils.FileUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class MoocCrawlerLogTask extends AbstractBaseTask {

    @PostConstruct
    public void init() throws Exception {
        vo = zookeeperUtils.getVo();
        vo.setZkWorkPath(config.getImoocClassZK());
        // 创建工作目录
        zookeeperUtils.createWorkNode(vo);
    }

    @Scheduled(fixedRate = 10)
    public void execute() throws Exception {
        // 更新日志文件目录信息
        updateFilesState(vo);
        zookeeperUtils.sampleChildrenNode(vo);

        // 执行读取日志文件
        String data = FileUtil.readLineFile(new File(vo.getFilePath()), vo.getLastIndex());
        if (data == null) {
            logger.info(vo.getFilePath() + "文件已经被全部读取处理！");
            zookeeperUtils.deleteNode(vo);
            FileUtil.renameFile(new File(vo.getFilePath()));
            return;
        }
        // 再次更新日志文件
        vo.setZkNodeData(vo.getFilePath() + vo.SEPARATOR + (vo.getLastIndex() + 1l));
        zookeeperUtils.setNodeData(vo);
        System.err.println(data);
        FileUtil.writeFile(new File(vo.getFilePath() + "_write"), data);
    }
}

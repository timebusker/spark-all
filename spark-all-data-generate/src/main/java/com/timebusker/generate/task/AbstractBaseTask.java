package com.timebusker.generate.task;

import com.timebusker.generate.conf.TaskWorkPathConfig;
import com.timebusker.generate.utils.FileUtil;
import com.timebusker.generate.utils.ZookeeperUtil;
import com.timebusker.generate.vo.WorkDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Random;

@Component
public abstract class AbstractBaseTask {

    protected String suffix = "log";

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final long MAX_LINE_SIZE = 10;

    @Autowired
    protected ZookeeperUtil zookeeperUtil;

    protected static final String FILE_NAME = "/generate_log_";

    @Autowired
    protected TaskWorkPathConfig config;

    protected WorkDataVo vo;

    protected static final Random random = new Random();

    protected static final int SLEEP_TIME = 1000;

    protected static final String PATH = "/var/log/generate/";

    protected void updateFilesState(WorkDataVo vo) throws Exception {
        List<File> list = FileUtil.listFiles(vo.getSourcePath(), suffix);
        for (File file : list) {
            vo.setZkNodePath(file.getName());
            vo.setZkNodeData(file.getAbsolutePath() + vo.SEPARATOR + 0l);
            zookeeperUtil.createNode(vo);
        }
    }

    protected void updateZkState(WorkDataVo vo) throws Exception {
        vo.setZkNodeData(vo.getFilePath() + vo.SEPARATOR + (vo.getLastIndex() + 1l));
        zookeeperUtil.setNodeData(vo);
    }

    protected synchronized String readLineFile(WorkDataVo vo) throws Exception {
        // 更新日志文件目录信息
        updateFilesState(vo);
        // 随机抽样文件
        zookeeperUtil.sampleChildrenNode(vo);
        String data = FileUtil.readLineFile(new File(vo.getFilePath()), vo.getLastIndex(), vo.getEncode());
        if (data == null) {
            logger.info(vo.getFilePath() + "文件已经被全部读取处理！");
            zookeeperUtil.deleteNode(vo);
            FileUtil.renameFile(new File(vo.getFilePath()));
            throw new Exception(vo.getFilePath() + "文件已经被全部读取处理！");
        }
        // 再次更新日志文件
        updateZkState(vo);
        System.err.println(vo.getZkNodeData() + "\t\t\t" + data);
        return data;
    }
}

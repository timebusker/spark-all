package com.timebusker.generate.task;

import com.timebusker.generate.conf.TaskWorkPathConfig;
import com.timebusker.generate.utils.FileUtil;
import com.timebusker.generate.utils.ZookeeperUtils;
import com.timebusker.generate.vo.WorkDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public abstract class AbstractBaseTask {

    protected static final String suffix = "log";
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ZookeeperUtils zookeeperUtils;

    @Autowired
    protected TaskWorkPathConfig config;

    protected WorkDataVo vo;

    protected void updateFilesState(WorkDataVo vo) throws Exception {
        List<File> list = FileUtil.listFiles(config.getImoocClass(), suffix);
        for (File file : list) {
            vo.setZkNodePath(file.getName());
            vo.setZkNodeData(file.getAbsolutePath() + vo.SEPARATOR + 0l);
            zookeeperUtils.createNode(vo);
        }
    }
}

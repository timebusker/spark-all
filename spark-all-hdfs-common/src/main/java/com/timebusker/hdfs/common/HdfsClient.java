package com.timebusker.hdfs.common;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @DESC:HdfsClient：HDFS客户端封装
 * @author:timebusker
 * @date:2019/9/2
 */
@Component
public class HdfsClient {

    FileSystem fs = null;

    @PostConstruct
    private void init() throws Exception {
        Path core = new Path("");
        Path hdfs = new Path("");
        Path mappred = new Path("");
        Path yarn = new Path("");
        Configuration conf = new Configuration();
        conf.addResource(core);
        conf.addResource(hdfs);
        conf.addResource(mappred);
        conf.addResource(yarn);

        if (fs == null) {
            fs = FileSystem.get(conf);
        }
    }

    public String getFsURL() {
        return fs.getConf().get("fs.defaultFS");
    }

    public String getFsProperties(String key) {
        return fs.getConf().get(key);
    }

    public FsPermission getFsPermission() {
        return FsPermission.getDirDefault();
    }
}

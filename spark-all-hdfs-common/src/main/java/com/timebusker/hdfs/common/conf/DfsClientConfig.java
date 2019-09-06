package com.timebusker.hdfs.common.conf;

import com.timebusker.hdfs.common.utils.DfsClientUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @DESC:HdfsClientConfig
 * @author:timebusker
 * @date:2019/9/3
 */
@Configuration
@ConfigurationProperties(prefix = "hadoop.client")
public class DfsClientConfig {

    private String core;
    private String yarn;
    private String dfs;
    private String mapred;

    @Bean
    public DfsClientUtil dfsClient() {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.addResource(this.core);
        conf.addResource(this.dfs);
        conf.addResource(this.mapred);
        conf.addResource(this.yarn);
        DfsClientUtil client = new DfsClientUtil(conf);
        return client;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getYarn() {
        return yarn;
    }

    public void setYarn(String yarn) {
        this.yarn = yarn;
    }

    public String getDfs() {
        return dfs;
    }

    public void setDfs(String dfs) {
        this.dfs = dfs;
    }

    public String getMapred() {
        return mapred;
    }

    public void setMapred(String mapred) {
        this.mapred = mapred;
    }
}

package com.timebusker.generate.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @DESC:TaskWorkPathConfig
 * @author:timebusker
 * @date:2019/8/30
 */

@Configuration
@ConfigurationProperties(prefix = "task.work.path")
public class TaskWorkPathConfig {


}

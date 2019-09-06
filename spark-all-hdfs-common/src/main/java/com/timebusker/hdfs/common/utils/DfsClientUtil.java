package com.timebusker.hdfs.common.utils;

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

/**
 * @DESC:HdfsClient：HDFS客户端封装
 * @author:timebusker
 * @date:2019/9/2
 */
public class DfsClientUtil {

    private FileSystem dfs;
    private Configuration conf;

    public DfsClientUtil(Configuration conf) {
        try {
            this.conf = conf;
            if (dfs == null) {
                dfs = FileSystem.get(this.conf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("分布式文件系统初始化成功：" + this.dfs.getUri().getScheme() + ":" + this.dfs.getUri().getSchemeSpecificPart());
    }

    /**
     * 获取文件系统URL
     *
     * @return
     */
    public String getFsURL() {
        return dfs.getConf().get("fs.defaultFS");
    }

    /**
     * 获取文件系统配置信息
     *
     * @param key
     * @return
     */
    public String getFsProperties(String key) {
        return dfs.getConf().get(key);
    }

    /**
     * 获取文件系统权限西信息
     *
     * @return
     */
    public FsPermission getFsPermission() {
        return FsPermission.getDirDefault();
    }

    /**
     * 检验文件路径是否存在
     *
     * @param path
     * @return
     */
    public boolean exists(String path) {
        try {
            return dfs.exists(new Path(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建文件目录
     *
     * @param path
     * @return
     */
    public boolean mkdir(String path) {
        try {
            dfs.mkdirs(new Path(path));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建文件目录
     *
     * @param path
     * @param permission
     * @return
     */
    public boolean mkdir(String path, FsPermission permission) {
        try {
            dfs.mkdirs(new Path(path), permission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 上传文件
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean copyFromLocalFile(String src, String dst) {
        try {
            dfs.copyFromLocalFile(new Path(src), new Path(dst));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean copyToLocalFile(String src, String dst) {
        Preconditions.checkNotNull(src);
        Preconditions.checkNotNull(dst);
        try {
            dfs.copyToLocalFile(new Path(src), new Path(dst));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取指定路径下的文件信息
     *
     * @param path
     * @return
     */
    public long getLength(String path) {
        Preconditions.checkNotNull(path);
        long length = 0L;
        try {
            length = dfs.getContentSummary(new Path(path)).getLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 文件移动、重命名
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean move(String src, String dst) {
        Preconditions.checkNotNull(src);
        Preconditions.checkNotNull(dst);
        try {
            dfs.rename(new Path(src), new Path(dst));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean delete(String path) {
        try {
            dfs.deleteOnExit(new Path(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public FSDataOutputStream create(String path) {
        try {
            FSDataOutputStream stream = dfs.create(new Path(path));
            return stream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

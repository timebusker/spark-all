package com.timebusker.generate.vo;

import java.io.File;

public class WorkDataVo {

    private String server;
    private String zkRoot;
    private String zkWorkPath;
    private String zkNodePath;
    private Object zkNodeData;
    private String filePath;
    private String fileName;
    private long lastIndex;
    private String sourcePath;
    private String encode;
    public static final String SEPARATOR = "@@@";

    public WorkDataVo(String server, String zkRoot) {
        this.zkRoot = zkRoot;
        this.server = server;
    }

    public String getZkRoot() {
        return zkRoot;
    }

    public String getZkWorkPath() {
        return zkWorkPath;
    }

    public void setZkWorkPath(String zkWorkPath) {
        if (!zkWorkPath.startsWith(zkRoot)) {
            zkWorkPath = zkRoot + zkWorkPath;
        }
        this.zkWorkPath = zkWorkPath;
    }

    public String getZkNodePath() {
        return zkNodePath;
    }

    public void setZkNodePath(String zkNodePath) {
        if (!zkNodePath.startsWith(zkWorkPath)) {
            zkNodePath = zkWorkPath + "/" + zkNodePath;
        }
        this.zkNodePath = zkNodePath;
    }

    public Object getZkNodeData() {
        return zkNodeData;
    }

    public void setZkNodeData(Object zkNodeData) {
        String data = (String) zkNodeData;
        this.filePath = data.split(SEPARATOR)[0];
        this.lastIndex = Long.parseLong(data.split(SEPARATOR)[1]);
        this.zkNodeData = zkNodeData;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getLastIndex() {
        return lastIndex;
    }

    public String getServer() {
        return server;
    }

    public String getFileName() {
        return new File(this.filePath).getName();
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    @Override
    public String toString() {
        return "WorkDataVo{" +
                "server='" + server + '\'' +
                ", zkRoot='" + zkRoot + '\'' +
                ", zkWorkPath='" + zkWorkPath + '\'' +
                ", zkNodePath='" + zkNodePath + '\'' +
                ", zkNodeData=" + zkNodeData +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", lastIndex=" + lastIndex +
                ", sourcePath='" + sourcePath + '\'' +
                ", encode='" + encode + '\'' +
                '}';
    }
}

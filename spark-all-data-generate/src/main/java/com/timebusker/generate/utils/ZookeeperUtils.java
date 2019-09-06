package com.timebusker.generate.utils;

import com.timebusker.generate.vo.WorkDataVo;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class ZookeeperUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);

    private ZkClient zkClient;

    private String server;

    private String ROOT;

    private static final Random random = new Random();

    public ZookeeperUtils(String servers, String rootPath) {
        server = servers;
        ROOT = rootPath;
        if (zkClient == null) {
            zkClient = new ZkClient(server);
        }
        // 需要先创建父节点
        if (!zkClient.exists(rootPath)) {
            zkClient.createPersistent(ROOT, 0);
        }
    }

    public Object getNodeData(WorkDataVo vo) throws Exception {
        validatorNode(vo.getZkNodePath());
        Object data = zkClient.readData(vo.getZkNodePath());
        return data;
    }

    public void setNodeData(WorkDataVo vo) throws Exception {
        validatorNode(vo.getZkNodePath());
        zkClient.writeData(vo.getZkNodePath(), vo.getZkNodeData());
    }

    private void validatorNode(String path) throws Exception {
        if (!zkClient.exists(path)) {
            throw new Exception("指定的zookeeper数据节点不存在！");
        }
    }

    public void createWorkNode(WorkDataVo vo) throws Exception {
        if (!zkClient.exists(vo.getZkWorkPath())) {
            zkClient.createPersistent(vo.getZkWorkPath(), vo.getZkNodeData());
        }
    }

    public void createNode(WorkDataVo vo) throws Exception {
        if (!zkClient.exists(vo.getZkNodePath())) {
            zkClient.createPersistent(vo.getZkNodePath(), vo.getZkNodeData());
        }
    }

    public void sampleChildrenNode(WorkDataVo vo) {
        List<String> list = zkClient.getChildren(vo.getZkWorkPath());
        if (list.size() == 0)
            return;
        int rand = random.nextInt(list.size());
        String path = vo.getZkWorkPath() + "/" + list.get(rand);
        Object data = zkClient.readData(path);
        vo.setZkNodePath(path);
        vo.setZkNodeData(data);
    }

    public void deleteNode(WorkDataVo vo) throws Exception {
        validatorNode(vo.getZkNodePath());
        zkClient.delete(vo.getZkNodePath());
    }

    public String getROOT() {
        return ROOT;
    }

    public String getServer() {
        return server;
    }
}

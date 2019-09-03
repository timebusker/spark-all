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

    private String imoocLog;
    private String imoocLogZK;
    private String imoocClass;
    private String imoocClassZK;
    private String sougou;
    private String sougouZK;
    private String hotel;
    private String hotelZK;

    public String getImoocLog() {
        return imoocLog;
    }

    public void setImoocLog(String imoocLog) {
        this.imoocLog = imoocLog;
    }

    public String getImoocClass() {
        return imoocClass;
    }

    public void setImoocClass(String imoocClass) {
        this.imoocClass = imoocClass;
    }

    public String getSougou() {
        return sougou;
    }

    public void setSougou(String sougou) {
        this.sougou = sougou;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getImoocLogZK() {
        return imoocLogZK;
    }

    public void setImoocLogZK(String imoocLogZK) {
        this.imoocLogZK = imoocLogZK;
    }

    public String getImoocClassZK() {
        return imoocClassZK;
    }

    public void setImoocClassZK(String imoocClassZK) {
        this.imoocClassZK = imoocClassZK;
    }

    public String getSougouZK() {
        return sougouZK;
    }

    public void setSougouZK(String sougouZK) {
        this.sougouZK = sougouZK;
    }

    public String getHotelZK() {
        return hotelZK;
    }

    public void setHotelZK(String hotelZK) {
        this.hotelZK = hotelZK;
    }
}

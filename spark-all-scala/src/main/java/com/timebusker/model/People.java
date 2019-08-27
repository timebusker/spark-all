package com.timebusker.model;

/**
 * @DESC:People
 * @author:timebusker
 * @date:2019/7/23
 */
public class People implements Comparable<People> {

    private String name;
    private int faceValue;

    public People(String name, int faceValue) {
        this.name = name;
        this.faceValue = faceValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }

    @Override
    public int compareTo(People o) {
        return this.getFaceValue() - o.getFaceValue();
    }
}

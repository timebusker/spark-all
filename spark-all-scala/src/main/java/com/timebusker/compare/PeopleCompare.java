package com.timebusker.compare;

import com.timebusker.model.People;

import java.util.*;

/**
 * @DESC:PeopleCompare
 * @author:timebusker
 * @date:2019/7/23
 */
public class PeopleCompare {

    public static void main(String[] args) {
        People jay = new People("jay", 98);
        People jj = new People("jj", 90);
        People omg = new People("omg", 99);

        List<People> list = new ArrayList<>();
        list.add(jay);
        list.add(jj);
        list.add(omg);

        for (People p : list) {
            System.out.println(p.getName() + "\t" + p.getFaceValue());
        }

        Collections.sort(list, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                return o1.getFaceValue() - o2.getFaceValue();
            }
        });
        for (People p : list) {
            System.out.println(p.getName() + "\t" + p.getFaceValue());
        }


        Collections.sort(list);
        for (People p : list) {
            System.out.println(p.getName() + "\t" + p.getFaceValue());
        }
    }
}

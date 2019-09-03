package com.timebusker.generate;

import java.util.Random;

public class RandomTest {

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int rand = random.nextInt(2);
            System.out.println(rand);
        }
    }
}

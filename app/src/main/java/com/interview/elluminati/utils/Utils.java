package com.interview.elluminati.utils;

import java.util.HashMap;

public class Utils {

    public static float getSumOfMap(HashMap<String,Float> hashMap){
        float sum = 0.0f;
        for (float f : hashMap.values()) {
            sum += f;
        }

        return sum;
    }
}



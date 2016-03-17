package com.example.arpaul.messagapp.Utilities;

import android.text.TextUtils;

/**
 * Created by ARPaul on 17-03-2016.
 */
public class StringUtils {
    public static int getInt(String integer) {
        int reqInteger = 0;

        if(integer == null || TextUtils.isEmpty(integer))
            return reqInteger;

        reqInteger = Integer.parseInt(integer);

        return reqInteger;
    }
}

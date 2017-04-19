package com.example.laura.madgame2.utils;

import java.util.regex.Pattern;

/**
 * Created by Philipp on 19.04.17.
 */

public class NetworkUtils {

    public static boolean checkIp(String ip){
       Pattern ipPattern = Pattern.compile("(([0-9]{1,3}\\.){3})[0-9]{1,3}:[0-9]{1,5}$");
        return ipPattern.matcher(ip).matches();


    }
}

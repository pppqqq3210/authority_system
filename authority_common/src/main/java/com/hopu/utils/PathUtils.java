package com.hopu.utils;

public class PathUtils {
    public static final String getWebAppPath(String path){
        return getClassPath("").replaceFirst("WEB-INF/classes/","") + path;
    }

    public static final String getClassPath(String path){
        return PathUtils.class.getResource("/").getPath().replaceFirst("/","") + path;
    }

}

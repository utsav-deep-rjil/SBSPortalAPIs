package com.jcs.sbs.common;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static Map<String, String> cachedData = new HashMap<>();
    private static Map<String, Long> lastUpdated = new HashMap<>();

    private static long maxAge = 120000;
    private static int maxCacheSize = 100000;

    public static long getMaxAge() {
        return maxAge;
    }

    public static void setMaxAge(long maxAge) {
        Cache.maxAge = maxAge;
    }

    public static int getMaxCacheSize() {
        return maxCacheSize;
    }

    public static void setMaxCacheSize(int maxCacheSize) {
        Cache.maxCacheSize = maxCacheSize;
    }

    public static void insert(String queryParams, String response) {
        if (cachedData.size() > maxCacheSize) {
            cachedData = new HashMap<>();
            lastUpdated = new HashMap<>();
        }
        cachedData.put(queryParams, response);
        lastUpdated.put(queryParams, System.currentTimeMillis());
    }

    public static String getData(String queryParams) {
        Long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdated.getOrDefault(queryParams, 0L) < maxAge) {
            return cachedData.get(queryParams);
        } else {
            return null;
        }
    }

}

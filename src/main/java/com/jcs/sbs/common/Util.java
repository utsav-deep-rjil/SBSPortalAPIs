package com.jcs.sbs.common;

import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;

import com.jcs.sbs.service.CommonService;
import com.jcs.sbs.service.impl.AccountServiceImpl;
import com.jcs.sbs.service.impl.SnapshotServiceImpl;
import com.jcs.sbs.service.impl.VolumeServiceImpl;

public class Util {

    public static CommonService getService(String queryType) {
        if(queryType==null)
            return null;
        switch (queryType) {
        case "accountSummary":
            return new AccountServiceImpl();
        case "volume":
            return new VolumeServiceImpl();
        case "snapshot":
            return new SnapshotServiceImpl();
        default:
            return null;
        }
    }
    
    public static Map<String, String> getTableHeaders(String queryType){
        try {
            Ini ini = new Ini(Util.class.getClassLoader().getResourceAsStream("tableHeaders.ini"));
            return ini.get(queryType);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String,String>();
        } 
    }
    
}

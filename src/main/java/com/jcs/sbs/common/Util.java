package com.jcs.sbs.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.ini4j.Ini;

import com.jcs.sbs.models.Snapshot;
import com.jcs.sbs.models.Volume;
import com.jcs.sbs.result.AccountSummaryExtended;
import com.jcs.sbs.service.CommonService;
import com.jcs.sbs.service.impl.AccountServiceImpl;
import com.jcs.sbs.service.impl.SnapshotServiceImpl;
import com.jcs.sbs.service.impl.VolumeServiceImpl;

public class Util {

    public static CommonService getService(String queryType) {
        if (queryType == null)
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

    public static Map<String, String> getTableHeaders(String queryType) {
        try {
            Ini ini = new Ini(Util.class.getClassLoader().getResourceAsStream("tableHeaders.ini"));
            return ini.get(queryType);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, String>();
        }
    }

    public static boolean isFieldBoolean(String queryType, String filter) {
        try {
            Class theClass = null;
            switch (queryType) {
            case "accountSummary":
                theClass = AccountSummaryExtended.class;
                break;
            case "volume":
                theClass = Volume.class;
                break;
            case "snapshot":
                theClass = Snapshot.class;
                break;
            default:
                return false;
            }
            Field field = theClass.getDeclaredField(filter);
            return field.getType().toString().equals("boolean");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;

    }

}

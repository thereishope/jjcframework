package com.development.separtion.dataSourceHolder;
import com.development.separtion.enums.DataBase;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiajunchen
 * @title DevDataSource
 * @project rwSeparation
 */
public class DevDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataBase> contextHolder = new ThreadLocal<DataBase>();

    private static Map<Object, Object> readMaps = new ConcurrentHashMap<>();

    public static void setMaster() {
        contextHolder.set(DataBase.MASTER);
    }

    public static void setSlave1() {
        contextHolder.set(DataBase.SLAVE1);
    }

    public static void setSlave2() {
        contextHolder.set(DataBase.SLAVE2);
    }

    public static DataBase getDataBase() {
        return contextHolder.get();
    }

    public static void setDataBase(DataBase dataBase) {
        contextHolder.set(dataBase);
    }

    public static void set(Map<Object, Object> dataSourceMap) {
        readMaps.putAll(dataSourceMap);
        if (readMaps.containsKey(DataBase.MASTER)) {
            readMaps.remove(DataBase.MASTER);
        }
    }

    public static void setSlaveRondom() {
        if (null != readMaps && readMaps.size() > 0) {
            DataBase base = (DataBase) new ArrayList(
                    readMaps.keySet()).get(new Random().nextInt(readMaps.size()));
            contextHolder.set(base);
        }else{
            contextHolder.set(DataBase.MASTER);
        }
    }

    public static DataBase getSlaveRondom(){
        if (null != readMaps && readMaps.size() > 0) {
            DataBase base = (DataBase) new ArrayList(
                    readMaps.keySet()).get(new Random().nextInt(readMaps.size()));
            contextHolder.set(base);
        }else{
            contextHolder.set(DataBase.MASTER);
        }
        return contextHolder.get();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }


}



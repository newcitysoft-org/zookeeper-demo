package com.newcitysoft.study.zookeeper.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 11:14
 */
public class PropContainer {
    private static Map<String, Prop> propMap = new HashMap<>();

    static {
        propMap.put("host.properties", new Prop("host.properties"));
    }

    public static Prop getProp(String confName){
        return propMap.get(confName);
    }

    public static Prop getHostProp(){
        return getProp("host.properties");
    }
}

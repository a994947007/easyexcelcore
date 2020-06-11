package org.easyexcel.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyao
 * 配置信息类，用于保存easy excel配置信息、属性
 */
public class ApplicationConfig {
    private Map<String, List<String>> config = new HashMap<String,List<String>>();
    private static volatile ApplicationConfig instance = null;
    static {
        if(instance == null){
            synchronized (ApplicationConfig.class){
                if(instance == null){
                    instance = new ApplicationConfig();
                }
            }
        }
    }

    public static ApplicationConfig getInstance(){
        return instance;
    }

    public void addConfig(String name,String value){
        List<String> configValues = config.get(name);
        if(configValues == null){
            configValues = new ArrayList<String>();
        }
        if(!configValues.contains(configValues)){
            configValues.add(value);
        }
        config.put(name,configValues);
    }

    public List<String> getConfig(String name){
        return config.get(name);
    }
}

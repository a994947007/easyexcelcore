package org.easyexcel.core;

import java.util.List;

public class EasyExcel extends AbstractEasyExcel{
    public EasyExcel(String url ) {
        super(url);
        List<String> list = ApplicationConfig.getInstance().getConfig("properties_property_autoLoad");
        String autoLoadConfig = list.get(0);
        if(autoLoadConfig != null && "true".equals(autoLoadConfig)){          //开启了自动装载配置
            load();
        }
    }

    /**
     * 在Excel未开启自动装载时，该方法可手动装载一个类
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> get(Class<?> clazz){
        List<String> list = ApplicationConfig.getInstance().getConfig("properties_property_useCache");
        String useCacheConfig = list.get(0);
        List<Object> results = null;
        if(useCacheConfig == null || "true".equals(useCacheConfig)){        //默认从缓存中读取
            results =  entitryContainer.get(clazz.getCanonicalName());
        }
        if(results == null){
            results =  excelParseByClass(clazz);
        }
        return (List<T>) results;
    }

    

    public void add(){

    }

    public void remove(){

    }

    public void update(){

    }
}

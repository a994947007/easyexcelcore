package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.easyexcel.annotation.FieldName;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

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
     * 从缓存中获取对应的对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getFromCache(Class<?> clazz){
        List<String> list = ApplicationConfig.getInstance().getConfig("properties_property_useCache");
        String useCacheConfig = list.get(0);
        List<Object> results = null;
        if(useCacheConfig == null || "true".equals(useCacheConfig)){        //默认从缓存中读取
            results =  entitryContainer.get(clazz.getCanonicalName());
        }
        return (List<T>) results;
    }


    /**
     * 根据类装载对应的java对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> get(Class<?> clazz){
        List<Object> results = getFromCache(clazz);
        if(results == null){
            results =  excelParseByClass(clazz,new DefaultlExcelParser());
        }
        return (List<T>) results;
    }

    /**
     * 根据类装载对应的java对象，并提供自定义解析器
     * @param clazz
     * @param excelParser
     * @param <T>
     * @return
     */
    public <T> List<T> get(Class<?> clazz,ExcelParser excelParser) {
        List<Object> results = getFromCache(clazz);
        if(results == null){
            results =  excelParseByClass(clazz,excelParser);
        }
        return (List<T>) results;
    }


    /**
     * 添加到excel
     * @param o
     */
    public void add(Object o){
        System.out.println(o.getClass());
    }

    public void add(Object o,ExcelParser excelParser){

    }

    /**
     * 添加到缓存
     * @param o
     */
    public void addToCache(Object o){

    }

    /**
     * 从excel中删除
     */
    public void remove(Object o){

    }

    /**
     * 从缓存中删除
     */
    public void removeFromCache(Object o){

    }

    public void update(){

    }


}

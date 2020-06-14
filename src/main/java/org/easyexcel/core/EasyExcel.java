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
    private <T> List<T> getFromCache(Class<?> clazz){
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
       return get(clazz,new DefaultlExcelParser());
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
    public void add(List<Object> list){
        add(list,new DefaultlExcelParser());
    }

    /**
     * 添加单行
     * @param o
     */
    public void add(Object o){
        List<Object> list = new ArrayList<Object>();
        list.add(o);
        add(list);
    }

    public void add(List<Object> list,ExcelParser excelParser){
        //写入到excel
        addToExcel(list,excelParser);
        //写入缓存
        addToCache(list);
    }

    public void add(Object o,ExcelParser excelParser){
        List<Object> list = new ArrayList<Object>();
        list.add(o);
        add(list,excelParser);
    }

    /**
     * 添加到缓存
     * @param o
     */
    public void addToCache(List<Object> list){
        List<String> configList = ApplicationConfig.getInstance().getConfig("properties_property_useCache");
        String useCacheConfig = configList.get(0);
        if(useCacheConfig == null || "true".equals(useCacheConfig)){        //默认从缓存中读取
            if(list!= null && list.size() > 0){
                Class<?> clazz = list.get(0).getClass();
                if(entitryContainer.containsKey(clazz.getCanonicalName())){
                    entitryContainer.get(clazz.getCanonicalName()).addAll(list);
                }else{
                    entitryContainer.put(clazz.getCanonicalName(),list);
                }
            }
        }
    }

    /**
     * 从excel中删除
     */
    public void remove(Object o){
        List<Object> list = new ArrayList<Object>();
        list.add(o);
        remove(list);
    }

    public void remove(List<Object> list){
        remove(list,new DefaultlExcelParser());
    }

    public void remove(Object o,ExcelParser parser){
        List<Object> list = new ArrayList<Object>();
        list.add(o);
        remove(list,parser);
    }

    public void remove(List<Object> list,ExcelParser parser){

    }

    /**
     * 从缓存中删除
     */
    public void removeFromCache(Object o){

    }

    /**
     * 在excel中更改，src改成dst，可匹配多行
     * @param  src 原excel中的内容
     * @param  dst 被改之后的内容
     */
    public void update(Object dst,Object src){

    }

    /**
     * 清空excel
     */
    public void clear(){

    }
}

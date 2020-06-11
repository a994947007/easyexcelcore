package org.easyexcel.core;

import org.easyexcel.io.XMLScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luyao
 * Demo版本，用于读写Excel
 *
 * @see org.easyexcel.annotation.Entity
 * @see org.easyexcel.annotation.FieldName
 */
public abstract class AbstractEasyExcel {
    /**
     * 存放Excel行数据对象
     */
    protected List<Class<?>> classes = new ArrayList<Class<?>>();

    /**
     * 对象容器，存储格式：类名：对象列表
     */
    protected Map<String,List<Object>> entitryContainer = new HashMap<String, List<Object>>();

    /**
     * 用于扫描配置信息
     */
    private XMLScanner scanner = null;

    public AbstractEasyExcel(String url){
        scanner = new XMLScanner(AbstractEasyExcel.class.getClassLoader().getResource(url).getPath());
    }

    /**
     * 初始化Container
     */
    protected void initContainer(){

    }

    /**
     * 扫描包，加载所有类
     */
    protected void scanPackages(){

    }

    /**
     * 扫描所有类，将加了注解的类构造对象保存到容器中
     */
    protected void instance(){

    }

    /**
     * 进行属性注入
     */
    protected void di(){

    }
}

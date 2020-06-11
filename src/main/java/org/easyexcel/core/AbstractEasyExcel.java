package org.easyexcel.core;

import org.easyexcel.io.XMLScanner;

import java.util.*;

import java.io.File;

/**
 * @author luyao
 * Demo版本，用于读写Excel
 *
 * @see org.easyexcel.annotation.Entity
 * @see org.easyexcel.annotation.FieldName
 */
public abstract class AbstractEasyExcel {
    /**
     * 存放Excel类数据对象
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
        scanPackages();
        instance();
        di();
    }

    /**
     * 扫描包，加载所有类
     */
    protected void scanPackages(){
        ApplicationConfig config = ApplicationConfig.getInstance();
        List<String> packages = config.getConfig("properties_property_scanPackage");
        for (String pkg : packages) {
            scanPackage(pkg);
        }
    }

    /**
     * 扫描一个包
     * @param packageName
     */
    private void scanPackage(String packagePath){
        String path = packagePath.replaceAll("\\.", "/");       //将所有.换成/
        File f = new File(AbstractEasyExcel.class.getClassLoader().getResource(path).getFile());
        Stack<File> fileStack = new Stack<File>();
        fileStack.push(f);
        while (!fileStack.isEmpty()){
            File file = fileStack.pop();
            if(file.isDirectory()){
                fileStack.push(file);
            }else if(file.isFile() && file.getName().endsWith(".class")){
                String className = file.getName().substring(0,file.getName().lastIndexOf("."));
                try {
                    Class<?> clazz = Class.forName(className.replaceAll("/","\\.") + "." + className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public abstract int add();
    public abstract int remove();
    public abstract int update();
}

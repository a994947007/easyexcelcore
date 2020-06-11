package org.easyexcel.core;

import java.util.List;

public class EasyExcel extends AbstractEasyExcel{
    public EasyExcel(String url) {
        super(url);
    }

    public <T> List<T> get(Class<?> clazz){
        return (List<T>) entitryContainer.get(clazz.getCanonicalName());
    }

    public void add(){

    }

    public void remove(){

    }

    public void update(){

    }

}

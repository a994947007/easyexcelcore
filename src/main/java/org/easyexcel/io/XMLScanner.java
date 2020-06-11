package org.easyexcel.io;

/**
 * @author luyao
 * 配置读写类，用于读写Excel数据配置，如excel文件路径，对象属性加载格式等
 */
public class XMLScanner {
    private String sourcePath = null;
    public XMLScanner(String path){
        this.sourcePath = path;
    }
}

package org.easyexcel.io;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.easyexcel.core.ApplicationConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author luyao
 * 配置读写类，用于读写Excel数据配置，如excel文件路径，对象属性加载格式等
 */
public class XMLScanner {
    private File applicationXML;
    public XMLScanner(String path){
        applicationXML = new File(path);
        init();
    }

    /**
     * 加载配置
     */
    private void init() {
        scan();
    }

    /**
     * 加载所有标签对
     */
    private void scan() {
        SAXReader reader = new SAXReader();
        try{
            Document document = reader.read(applicationXML);
            Element root = document.getRootElement();
            for(Iterator<Element> it = root.elementIterator();it.hasNext();){
                Element element = it.next();
                AbstractElementNode propertiesElementNode = new PropertiesElementNode();
                AbstractElementNode resourcesElementNode = new ResourcesElementNode();
                propertiesElementNode.setNext(resourcesElementNode);
                propertiesElementNode.execute(element);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }

    /**
     * xml结点处理器
     */
    private abstract static class AbstractElementNode{
        protected AbstractElementNode next;
        public void execute(Element element){
            boolean flag = handler(element);
            if(!flag && next != null){
                next.handler(element);
            }
        }

        /**
         * 返回一个执行状态，如果判定条件执行成功了，无需继续执行，返回true，否是返回false
         * @param element
         * @return
         */
        public abstract boolean handler(Element  element);
        public void setNext(AbstractElementNode next){
            this.next = next;
        }
    }

    /**
     * 加载properties标签
     */
    private static class PropertiesElementNode extends AbstractElementNode {
        @Override
        public boolean handler(Element element) {
            if("properties".equals(element.getName())){
                for(Iterator<Element> it = element.elementIterator();it.hasNext();){
                    Element ele = it.next();
                    if("property".equals(ele.getName())){
                        Iterator<Attribute>  attributes = ele.attributeIterator();
                        String name = attributes.next().getValue();
                        String value = attributes.next().getValue();
                        ApplicationConfig.getInstance().addConfig("properties_property_" + name,value);
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 加载resources标签
     */
    private static class ResourcesElementNode extends AbstractElementNode {
        @Override
        public boolean handler(Element element) {
            if ("resources".equals(element.getName())) {
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        try {
            File file = null;
            try {
                file = new File(XMLScanner.class.getClassLoader().getResource("easyexcel.xml").toURI().getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            System.out.println(file.getAbsolutePath());
            try {
                reader.read(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
           // reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

package test;

import org.easyexcel.core.EasyExcel;

import java.util.List;

/**
 * 增加根据col_index解析excel
 * 完善ExcelWriter，ExcelParser
 * 参考：https://www.jb51.net/article/157349.htm
 * https://my.oschina.net/u/3897028/blog/2049488
 * https://www.jianshu.com/p/4354bab7b792
 */
public class Main {
    public static void main(String[] args) {
        EasyExcel easyExcel = new EasyExcel("easyexcel.xml");
        Student student = new Student();
        student.setId(3);
        student.setName("李四");
        student.setAge(20);
        easyExcel.add(student);
    }
}

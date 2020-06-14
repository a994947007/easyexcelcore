package test;

import org.easyexcel.core.EasyExcel;

import java.util.List;

/**
 * 需完成以下内容
 * 1、增加根据col_index解析excel
 * 2、每个Entity无需一定要指定path
 * 完善ExcelWriter，ExcelParser
 * 参考：https://www.jb51.net/article/157349.htm
 *
 *
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

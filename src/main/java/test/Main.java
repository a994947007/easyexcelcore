package test;

import org.easyexcel.core.EasyExcel;

import java.util.List;

/**
 * 增加根据col_index解析excel
 * 完善ExcelWriter，ExcelParser
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

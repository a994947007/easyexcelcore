package test;

import org.easyexcel.core.EasyExcel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 需完成以下内容
 * 1、增加根据col_index解析excel
 * 2、每个Entity无需一定要指定path
 * 3、增加删除后，出现问题，后面再次添加的话，从删除后的最后一行开始添加的，而非根据原本excel中的行数加到末尾。
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


        for (int i = 0; i < 10; i++) {
            easyExcel.add(student);
        }

        easyExcel.remove(student);

        for (int i = 0; i < 10; i++) {
            easyExcel.add(student);
        }

    }
}

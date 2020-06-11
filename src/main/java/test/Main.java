package test;

import org.easyexcel.core.EasyExcel;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EasyExcel easyExcel = new EasyExcel("easyexcel.xml");
        List<TestData> datas = easyExcel.get(TestData.class);
        for (TestData data : datas) {
            System.out.println(data);
        }
    }
}

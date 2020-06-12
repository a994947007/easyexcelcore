package org.easyexcel.io;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ExcelOperator {
    protected File excelFile = null;
    private Workbook wb = null;
    public ExcelOperator(String url){
        excelFile = new File(url);
        init();
    }

    public void init(){

    }

    public void close(){
        try {
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.easyexcel.io;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public abstract class AbstractExcelOperator {
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    protected File excelFile = null;
    private Workbook wb = null;
    public AbstractExcelOperator(String url){
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

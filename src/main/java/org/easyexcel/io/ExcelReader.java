package org.easyexcel.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easyexcel.exception.UnsupportFileTypeException;

import java.io.FileInputStream;
import java.io.IOException;


public class ExcelReader extends ExcelOperator{
    public ExcelReader(String url) {
        super(url);
    }

    public Workbook read(){
        FileInputStream fs = null;
        Workbook wb = null;
        String path = excelFile.getPath();
        try {
            fs = new FileInputStream(excelFile.getAbsolutePath());
            if(path.endsWith(".xlsx")){
                wb = new XSSFWorkbook(fs);
            }else if(path.endsWith(".xls")){
                wb = new HSSFWorkbook(fs);
            }else{
                throw new UnsupportFileTypeException("不支持这种excel文件类型");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportFileTypeException e) {
            e.printStackTrace();
        }
        return wb;
    }
}

package org.easyexcel.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easyexcel.core.ExcelParser;
import org.easyexcel.exception.UnsupportFileTypeException;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter extends ExcelOperator{
    public ExcelWriter(String url) {
        super(url);
    }

    public void write(Object o, ExcelParser parser){
        FileOutputStream fs = null;
        Workbook wb = null;
        String path = excelFile.getPath();
        try {
            fs = new FileOutputStream(excelFile.getAbsolutePath());
            if(path.endsWith(".xlsx")){
                wb = new XSSFWorkbook();
            }else if(path.endsWith(".xls")){
                wb = new HSSFWorkbook();
            }else{
                throw new UnsupportFileTypeException("不支持这种excel文件类型");
            }
            wb.write(fs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportFileTypeException e) {
            e.printStackTrace();
        }finally {
            try {
                if(wb != null){
                    wb.close();
                    wb = null;
                }
                if(fs != null){
                    fs.close();
                    fs = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

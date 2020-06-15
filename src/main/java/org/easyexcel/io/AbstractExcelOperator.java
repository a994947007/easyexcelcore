package org.easyexcel.io;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easyexcel.exception.UnsupportFileTypeException;

import java.io.File;

/**
 * @author luyao
 */
public abstract class AbstractExcelOperator {
    protected static final String XLS = ".xls";
    protected static final String XLSX = ".xlsx";
    protected File excelFile = null;
    public AbstractExcelOperator(String url){
        excelFile = new File(url);
    }

    public boolean existsExcelFile(File file){
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public Workbook createWorkbook() throws UnsupportFileTypeException {
        String path = excelFile.getPath();
        Workbook wb;
        if(path.endsWith(".xlsx")){
            wb = new XSSFWorkbook();
        }else if(path.endsWith(".xls")){
            wb = new HSSFWorkbook();
        }else{
            throw  new UnsupportFileTypeException("不支持这种excel文件类型");
        }
        return wb;
    }
}

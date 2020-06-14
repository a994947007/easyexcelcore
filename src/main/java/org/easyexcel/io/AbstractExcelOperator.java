package org.easyexcel.io;


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
}

package org.easyexcel.io;


import java.io.File;


public abstract class AbstractExcelOperator {
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
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

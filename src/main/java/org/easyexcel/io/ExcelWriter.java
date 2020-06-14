package org.easyexcel.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easyexcel.core.ExcelParser;
import org.easyexcel.exception.UnsupportFileTypeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ExcelWriter extends AbstractExcelOperator {
    public ExcelWriter(String url) {
        super(url);
    }


    /**
     * 写出workbook，如果wb存在则追加，否则新建
     * @param workbook
     */
    public void write(Workbook workbook){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(excelFile);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(workbook != null){
                    workbook.close();
                    workbook = null;
                }
                if(fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //获取一个Workbook，如果存在则返回存在的wb，否则新建一个wb
    public Workbook getWorkbook(){
        Workbook wb = null;
        String path = excelFile.getPath();
        FileInputStream fis = null;
        try {
            if(excelFile.exists()){
                fis = new FileInputStream(excelFile);
                if(path.endsWith(".xlsx")){
                    wb = new XSSFWorkbook(fis);
                }else if(path.endsWith(".xls")){
                    wb = new HSSFWorkbook(fis);
                }else{
                    throw new UnsupportFileTypeException("不支持这种excel文件类型");
                }
            }else{      //不存则创建一个新的
                if(path.endsWith(".xlsx")){
                    wb = new XSSFWorkbook();
                }else if(path.endsWith(".xls")){
                    wb = new HSSFWorkbook();
                }else{
                    throw  new  UnsupportFileTypeException("不支持这种excel文件类型");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportFileTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //否则创建Workbook
        return wb;
    }
}

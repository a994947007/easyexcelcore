package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author luyao
 * 解析Excel格式，数据
 */
public interface ExcelParser {
    /**
     * 将Workbook解析成java对象
     * @param workbook
     * @param clazz
     * @return
     */
    List<Object> parse(Workbook workbook,Class<?> clazz);

    /**
     * 将java对象列表解析成Workbook，并加到原有Workbook内容最后
     * @param list
     */
    Workbook parse(List<Object> list,Workbook workbook);

    /**
     * 抹除掉List<Object>后剩下的内容
     * @param list
     * @param workbook
     * @return
     */
    Workbook maskParse(List<Object> list,Workbook workbook);
}

package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 解析Excel格式，数据
 */
public interface ExcelParser {
    /**
     * 解析类生成对象列表
     * @param workbook
     * @param clazz
     * @return
     */
    List<Object> parse(Workbook workbook,Class<?> clazz);

    /**
     * 解析添加的对象生成Row添加到Workbook最后
     * @param list
     */
    Workbook parse(List<Object> list,Workbook workbook);
}

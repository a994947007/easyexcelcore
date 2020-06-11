package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 解析Excel格式，数据
 */
public interface ExcelParser {
    List<Object>  parse(Workbook workbook,Class<?> clazz);
}

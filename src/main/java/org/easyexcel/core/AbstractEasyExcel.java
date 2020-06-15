package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.easyexcel.annotation.Entity;
import org.easyexcel.annotation.FieldName;
import org.easyexcel.exception.UnsupportFileTypeException;
import org.easyexcel.io.ExcelReader;
import org.easyexcel.io.ExcelWriter;
import org.easyexcel.io.XMLScanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;

import java.io.File;

/**
 * @author luyao
 * Demo版本，用于读写Excel
 * @see org.easyexcel.annotation.Entity
 * @see org.easyexcel.annotation.FieldName
 */
public abstract class AbstractEasyExcel {
    /**
     * 存放扫描包下的数据类
     */
    protected List<Class<?>> classes = new ArrayList<Class<?>>();

    /**
     * 存放扫描包下加了Entity注解的类
     */

    protected List<Class<?>> entityClasses = new ArrayList<Class<?>>();
    /**
     * 对象容器，存储格式：类名：对象列表
     */
    protected Map<String, List<Object>> entityContainer = new HashMap<String, List<Object>>();

    /**
     * 用于扫描配置信息
     */
    private XMLScanner scanner = null;

    public AbstractEasyExcel(String url) {
        try {
            scanner = new XMLScanner(AbstractEasyExcel.class.getClassLoader().getResource(url).toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void load() {
        initContainer();
    }

    /**
     * 初始化Container
     */
    protected void initContainer() {
        scanPackages();
        scanEntity();
        di();
    }

    /**
     * 扫描包，加载所有类
     */
    private void scanPackages() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        List<String> packages = config.getConfig("properties_property_scanPackage");
        for (String pkg : packages) {
            scanPackage(pkg);
        }
    }

    /**
     * 扫描一个包
     *
     * @param packageName
     */
    private void scanPackage(String packagePath) {
        String path = packagePath.replaceAll("\\.", "/");       //将所有.换成/
        Stack<String> fileStack = new Stack<String>();
        fileStack.push(path);
        while (!fileStack.isEmpty()) {
            String nodePath = fileStack.pop();
            File file = null;
            try {
                file = new File(AbstractEasyExcel.class.getClassLoader().getResource(nodePath).toURI().getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File _f : files) {
                    fileStack.push(nodePath + "/" + _f.getName());
                }
            } else if (file.isFile() && file.getName().endsWith(".class")) {
                try {
                    String classUrl = nodePath.substring(0, nodePath.lastIndexOf(".")).replaceAll("/", "\\.");
                    Class<?> clazz = Class.forName(classUrl);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 扫描所有类，将加了注解的类构造对象保存到容器中
     */
    private void scanEntity() {
        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Entity.class)) {
                entityClasses.add(aClass);
            }
        }
    }

    /**
     * 进行对象生成和属性注入
     */
    protected void di() {
        for (Class<?> entityClass : entityClasses) {
            List<Object> list = excelParseByClass(entityClass, new DefaultlExcelParser());
            entityContainer.put(entityClass.getCanonicalName(), list);
        }
    }

    /**
     * 根据类名解析Excel
     *
     * @param clazz
     * @return
     */
    protected List<Object> excelParseByClass(Class<?> clazz, ExcelParser excelParser) {
        Entity annotation = clazz.getAnnotation(Entity.class);
        String excelPath = annotation.path();
        ExcelReader reader = new ExcelReader(excelPath);
        List<Object> list = excelParser.parse(reader.read(), clazz);
        return list;
    }

    /**
     * 将对象列表添加到excel中
     * @param list
     * @param excelParser
     */
    protected void addToExcel(List<Object> list,ExcelParser excelParser){
        if(list == null || list.size() == 0){
            return;
        }
        Entity annotation = list.get(0).getClass().getAnnotation(Entity.class);
        String excelPath = annotation.path();
        ExcelWriter writer = new ExcelWriter(excelPath);
        writer.write(excelParser.parse(list,writer.getWorkbook()));
    }

    /**
     * 用于判断一个类的某个方法是否被重写
     * @return
     */
    private boolean isOverride(Class<?> clazz,String methodName,Class<?> ...parameter){
        try {
            clazz.getDeclaredMethod(methodName,parameter);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    /**
     * 对比两个Entity对象的值是否相等
     * @param deleteObj 待删除对象
     * @param excelObj Excel中的对象
     * @return
     */
    protected boolean equalObject(Object deleteObj,Object excelObj){
        if(deleteObj == null && excelObj == null){
            return true;
        }
        if(!deleteObj.getClass().getCanonicalName().equals(excelObj.getClass().getCanonicalName())){
            return false;
        }
        //如果重写了equals方法，则直接使用equals方法比较
        if(isOverride(deleteObj.getClass(),"equals",Object.class)){
            return deleteObj.equals(excelObj);
        }
        //如果没有重写equals方法，则使用反射来对比属性值
        Field fields1[] = deleteObj.getClass().getDeclaredFields();
        Field fields2[] = excelObj.getClass().getDeclaredFields();
        boolean flag = true;   //用于记录待删除对象的属性是否全为null
        try {
            for (int i = 0; i < fields1.length; i++) {
                Field field1 = fields1[i];
                Field field2 = fields2[i];
                field1.setAccessible(true);
                field2.setAccessible(true);
                Object field1Result = field1.get(deleteObj);
                Object field2Result = field2.get(excelObj);
                if(field1Result == null){
                    continue;
                }
                flag = false;
                if(field2Result == null || !field1Result.equals(field2Result)){      //暂不支持对象中嵌套对象，只做java类型数据的比较
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return flag == true ? false:true;
    }

    /**
     * 从excel中删除对应的行
     * @param list
     * @param excelParser
     */
    protected void removeFromExcel(List<Object> list,ExcelParser excelParser){
        if(list == null || list.size() == 0){
            return;
        }
        Class<?> clazz = list.get(0).getClass();
        Entity entity = clazz.getAnnotation(Entity.class);
        String excelPath = entity.path();
        ExcelWriter writer = new ExcelWriter(excelPath);
        Workbook workbook = excelParser.maskParse(list,writer.getWorkbook());
        writer.write(workbook);
    }

    /**
     * Excel通用解析器，List<Object>转Workbook，WorkBook转List<Object>
     */
    protected static class DefaultlExcelParser implements ExcelParser {
        private class Header {
            private Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
            private Map<Integer, String> indexToName = new HashMap<Integer, String>();

            public Header(Row firstRow) {
                Iterator<Cell> it = firstRow.iterator();
                while (it.hasNext()) {
                    Cell cell = it.next();
                    nameToIndex.put(cell.getStringCellValue(), cell.getColumnIndex());
                    indexToName.put(cell.getColumnIndex(), cell.getStringCellValue());
                }
            }

            public Integer getColIndex(String name) {
                return nameToIndex.get(name);
            }

            public String getColName(String index) {
                return indexToName.get(index);
            }
        }

        private Header getHeader(Row firstRow) {
            return new Header(firstRow);
        }

        private Header createHeader(Class<?> clazz,Row firstRow){
            Field fields[] = clazz.getDeclaredFields();
            int colIndex = 0;
            for (Field field : fields) {
                FieldName fieldName = field.getAnnotation(FieldName.class);
                if(fieldName != null){
                    String fieldValue = fieldName.value();
                    Cell cell = firstRow.createCell(colIndex++);
                    if(fieldValue != null && !fieldValue.equals("")){
                        cell.setCellValue(fieldValue);
                    }else{
                        cell.setCellValue(field.getName());
                    }
                }
            }
            return new Header(firstRow);
        }

        public List<Object> parse(Workbook workbook, Class<?> clazz) {
            List<Object> list = new ArrayList<Object>();
            try{
                Entity entity = clazz.getAnnotation(Entity.class);
                Sheet sheet = workbook.getSheetAt(entity.sheet());
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                Header header = getHeader(sheet.getRow(firstRowNum));
                for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Object o = parseObejct(header, row, clazz);
                        if (o != null) list.add(o);
                    }
                }
            }finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        public Workbook parse(List<Object> list, Workbook workbook) {
            //根据list将Row添加到workbook最后
            //先创建sheet
            Class<?> clazz = list.get(0).getClass();
            Entity entity = clazz.getAnnotation(Entity.class);
            Sheet sheet = null;
            if(workbook.getNumberOfSheets() > 0){
                sheet = workbook.getSheetAt(entity.sheet());
            }else{
                sheet = workbook.createSheet();
            }
            int rowIndex = sheet.getPhysicalNumberOfRows();
            //创建Header
            Header header;
            if(existsHeader(sheet,clazz)){
                header = getHeader(sheet.getRow(sheet.getFirstRowNum()));
            }else{
                Row headRow = sheet.createRow(rowIndex++);
                header = createHeader(clazz,headRow);
            }
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                if(o != null){
                    Row row = sheet.createRow(rowIndex++);
                    convertDataToRow(header,row,o);
                }
            }
            return workbook;
        }

        public Workbook maskParse(List<Object> list, Workbook workbook) {
            Class<?> clazz = list.get(0).getClass();
            Entity entity = clazz.getAnnotation(Entity.class);
            Sheet sheet = null;
            if(workbook.getNumberOfSheets() > 0){
                sheet = workbook.getSheetAt(entity.sheet());
            }else{
                sheet = workbook.createSheet();
            }
            return null;
        }

        /**
         * 判断是否存在Header，如果workbook中存在row，则可能存在header，可以将row中的数据和class的属性列表对应名对比进一步确认
         * @return
         */
        private boolean existsHeader(Sheet sheet,Class<?> clazz){
            if(sheet.getPhysicalNumberOfRows() == 0){
                return false;
            }else{
                //拿到firstRow
                Row firstRow = sheet.getRow(sheet.getFirstRowNum());
                Header header = getHeader(firstRow);
                Field fields[] = clazz.getDeclaredFields();
                for (Field field : fields) {
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    String fieldValue = fieldName.value();
                    String name;
                    if(fieldValue != null && !fieldValue.equals("")){
                        name = fieldValue;
                    }else{
                        name = field.getName();
                    }
                    if(header.getColIndex(name) == null){
                        return false;
                    }
                }
                return true;
            }
        }

        /**
         * 将一个object对象的属性装载到Row中
         * @param header
         * @param row
         * @param o
         * @return
         */
        private Row convertDataToRow(Header header,Row row ,Object o){
            Class<?> clazz = o.getClass();
            Field fields[] = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if(fieldName != null){
                        int colIndex = 0;
                        String fieldValue = fieldName.value();
                        if(fieldValue != null && !fieldValue.equals("")){
                            colIndex = header.getColIndex(fieldValue);
                        }else{
                            colIndex = header.getColIndex(field.getName());
                        }
                        Cell cell = row.createCell(colIndex);
                        field.setAccessible(true);
                        Object value = field.get(o) == null? "":field.get(o);
                        if(value instanceof Integer){
                            cell.setCellValue((Integer)value);
                        }else if(value instanceof String){
                            cell.setCellValue(String.valueOf(value));
                        }else if(value instanceof Long){
                            cell.setCellValue((Long)value);
                        }else if(value instanceof Double){
                            cell.setCellValue((Double)value);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return row;
        }

        /**
         * 将Row解析成Object
         * @param header
         * @param row
         * @param clazz
         * @return
         */
        private Object parseObejct(Header header, Row row, Class<?> clazz) {
            try {
                java.lang.Object o = clazz.newInstance();
                Iterator<Cell> iterator = row.iterator();
                for (Field field : clazz.getDeclaredFields()) {
                    FieldName annotation = field.getAnnotation(FieldName.class);
                    if (annotation != null) {
                        String value = annotation.value();
                        String name = null;
                        if (value == null || value.equals("")) {
                            name = field.getName();
                        } else {
                            name = value;         //没有配置Field的value属性时，默认开启驼峰命名
                        }
                        Integer col_index = header.getColIndex(name);
                        if (col_index != null) {          //匹配时才注入
                            field.setAccessible(true);
                            Type fieldType = field.getGenericType();
                            if (fieldType.toString().equals("class java.lang.Integer")) {             //这里需要考虑更多的类型
                                double tmp = row.getCell(col_index).getNumericCellValue();          //获取到的肯定是Double类型
                                field.set(o, (int) tmp);
                            } else if (fieldType.toString().equals("class java.lang.String")) {
                                field.set(o, row.getCell(col_index).toString());
                            } else if (fieldType.toString().equals("class java.lang.Double")) {
                                field.set(o, row.getCell(col_index).getNumericCellValue());
                            } else if (fieldType.toString().equals("class java.lang.Long")) {
                                field.set(o, (long) row.getCell(col_index).getNumericCellValue());
                            }
                        }
                    }
                }
                return o;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

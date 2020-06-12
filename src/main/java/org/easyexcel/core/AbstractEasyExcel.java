package org.easyexcel.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.easyexcel.annotation.Entity;
import org.easyexcel.annotation.FieldName;
import org.easyexcel.io.ExcelReader;
import org.easyexcel.io.XMLScanner;

import java.lang.reflect.Field;
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
    protected Map<String, List<Object>> entitryContainer = new HashMap<String, List<Object>>();

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
            entitryContainer.put(entityClass.getCanonicalName(), list);
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
     * Excel通用解析器
     */
    protected static class DefaultlExcelParser implements ExcelParser {
        private class Header {
            private Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
            private Map<Integer, String> indexToName = new HashMap<Integer, String>();

            public Header(Row firstRow) {
                Map<String, Integer> header = new HashMap<String, Integer>();
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

        /**
         * 将java类对应的excel表解析成对象列表
         *
         * @param workbook
         * @param clazz
         * @return
         */
        public List<Object> parse(Workbook workbook, Class<?> clazz) {
            List<Object> list = new ArrayList<Object>();
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
            return list;
        }

        public List<Row> parse(List<Object> list) {
            List<Row> rows = new ArrayList<Row>();
            return rows;
        }


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

package com.ai.generator.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

/**
 * @author: QIK
 * @CreateDate: 2019/4/30 18:33
 */
public class ExcelUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    /**
     * 读取Excel内容
     * @param file
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseExcel(MultipartFile file,Class<T> tClass) throws Exception {
        return parseExcel(file,tClass,1);
    }
    /**
     * 读取Excel内容
     * @param file
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseExcel(MultipartFile file,Class<T> tClass,int startRow) throws Exception {
        if (file == null || file.isEmpty()) {
            //代表没有文件
            return null;
        }
        if (file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())) {
            return null ;
        }
        if(tClass == null){
            return null ;
        }
        Field[] field = tClass.getDeclaredFields();
        Map<Integer, Object[]> res = new ConcurrentHashMap<Integer, Object[]>();
        try {
            for (Field fie : field) {
                if (fie.isAnnotationPresent(Excel.class)) {
                    Excel resources = fie.getAnnotation(Excel.class);
                    if (!res.containsKey(resources.sort())) {
                        res.put(resources.sort(), new Object[]{resources, fie});
                    }
                }
            }
            if (!res.isEmpty()) {
                String filename = file.getOriginalFilename();
                Workbook workbook = null ;
                if(filename.endsWith(".xls")) {
                    //POI导入文件,存放到list集合
                    workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
                }else if(filename.endsWith(".xlsx")){
                    //POI导入文件,存放到list集合
                    workbook = new XSSFWorkbook(file.getInputStream());
                }
                return parseWorkbook(workbook,res,tClass,startRow) ;
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }finally {
            res.clear();
            res = null ;
        }
        return null ;
    }


    /**
     * 读取Excel内容
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<Integer,Object>> parseExcel(MultipartFile file) throws Exception{
        return parseExcel(file,1);
    }
    /**
     * 读取Excel内容
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<Integer,Object>> parseExcel(MultipartFile file,int startRow) throws Exception{
        if (file == null || file.isEmpty()) {
            //代表没有文件
            return null;
        }
        if (file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())) {
            return null ;
        }
        String filename = file.getOriginalFilename();
        Workbook workbook = null;
        if(filename.endsWith(".xls")) {
            //POI导入文件,存放到list集合
            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        }else if(filename.endsWith(".xlsx")){
            //POI导入文件,存放到list集合
            workbook = new XSSFWorkbook(file.getInputStream());
        }
        return parseWorkbook(workbook,startRow) ;
    }

    /**
     *  excel数据导出
      * @param exportDataDTO
     * @param cls
     * @param response
     * @throws IOException
     */
    public static void exportExcel(ExportDataDTO exportDataDTO, Class<?> cls, HttpServletResponse response) throws IOException {
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(exportDataDTO.getFileName(), "utf-8"));
        exportDeal(exportDataDTO,response.getOutputStream(), cls);
    }

    /**
     * 导出数据处理
     *
     * @param exportDataDTO
     * @param out
     */
    public static void exportDeal(ExportDataDTO exportDataDTO, OutputStream out, Class<?> cls) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        try {
            if (exportDataDTO.getSheetName() == null) {
                exportDataDTO.setSheetName("sheet1");
            }
            HSSFSheet hssfSheet = hssfWorkbook.createSheet(exportDataDTO.getSheetName());
            writeWorkBook(hssfWorkbook, hssfSheet, exportDataDTO, cls);
            hssfWorkbook.write(out);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            hssfWorkbook.close();
        }
    }

    /**
     * 解析Workbook转换为对象
     * @param res
     * @param tClass
     * @param startRow
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> List<T> parseWorkbook( Workbook workbook,Map<Integer, Object[]> res,Class<T> tClass,int startRow) throws IllegalAccessException, InstantiationException {
        if(workbook == null || res == null || tClass == null || res.isEmpty()){
            return null ;
        }
        List<T> list = new ArrayList<>();
        Sheet sheetAt = workbook.getSheetAt(0);
        //这个表示当前sheet有多少行数据,一行一行读取就行,但是会把没有数据的行读出来,需要加异常处理
        int rows = sheetAt.getPhysicalNumberOfRows();
        for (int i = startRow; i < rows; i++) {
            //某一行的数据,是一行一行的读取
            Row row = sheetAt.getRow(i);
            if(row == null){
                continue ;
            }
            T t = tClass.newInstance();
            int idx = 0;
            for ( int start = row.getFirstCellNum(), end = row.getLastCellNum(); start < end; start++) {
                Cell cell = row.getCell(start);
                if(cell == null){
                    continue ;
                }
                Object[] objects = res.get(idx++);
                Excel excel = (Excel) objects[0];
                Field fie = (Field)objects[1];
                invokeField(t,excel,fie,cell);
            }
            list.add(t);
        }
        return list ;
    }

    /**
     * 设置字段属性
     * @param field
     * @param cell
     */
    private static void invokeField(Object object,Excel excel ,Field field,Cell cell) throws IllegalAccessException {
        if(field == null || cell==null){
            return ;
        }
        field.setAccessible(true);
        cell.setCellType(CellType.STRING);
        String cellValue = cell.getStringCellValue();
        Object fieldValue = null ;
        if(field.getType() == String.class){
            fieldValue = cellValue ;
        }else if(field.getType() == Integer.class){
            try{
                fieldValue = Integer.parseInt(cellValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(field.getType() == Long.class){
            try{
                fieldValue = Long.parseLong(cellValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(field.getType() == Float.class){
            try{
                fieldValue = Float.parseFloat(cellValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(field.getType() == Double.class){
            try{
                fieldValue = Double.parseDouble(cellValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(field.getType() == Date.class){
            try{
                sdf.applyPattern(excel.format());
                fieldValue = sdf.parse(cellValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        field.set(object,fieldValue);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("");
        sdf.applyPattern("yyyy-MM-dd");
        System.out.println(sdf.format(sdf.parse("2020-12-21")));
    }
    /**
     * 解析Workbook转换为Map
     * @param workbook
     * @param startRow
     * @return
     */
    private static List<Map<Integer,Object>> parseWorkbook(Workbook workbook,int startRow){
        if(workbook == null){
            return null ;
        }
        //意思是有几个文件,一个excel可能有多个sheet,这里只读取第一个
        // int sheets = workbook.getNumberOfSheets();
        //只读取第一个sheet
        Sheet sheetAt = workbook.getSheetAt(0);
        //这个表示当前sheet有多少行数据,一行一行读取就行,但是会把没有数据的行读出来,需要加异常处理
        int rows = sheetAt.getPhysicalNumberOfRows();
        List<Map<Integer,Object>> list = new ArrayList<>();
        for (int i = startRow; i < rows; i++) {
            //某一行的数据,是一行一行的读取
            Row row = sheetAt.getRow(i);
            if(row == null){
                continue ;
            }
            Map<Integer, Object> map = new HashMap<>();
            int idx = 0;
            for (int start = row.getFirstCellNum(), end = row.getLastCellNum(); start < end; start++) {
                Cell cell = row.getCell(start);
                if(cell == null){
                    continue ;
                }
                cell.setCellType(CellType.STRING);
                map.put(idx++, cell.getStringCellValue());
            }
            if(!map.isEmpty()){
                list.add(map);
            }
        }
        return list ;
    }

    /**
     * 数据写入到workbook
     */
    private static void writeWorkBook(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, ExportDataDTO exportDataDTO, Class<?> cls) {
        int rowIndex = 0;
        //------统计有多少列需要导出
        int count = 0;
        Field[] field = cls.getDeclaredFields();
        Map<Integer, Object[]> res = new ConcurrentHashMap<Integer, Object[]>();
        List<Map<Integer, Object[]>> collection = new ArrayList<Map<Integer, Object[]>>();
        for (Field fie : field) {
            if (fie.isAnnotationPresent(Excel.class)) {
                Excel resources = fie.getAnnotation(Excel.class);
                if (!res.containsKey(resources.sort())) {
                    res.put(resources.sort(), new Object[]{resources, fie});
                    count++;
                }
            }
        }
        if (res.size() > 0) {
            //设置数据第一行标题并且居中
            boolean b = createTitle(hssfWorkbook, hssfSheet, count, exportDataDTO);
            if(b) {
                rowIndex++;
            }
            //设置导出的每列标题
            collection.add(res);
            Map<Integer, Object[]> dealSort = new ConcurrentHashMap<>();
            //对res根据key的大小排序，dataList为Filed排序之后的结果
            res.entrySet().stream().sorted(Map.Entry.<Integer, Object[]>comparingByKey().reversed()).forEach(e -> dealSort.put(e.getKey(), e.getValue()));
            List<Object[]> dataList = dealSort.entrySet().stream().map(e -> e.getValue()).collect(toList());
            //导出属性列名对应的字段
            List<String> heads = new ArrayList<>();
            //------获取每列的列名
            HSSFRow row = hssfSheet.createRow(rowIndex);
            rowIndex++;
            int colIndex = 0;
            for (Object[] obj : dataList) {
                Excel resources = (Excel) obj[0];
                HSSFCell cell = row.createCell(colIndex);
                cell.setCellValue(resources.title());
                HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
                cell.setCellStyle(hssfCellStyle);
                Field file = (Field) obj[1];
                heads.add(file.getName());
                colIndex++;
            }
            //-----设置每行的值并根据注解排序
            Map<String, Map<Integer, String>> result = new ConcurrentHashMap<>(heads.size());
            orderBy(heads, exportDataDTO, result);
            for (int i = 0; i < exportDataDTO.getDataList().size(); i++) {
                int zdCell = 0;
                HSSFRow ro = hssfSheet.createRow(rowIndex);
                rowIndex++;
                for (String head : heads) {
                    //写进excel对象
                    ro.createCell((short) zdCell).setCellValue(result.get(head).get(i));
                    zdCell++;
                }
            }
        }
    }


    /**
     * 创建第一行数据标题
     *
     * @param hssfWorkbook
     * @param hssfSheet
     * @param mergeCount    统计合并列的结束位置
     * @param exportDataDTO 获取第一行数据的标题
     */
    public static boolean createTitle(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, int mergeCount, ExportDataDTO exportDataDTO) {
        if(!StringUtils.isEmpty(exportDataDTO.getTitle())){
            Font font = hssfWorkbook.createFont();
            font.setFontName("Arial");
            font.setColor(IndexedColors.BLACK.index);
            font.setBold(true);

            HSSFCellStyle titleStyle = hssfWorkbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFillBackgroundColor(IndexedColors.RED.index);
            HSSFRow row = hssfSheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(exportDataDTO.getTitle());
            CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, mergeCount - 1);
            hssfSheet.addMergedRegion(rangeAddress);

            setStyle(titleStyle, font);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(exportDataDTO.getTitle());
            return true ;
        }
        return false;
    }

    /**
     * 排序
     *
     * @param
     * @param heads         列属性
     * @param exportDataDTO
     * @param result        排序结果
     */
    private static void orderBy(List<String> heads, ExportDataDTO exportDataDTO, Map<String, Map<Integer, String>> result) {
        for (String str : heads) {
            result.put(str, new HashMap<>());
        }
        Integer num = 0;
        for (Object t : exportDataDTO.getDataList()) {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                String propertyName = field.getName();
                if (heads.contains(propertyName)) {
                    //获取value值
                    String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    Class cl = t.getClass();
                    try {
                        Method getMethod = cl.getMethod(methodName,
                                new Class[]{});
                        //操控该对象属性的get方法，从而拿到属性值
                        Object val = getMethod.invoke(t, new Object[]{});
                        if (val != null) {
                            //转化成String
                            result.get(propertyName).put(num, String.valueOf(val));
                        } else {
                            result.get(propertyName).put(num, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            num++;
        }
    }

    /**
     * 指定单元格的样式
     */
    private static void setStyle(HSSFCellStyle titleStyle, Font font) {
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setBorderLeft(BorderStyle.DOTTED);
        titleStyle.setBorderTop(BorderStyle.DOTTED);
        titleStyle.setBorderRight(BorderStyle.DOTTED);
        titleStyle.setBorderBottom(BorderStyle.DOTTED);
        titleStyle.setWrapText(true);
        titleStyle.setFont(font);
    }
}

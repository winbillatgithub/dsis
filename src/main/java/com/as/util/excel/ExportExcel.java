package com.as.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.as.util.PageData;
import com.as.util.StringUtil;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * @version v1.0
 * @param <> 应用泛型，代表任意一个符合javabean风格的类
 *        注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx() byte[]表jpg格式的图片数据
 */
public class ExportExcel {

	@SuppressWarnings("unchecked")
	public File fileExcel(List<Object> list, String fileName) {
		// 创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建表格
		Sheet sheet = workbook.createSheet(fileName);
		// 设置默认宽度
		sheet.setDefaultColumnWidth(25);
		// 创建样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setFillForegroundColor(IndexedColors.GOLD.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		// 生成字体
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		// 表头的样式
		XSSFCellStyle titleStyle = workbook.createCellStyle();// 样式对象
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置字体
		XSSFFont titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 15);
		titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 粗体
		titleStyle.setFont(titleFont);

		// 应用字体
		style.setFont(font);
		// 自动换行
		style.setWrapText(true);
		// 产生表格标题行
		int row_index = 0;
		for (Object e : list) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) e;
			if (row_index == 0) {
				Row rowHeader = sheet.createRow(0);
				int row_cell = 0;
				for (String in : map.keySet()) {
//			           String str = (map.get(in)==null?"":map.get(in));//得到每个key多对用value的值
					Cell cellHeader = rowHeader.createCell(row_cell);
					XSSFRichTextString textHeader = new XSSFRichTextString(in);
					cellHeader.setCellValue(textHeader);
					cellHeader.setCellStyle(titleStyle);
					row_cell++;
				}
			}
			Row rowHeader = sheet.createRow(row_index + 1);
			int row_cell = 0;
			for (String in : map.keySet()) {
				String str = (map.get(in)==null?"":map.get(in)).toString();// 得到每个key多对用value的值
				Cell cell = rowHeader.createCell(row_cell);
				XSSFRichTextString text = new XSSFRichTextString(str);
				cell.setCellValue(text + "");
				cell.setCellStyle(style);
				row_cell++;
			}
			row_index++;
		}

//		DocumentSummaryInformation file =workbook.getDocumentSummaryInformation();
		File file = null;
		try {
			String s1 = fileName + ".xlsx";
			file = new File(s1);// new String(fileName.getBytes(),"ISO-8859-1")
			OutputStream out = new FileOutputStream(file);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	@SuppressWarnings("unchecked")
	public File PageDataExcel(List<PageData> list, String fileName) {
		// 创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建表格
		Sheet sheet = workbook.createSheet(fileName);
		// 设置默认宽度
		sheet.setDefaultColumnWidth(25);
		// 创建样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setFillForegroundColor(IndexedColors.GOLD.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		// 生成字体
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		// 表头的样式
		XSSFCellStyle titleStyle = workbook.createCellStyle();// 样式对象
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置字体
		XSSFFont titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 15);
		titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 粗体
		titleStyle.setFont(titleFont);

		// 应用字体
		style.setFont(font);
		// 自动换行
		style.setWrapText(true);
		// 产生表格标题行
		int row_index = 0;
		for (Object e : list) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) e;
			if (row_index == 0) {
				Row rowHeader = sheet.createRow(0);
				int row_cell = 0;
				for (String in : map.keySet()) {
//			           String str = (map.get(in)==null?"":map.get(in));//得到每个key多对用value的值
					Cell cellHeader = rowHeader.createCell(row_cell);
					XSSFRichTextString textHeader = new XSSFRichTextString(in);
					cellHeader.setCellValue(textHeader);
					cellHeader.setCellStyle(titleStyle);
					row_cell++;
				}
			}
			Row rowHeader = sheet.createRow(row_index + 1);
			int row_cell = 0;
			for (String in : map.keySet()) {
				String str = (map.get(in)==null?"":map.get(in)).toString();// 得到每个key多对用value的值
				Cell cell = rowHeader.createCell(row_cell);
				XSSFRichTextString text = new XSSFRichTextString(str);
				cell.setCellValue(text + "");
				cell.setCellStyle(style);
				row_cell++;
			}
			row_index++;
		}

//		DocumentSummaryInformation file =workbook.getDocumentSummaryInformation();
		File file = null;
		try {
			String s1 = fileName + ".xlsx";
			file = new File(s1);// new String(fileName.getBytes(),"ISO-8859-1")
			OutputStream out = new FileOutputStream(file);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public void fileExcel(List<Object> list, String fileName, HttpServletResponse response) {
		// 创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建表格
		Sheet sheet = workbook.createSheet(fileName);
		// 设置默认宽度
		sheet.setDefaultColumnWidth(25);
		// 创建样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setFillForegroundColor(IndexedColors.GOLD.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		// 生成字体
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		// 表头的样式
		XSSFCellStyle titleStyle = workbook.createCellStyle();// 样式对象
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置字体
		XSSFFont titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 15);
		titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 粗体
		titleStyle.setFont(titleFont);

		// 应用字体
		style.setFont(font);
		// 自动换行
		style.setWrapText(true);
		// 产生表格标题行
		int row_index = 0;
		for (Object e : list) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) e;
			if (row_index == 0) {
				Row rowHeader = sheet.createRow(0);
				int row_cell = 0;
				for (String in : map.keySet()) {
//			           String str = (map.get(in)==null?"":map.get(in));//得到每个key多对用value的值
					Cell cellHeader = rowHeader.createCell(row_cell);
					XSSFRichTextString textHeader = new XSSFRichTextString(in);
					cellHeader.setCellValue(textHeader);
					cellHeader.setCellStyle(titleStyle);
					row_cell++;
				}
			}
			Row rowHeader = sheet.createRow(row_index + 1);
			int row_cell = 0;
			for (String in : map.keySet()) {
				String str = (map.get(in)==null?"":map.get(in)).toString();// 得到每个key多对用value的值
				Cell cell = rowHeader.createCell(row_cell);
				XSSFRichTextString text = new XSSFRichTextString(str);
				cell.setCellValue(text + "");
				cell.setCellStyle(style);
				row_cell++;
			}
			row_index++;
		}

//		DocumentSummaryInformation file =workbook.getDocumentSummaryInformation();
		try {
			String s1 = fileName + ".xlsx";
			OutputStream out = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream;charset=utf-8");// 根据个人需要,这个是下载文件的类型
			s1 = URLEncoder.encode(s1,"utf-8");
			response.addHeader("Content-Disposition", "attachment;   filename="+s1);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void fileExcelList(List<List<Object>> list1, List<String> SheetName, String fileName, HttpServletResponse response) {
		// 创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		for (int i = 0; i < list1.size(); i++) {
			List<Object> list=list1.get(i);
			// 创建表格
			Sheet sheet = workbook.createSheet(SheetName.get(i));
			// 设置默认宽度
			sheet.setDefaultColumnWidth(25);
			// 创建样式
			XSSFCellStyle style = workbook.createCellStyle();
			// 设置样式
			style.setFillForegroundColor(IndexedColors.GOLD.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderTop(CellStyle.BORDER_THIN);
			// 生成字体
			XSSFFont font = workbook.createFont();
			font.setColor(IndexedColors.VIOLET.index);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

			// 表头的样式
			XSSFCellStyle titleStyle = workbook.createCellStyle();// 样式对象
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);// 水平居中
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			// 设置字体
			XSSFFont titleFont = workbook.createFont();
			titleFont.setFontHeightInPoints((short) 15);
			titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 粗体
			titleStyle.setFont(titleFont);

			// 应用字体
			style.setFont(font);
			// 自动换行
			style.setWrapText(true);
			// 产生表格标题行
			int row_index = 0;
			for (Object e : list) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) e;
				if (row_index == 0) {
					Row rowHeader = sheet.createRow(0);
					int row_cell = 0;
					for (String in : map.keySet()) {
//				           String str = (map.get(in)==null?"":map.get(in));//得到每个key多对用value的值
						Cell cellHeader = rowHeader.createCell(row_cell);
						XSSFRichTextString textHeader = new XSSFRichTextString(in);
						cellHeader.setCellValue(textHeader);
						cellHeader.setCellStyle(titleStyle);
						row_cell++;
					}
				}
				Row rowHeader = sheet.createRow(row_index + 1);
				int row_cell = 0;
				for (String in : map.keySet()) {
					String str = (map.get(in)==null?"":map.get(in)).toString();// 得到每个key多对用value的值
					Cell cell = rowHeader.createCell(row_cell);
					XSSFRichTextString text = new XSSFRichTextString(str);
					cell.setCellValue(text + "");
					cell.setCellStyle(style);
					row_cell++;
				}
				row_index++;
			}
		}
		try {
			String s1 = fileName + ".xlsx";
			OutputStream out = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream;charset=utf-8");// 根据个人需要,这个是下载文件的类型
			s1 = URLEncoder.encode(s1,"utf-8");
			response.addHeader("Content-Disposition", "attachment;   filename="+s1);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	 public  static  Map<String,ArrayList<Map<String, Object>>> importExcel(InputStream file,Integer type) throws Exception {  
         
	        //装载流  
	        XSSFWorkbook hw= new XSSFWorkbook(file);  
	          
	        Map<String,ArrayList<Map<String, Object>>>  map = new HashMap<String,ArrayList<Map<String, Object>>>();
	        for (int y = 0; y < hw.getNumberOfSheets(); y++) {
		        XSSFSheet sheet = hw.getSheetAt(y);  
		        String SheetName=sheet.getSheetName();
		        int l=SheetName.indexOf("@");
	        	 //容器  
		        ArrayList<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();  
		        XSSFRow top= sheet.getRow(0);  
		        //遍历行 从下标第一行开始（去除标题）  
		        for (int i = 1; i <= sheet.getLastRowNum(); i++) {  
		            XSSFRow row= sheet.getRow(i);  
		            if(row!=null){  
		                 ret.add(dataObj(top,row,type));  
		            }  
		        }  
		        map.put(SheetName.substring(l<0?0:l+1), ret);
			}
	        return map;  
	    }  
	 
	    private  static  Map<String, Object>  dataObj(XSSFRow top, XSSFRow row,Integer type) throws Exception {  
	        Map<String, Object> map = new LinkedHashMap<String, Object>();  
	        int j=0;
	        for (Iterator<Cell> iterator = top.iterator(); iterator.hasNext();) {
	        	Cell cell =iterator.next();
	        		if(type==1&&StringUtil.isEmpty(row.getCell(j).toString())) {
	        			j++;
	        			continue;
	        		}
	        		int l=cell.getStringCellValue().indexOf("@");
			        map.put(cell.getStringCellValue().substring(l<0?0:l+1), row.getCell(j).toString());
				 j++;
			}
	        return map;   
	    }  



}
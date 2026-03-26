/**
 * 
 */
package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.*;
import org.json.JSONArray;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
import org.json.JSONObject;

/**
 * 
 */
public class JSONToExcelConverter {
	
	private static Map<String, String> flatMap = new LinkedHashMap<>();
	
	public static void converter(String jsonString, String excelFilePath) throws IOException {
		// Implementation of the converter method
		JSONObject jsonObject = new JSONObject(jsonString);
		flattenJSON(jsonObject, "");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("API_Response");
		
		//Header Row
		XSSFRow headerRow = sheet.createRow(0);
		
		int col=0;
		
		for (String key : flatMap.keySet()) {
			headerRow.createCell(col++).setCellValue(key);
		}
		
		//Data Row
		XSSFRow dataRow = sheet.createRow(1);
		col=0;
		
		for (String value : flatMap.values()) {
			dataRow.createCell(col++).setCellValue(value);
		}
		FileOutputStream fileOut= new FileOutputStream(excelFilePath);
		workbook.write(fileOut);
		workbook.close();
		fileOut.close();
			
			System.out.println("JSON data has been converted to Excel successfully.");
	}	
	
	
	private static void flattenJSON(Object object, String prefix) {
		if(object instanceof JSONObject) {
			JSONObject obj = (JSONObject) object;
			for(String key: obj.keySet()) {
				flattenJSON(obj.get(key), prefix + key + ".");
			}
		}
		else if(object instanceof JSONArray) {
			JSONArray arr = (JSONArray) object;
			for(int i=0; i<arr.length(); i++) {
				flattenJSON(arr.get(i), prefix + "[" + i + "].");
			}			
		}
		else {
			String columnName = prefix;
			if(prefix.length()>0 && prefix.endsWith(".")) {
				columnName = prefix.substring(0, prefix.length()-1);
		}
			flatMap.put(columnName, object.toString());
		}
	}
	
}
/*	public static void converter(String jsonString, String excelFilePath) {
		try {
			// Parse the JSON string into a JSONObject
			JSONObject jsonObject = new JSONObject(jsonString);

			// Flatten the JSON object
			flattenJSON(jsonObject, "");

			// Create an Excel workbook and sheet
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Data");

			// Write the flattened data to the Excel sheet
			int rowNum = 0;
			for (Map.Entry<String, String> entry : flatMap.entrySet()) {
				Row row = sheet.createRow(rowNum++);
				Cell keyCell = row.createCell(0);
				Cell valueCell = row.createCell(1);
				keyCell.setCellValue(entry.getKey());
				valueCell.setCellValue(entry.getValue());
			}

			// Save the Excel file
			FileOutputStream fileOut = new FileOutputStream(excelFilePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();

			System.out.println("JSON data has been converted to Excel successfully.");
		} catch (Exception e) {
			e.printStackTrace();

}*/

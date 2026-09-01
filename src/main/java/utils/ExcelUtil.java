package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel utility helpers using Apache POI.
 *
 * Provides simple methods to read an Excel sheet into a List of Maps (column header -> cell value)
 * and to write a List of Maps back to a new .xlsx file.
 */
public class ExcelUtil {

	/**
	 * Read a sheet by name and return rows as a list of maps where each map represents a row with
	 * header->cellValue mappings. The first non-empty row is treated as header row.
	 *
	 * @param excelFilePath path to the Excel file (xlsx / xls)
	 * @param sheetName sheet name to read
	 * @return list of rows as maps (header -> value). Empty list if sheet not found or no data.
	 * @throws IOException on IO problems
	 */
	public static List<Map<String, String>> readSheetAsMaps(String excelFilePath, String sheetName) throws IOException {
		try (InputStream is = new FileInputStream(excelFilePath);
			 Workbook workbook = WorkbookFactory.create(is)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				return new ArrayList<>();
			}
			return sheetToMaps(sheet, workbook);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Failed to read Excel file: " + excelFilePath, e);
		}
	}

	/**
	 * Read a sheet by zero-based index and return rows as a list of maps.
	 */
	public static List<Map<String, String>> readSheetAsMaps(String excelFilePath, int sheetIndex) throws IOException {
		try (InputStream is = new FileInputStream(excelFilePath);
			 Workbook workbook = WorkbookFactory.create(is)) {
			if (sheetIndex < 0 || sheetIndex >= workbook.getNumberOfSheets()) {
				return new ArrayList<>();
			}
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			return sheetToMaps(sheet, workbook);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Failed to read Excel file: " + excelFilePath, e);
		}
	}

	private static List<Map<String, String>> sheetToMaps(Sheet sheet, Workbook workbook) {
		List<Map<String, String>> rows = new ArrayList<>();
		Iterator<Row> rowIter = sheet.rowIterator();
		DataFormatter formatter = new DataFormatter();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		if (!rowIter.hasNext()) {
			return rows; // empty sheet
		}

		// Find header row (first non-empty row)
		Row headerRow = null;
		while (rowIter.hasNext()) {
			Row r = rowIter.next();
			if (rowHasAnyCell(r)) {
				headerRow = r;
				break;
			}
		}

		if (headerRow == null) {
			return rows;
		}

		List<String> headers = new ArrayList<>();
		int lastCol = headerRow.getLastCellNum();
		if (lastCol < 0) lastCol = 0;
		for (int c = 0; c < lastCol; c++) {
			Cell cell = headerRow.getCell(c);
			String header = cell == null ? "" : formatter.formatCellValue(cell, evaluator);
			headers.add(header == null ? "" : header);
		}

		// Process remaining rows
		while (rowIter.hasNext()) {
			Row r = rowIter.next();
			if (!rowHasAnyCell(r)) continue; // skip empty rows
			Map<String, String> map = new HashMap<>();
			for (int c = 0; c < headers.size(); c++) {
				String key = headers.get(c);
				Cell cell = r.getCell(c);
				String value = cell == null ? "" : formatter.formatCellValue(cell, evaluator);
				map.put(key, value);
			}
			rows.add(map);
		}

		return rows;
	}

	private static boolean rowHasAnyCell(Row r) {
		if (r == null) return false;
		int last = r.getLastCellNum();
		if (last <= 0) return false;
		for (int c = 0; c < last; c++) {
			Cell cell = r.getCell(c);
			if (cell != null && cell.getCellType() != CellType.BLANK) return true;
		}
		return false;
	}

	/**
	 * Write a list of maps to a new .xlsx file. The keys of the first map are used as headers.
	 * If the data list is empty a file with only headers will not be created.
	 *
	 * @param excelFilePath path where the .xlsx will be written (overwritten if exists)
	 * @param sheetName sheet name to create
	 * @param data list of maps to write (each map is a row)
	 * @throws IOException on IO problems
	 */
	public static void writeMapsToXlsx(String excelFilePath, String sheetName, List<Map<String, String>> data) throws IOException {
		if (data == null) data = new ArrayList<>();
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(sheetName == null ? "Sheet1" : sheetName);
			if (data.isEmpty()) {
				// nothing to write; create empty file
			} else {
				// use keys from first map as headers
				Map<String, String> first = data.get(0);
				List<String> headers = new ArrayList<>(first.keySet());
				// header row
				Row headerRow = sheet.createRow(0);
				for (int c = 0; c < headers.size(); c++) {
					headerRow.createCell(c).setCellValue(headers.get(c));
				}

				// data rows
				for (int r = 0; r < data.size(); r++) {
					Row row = sheet.createRow(r + 1);
					Map<String, String> rowMap = data.get(r);
					for (int c = 0; c < headers.size(); c++) {
						String key = headers.get(c);
						String val = rowMap.get(key);
						row.createCell(c).setCellValue(val == null ? "" : val);
					}
				}
			}

			try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
				workbook.write(fos);
			}
		}
	}

}

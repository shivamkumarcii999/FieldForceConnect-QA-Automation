package com.fieldforce.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Excel Utility for reading and generating test data using Apache POI.
 */
public class ExcelUtil {

    public static Object[][] getTestData(String filePath, String sheetName) {
        Object[][] data = null;
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Excel file not found at: " + filePath);
            return new Object[0][0];
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }

            int totalRows = sheet.getLastRowNum();
            Row headerRow = sheet.getRow(0);
            int totalCols = headerRow.getLastCellNum();

            data = new Object[totalRows][totalCols];
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < totalCols; j++) {
                    if (row == null) {
                        data[i - 1][j] = "";
                    } else {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        data[i - 1][j] = formatter.formatCellValue(cell).trim();
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            return new Object[0][0];
        }

        return data;
    }

    /**
     * Generates a template Excel file if one does not exist.
     */
    public static void createSampleExcel(String filePath, String sheetName, String[] headers, Object[][] rows) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {

            Sheet sheet = workbook.createSheet(sheetName);

            // Create Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Write Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Write Rows
            for (int r = 0; r < rows.length; r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < rows[r].length; c++) {
                    Cell cell = row.createCell(c);
                    cell.setCellValue(rows[r][c] != null ? rows[r][c].toString() : "");
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fos);
            System.out.println("Generated sample test data Excel file: " + filePath);

        } catch (IOException e) {
            System.err.println("Failed to write Excel file: " + e.getMessage());
        }
    }
}

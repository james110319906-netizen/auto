package until;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jspecify.annotations.NonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExelUntils {
    public static List<Map<String, String>> getExcelData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;

            int colCount = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowMap = new HashMap<>();
                boolean isEmptyRow = true;
                for (int j = 1; j < colCount; j++) {
                    String header = formatter.formatCellValue(headerRow.getCell(j));
                    String value = formatter.formatCellValue(row.getCell(j)).trim();

                    if (!value.isEmpty()) {
                        isEmptyRow = false;
                    }
                    rowMap.put(header, value);
                }

                if (!isEmptyRow) {
                    data.add(rowMap);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file Excel: " + e.getMessage());
        }
        return data;
    }
}

//    public static Object @NonNull [][] getExcelData(String filePath, String sheetName) throws IOException{
//        FileInputStream fis = new FileInputStream(filePath);
//        Workbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheet(sheetName);
//        int rowCount = sheet.getPhysicalNumberOfRows();
//        int colCount = sheet.getRow(0).getLastCellNum();
//
//        Object[][] data = new Object[rowCount - 1][colCount];
//        DataFormatter formatter = new DataFormatter();
//        for(int i = 1; i < rowCount; i++){
//            Row row = sheet.getRow(i);
//            for (int j = 0; j < colCount; j++){
//                Cell cell = (row != null) ? row.getCell(j) : null;
//                data[i-1][j] = (cell != null) ? formatter.formatCellValue(cell) : "";
//            }
//        }
//        workbook.close();
//        fis.close();
//        return data;
//    }
//}
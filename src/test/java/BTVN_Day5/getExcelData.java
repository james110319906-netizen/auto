package BTVN_Day5;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class getExcelData {
    public static void main(String[] args){
        try (FileInputStream file = new FileInputStream("data.xlsx");
                Workbook workbook =new XSSFWorkbook(file)) {
            Sheet sheetDataLogin = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            for (Row row : sheetDataLogin){
                if (row.getRowNum() == 0) continue;
                String id = dataFormatter.formatCellValue(row.getCell(0)).trim();
                String businessEmail = dataFormatter.formatCellValue(row.getCell(1)).trim();
                String firstName = dataFormatter.formatCellValue(row.getCell(2)).trim();
                String lastName = dataFormatter.formatCellValue(row.getCell(3)).trim();
                String company = dataFormatter.formatCellValue(row.getCell(4)).trim();
                String phoneNumber = dataFormatter.formatCellValue(row.getCell(5)).trim();
                String country = dataFormatter.formatCellValue(row.getCell(6)).trim();
                String state = dataFormatter.formatCellValue(row.getCell(7)).trim();
                String useCase = dataFormatter.formatCellValue((row.getCell(8))).trim();
                String comment = dataFormatter.formatCellValue(row.getCell(9)).trim();
                if (!businessEmail.isEmpty() || !firstName.isEmpty()){
                    System.out.println("email: " + businessEmail);
                    System.out.println("firstName: " + firstName);
                }
            }
        } catch (IOException e) {
            System.err.println("da xay ra loi khi doc file excel: " + e.getMessage());
        }
    }
}

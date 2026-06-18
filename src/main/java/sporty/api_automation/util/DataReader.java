package sporty.api_automation.util;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class DataReader {

    private static final String FILE_PATH = "sporty/api_automation/resources/testdata.xlsx";

    public static Object[][] getSheetData(String sheetName) {
        try {
            InputStream is = DataReader.class.getClassLoader().getResourceAsStream(FILE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            // Skip header row (row 0), data starts from row 1
            Object[][] data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                XSSFRow row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    data[i - 1][j] = row.getCell(j).getStringCellValue();
                }
            }

            workbook.close();
            is.close();
            return data;

        } catch (Exception e) {
            throw new RuntimeException("Failed to read sheet '" + sheetName + "' from " + FILE_PATH, e);
        }
    }
}

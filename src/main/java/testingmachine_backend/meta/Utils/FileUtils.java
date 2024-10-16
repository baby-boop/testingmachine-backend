package testingmachine_backend.meta.Utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtils {

    // txt
    public static List<String> readIdsFromFile(String fileName) throws IOException {
        List<String> ids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing spaces
                if (!line.isEmpty()) {
                    ids.add(line); // Add only non-empty lines
                }
            }
        }
        return ids;
    }

    //excel
    public static List<String> readIdsFromExcel(String filePath){
        List<String> ids = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (rowIndex >= 1) { // Skip the header, starts from row 2
                    Cell cell = row.getCell(1); // Column B
                    if (cell != null) {
                        ids.add(cell.getStringCellValue().trim());
                    }
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

    //2 path
    public static List<String[]> readIdMenuPairsFromFile(String filePath) {
        List<String[]> idMenuPairs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split by whitespace (space or tab)
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String id = parts[0];
                    String menuId = parts[1];
                    idMenuPairs.add(new String[]{id, menuId});
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return idMenuPairs;
    }
}

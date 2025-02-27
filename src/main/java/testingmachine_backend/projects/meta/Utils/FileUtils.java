package testingmachine_backend.projects.meta.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class FileUtils {

    // txt
    public static List<String> readIdsFromFile(String fileName) throws IOException {
        List<String> ids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); //
                if (!line.isEmpty()) {
                    ids.add(line); //
                }
            }
        }
        return ids;
    }

    //excel
//    public static List<Map<String, String>> readDataFromExcel(String filePath) {
//        List<Map<String, String>> data = new ArrayList<>();
//        try (FileInputStream fis = new FileInputStream(new File(filePath));
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            int rowIndex = 0;
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                if (rowIndex >= 1) { // Skip header row
//                    Cell idCell = row.getCell(5); // ID
//                    Cell codeCell = row.getCell(6); // Code
//                    Cell nameCell = row.getCell(7); // Name
//                    Cell moduleCell = row.getCell(4);
//
//                    if (idCell != null) {
//                        Map<String, String> record = new HashMap<>();
//                        record.put("id", idCell.getStringCellValue().trim());
//                        record.put("code", codeCell != null ? codeCell.getStringCellValue().trim() : "");
//                        record.put("name", nameCell != null ? nameCell.getStringCellValue().trim() : "");
//                        record.put("moduleName", moduleCell != null ? moduleCell.getStringCellValue().trim() : "");
//
//                        data.add(record);
//                    }
//                }
//                rowIndex++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    public static List<Map<String, String>> readDataFromExcel(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);  // Get the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (rowIndex >= 1) { // Skip header row
                    Cell idCell = row.getCell(4); // ID column
                    Cell codeCell = row.getCell(5); // Code column
                    Cell nameCell = row.getCell(6); // Name column
                    Cell moduleCell = row.getCell(3); // Module column

                    if (idCell != null) {
                        Map<String, String> record = new HashMap<>();

                        // Get ID (String type)
                        record.put("id", getCellValue(idCell));

                        // Get Code (String type)
                        record.put("code", getCellValue(codeCell));

                        // Get Name (String type)
                        record.put("name", getCellValue(nameCell));

                        // Get Module Name (String type)
                        record.put("moduleName", getCellValue(moduleCell));

                        data.add(record);
                    }
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Helper method to get cell value as a string based on the cell type
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0$", "");
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // You can also evaluate the formula if needed
            default:
                return "";
        }
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

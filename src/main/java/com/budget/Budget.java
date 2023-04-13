package com.budget;

import com.github.jsixface.YamlConfig;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Budget {
    public static YamlConfig config;
    public static Workbook newExcelFile;
    public static int bank;

    public static void main(String[] args) {
        try {
            // ask user to choose the bank and read the configs
            readConfigs(chooseBank());

            // get, from user, the original file location
            System.out.println("Please insert the full path for original file (path + file name)");
            String filePath = (new Scanner(System.in)).nextLine();
            filePath = filePath.replaceAll("\"", "");

            // create new Excel file
            newExcelFile = createExcelFile();

            // read original file
            BufferedReader originalFile = readFile(filePath);

            // read each line of the original file and process it into the new Excel file row
            int lineCount = 1;
            String originalFileLine;
            int firstLine = config.getInt("FIRST_LINE");
            while((originalFileLine = originalFile.readLine()) != null){
                if(lineCount >= firstLine) {
                    String[] columns = originalFileLine.split(config.getString("DELIMITER"));

                    if(columns.length > 1) {
                        originalFileLineToNewExcelRow(columns, lineCount-firstLine+1);
                    }
                }

                lineCount++;
            }

            saveExcelFile(filePath, newExcelFile);

        }
        catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static String chooseBank() {
        System.out.println("Please choose one option: \n\t1 - Montepio \n\t2 - Crypto\n\t3 - ActivoBank\n\t4 - CreditoAgricola");
        String option = (new Scanner(System.in)).nextLine();
        String configFile;
        switch (option) {
            case "1" -> { configFile = "configMontepio.yml"; bank = 1; }
            case "2" -> { configFile = "configCrypto.yml"; bank = 2; }
            case "3" -> { configFile = "configActivoBank.yml"; bank = 3; }
            case "4" -> { configFile = "configCreditoAgricola.yml"; bank = 4; }
            default -> {
                bank = -1;
                System.out.println("Invalid option");
                throw new InvalidParameterException();
            }
        }

        return configFile;
    }

    static void readConfigs(String configFile) {
        InputStream resource = Budget.class
                .getClassLoader()
                .getResourceAsStream(configFile);

        config = YamlConfig.load(resource);
    }

    static BufferedReader readFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        return new BufferedReader(fileReader);
    }

    static Workbook createExcelFile() {
        // create Excel file
        Workbook workbook = new XSSFWorkbook();

        // add sheet
        Sheet sheet = workbook.createSheet(config.getString("BANK_NAME"));

        // add header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Origin");
        header.createCell(1).setCellValue("Date");
        header.createCell(2).setCellValue("Type");
        header.createCell(3).setCellValue("Category");
        header.createCell(4).setCellValue("Sub-category");
        header.createCell(5).setCellValue("Value");
        header.createCell(6).setCellValue("Original Description");

        DataFormat dataFormat = workbook.createDataFormat();

        // define text style
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setDataFormat(dataFormat.getFormat("TEXT"));

        // define date style
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd-mm-yyyy"));

        // set each Excel column style
        sheet.setDefaultColumnStyle(0, textStyle);
        sheet.setDefaultColumnStyle(1, dateStyle);
        sheet.setDefaultColumnStyle(2, textStyle);
        sheet.setDefaultColumnStyle(3, textStyle);
        sheet.setDefaultColumnStyle(4, textStyle);
        sheet.setDefaultColumnStyle(5, textStyle);
        sheet.setDefaultColumnStyle(6, textStyle);

        return workbook;
    }

    static void saveExcelFile(String filePath, Workbook fileToSave) throws IOException {
        File folderToSave = new File(filePath + "_FINAL.xlsx");
        String fileLocation = folderToSave.getAbsolutePath();
        FileOutputStream outputStream = new FileOutputStream(fileLocation);

        fileToSave.write(outputStream);
        fileToSave.close();

        System.out.println("File created at: " + fileLocation);
    }

    static void originalFileLineToNewExcelRow(String[] columns, int rowNum) throws ParseException {
        String bankName = config.getString("BANK_NAME");
        double amount = getAmount(columns[config.getInt("AMOUNT_COLUMN_POSITION")]);
        Date date = getDate(columns[config.getInt("DATE_COLUMN_POSITION")]);
        String type = getType(amount);
        String originalDescription = columns[config.getInt("DESCRIPTION_COLUMN_POSITION")];
        String category = getCategory(originalDescription, amount);
        String subCategory = getSubCategory(originalDescription, amount);

        // add new row, with values for each column, to Excel file
        Row row = newExcelFile.getSheet(bankName).createRow(rowNum);
        row.createCell(0).setCellValue(bankName);
        row.createCell(1).setCellValue(date);
        row.createCell(2).setCellValue(type);
        row.createCell(3).setCellValue(category);
        row.createCell(4).setCellValue(subCategory);
        row.createCell(5).setCellValue(amount);
        row.createCell(6).setCellValue(originalDescription);
    }

    static Date getDate(String originalDate) throws ParseException {
        String dateFormat = config.getString("DATE_FORMAT");
        return (new SimpleDateFormat(dateFormat)).parse(originalDate);
    }

    static double getAmount(String originalValue) {
        if(bank == 1) {
            return Double.parseDouble(originalValue.replace(".", "").replace(",", "."));
        }
        return Double.parseDouble(originalValue);
    }

    static String getType(double amount) {
        return (amount > 0) ? "Income" : "Expense";
    }

    static String getCategory(String originalDescription, double value) {
        // TODO: we can add here some logic to get the category
        return "";
    }

    static String getSubCategory(String originalDescription, double value) {
        // TODO: we can add here some logic to get the sub-category
        return "";
    }
}

package com.vanancrm.TestCases.BulkDataTesting.BulkTranslation;

import com.vanancrm.Common.TestBase;
import com.vanancrm.PageObjects.MainPages.Gmail;
import com.vanancrm.PageObjects.MainPages.Translation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TranslationLevel1 extends TestBase {

    private WebDriver driver;

    private static String username = "";
    private static String password = "";

    private String service = "Translation";

    private static List<String> sourceLanguages = new ArrayList<String>();
    private static List<String> targetLanguages = new ArrayList<String>();
    private static List<Double> unitCosts = new ArrayList<Double>();
    private static List<String> processings = new ArrayList<String>();
    private static List<Double> pages = new ArrayList<Double>();

    private double totalUnitCost = 0;
    private double grandtotal = 0;
    private double transcationCost = 0;
    private double orderCost = 0;

    private String unitStatus = "";
    private String totalUnitStatus = "";
    private String grandtotalStatus = "";
    private String transcationStatus = "";
    private String orderStatus = "";

    @Test
    public void testStep() throws IOException {
        for (int i = 0; i < 2; i++) {
            //driver.get(System.getProperty("website"));
            driver.get("https://vananservices.com/Translation-Quote.php");
            sheet = workbook.getSheetAt(0);
            Translation translation = new Translation(driver);
            System.out.print("S.No " + (i + 1));
            System.out.print("Source language : " + sourceLanguages.get(i));
            System.out.print("Target language : " + targetLanguages.get(i));
            System.out.print("Unit costs : " + unitCosts.get(i));
            System.out.print("Processings : " + processings.get(i));
            translation.selectFileType("Document");
            translation.enterDocumentType("testing");
            translation.pageCount(pages.get(i).intValue()+"");
            translation.selectLanguageFrom(sourceLanguages.get(i));
            translation.selectLanguageTo(targetLanguages.get(i));
            String currentUrl = "";
            if (!processings.get(i).equalsIgnoreCase("Request")) {

                totalUnitCost = pages.get(i) * unitCosts.get(i);
                grandtotal = totalUnitCost;
                transcationCost = grandtotal * 0.05;
                orderCost = grandtotal + transcationCost;

                unitStatus = checkStatus(unitCosts.get(i), translation.getActualCost(), "Unit cost");
                totalUnitStatus = checkStatus(totalUnitCost, translation.getTranslationCost(), "Total Unit cost");
                grandtotalStatus = checkStatus(grandtotal, translation.getSubTotal(), "Grand Total");
                transcationStatus = checkStatus(transcationCost, translation.getTransactionFee(), "Transaction fee");
                orderStatus = checkStatus(orderCost, translation.getGrandTotal(), "Order total");

                cell = sheet.getRow(i + 1).getCell(5);
                cell.setCellValue(totalUnitCost);
                cell = sheet.getRow(i + 1).getCell(6);
                cell.setCellValue(grandtotal);
                cell = sheet.getRow(i + 1).getCell(7);
                cell.setCellValue(transcationCost);
                cell = sheet.getRow(i + 1).getCell(8);
                cell.setCellValue(orderCost);
                cell = sheet.getRow(i + 1).getCell(9);
                cell.setCellValue(unitStatus);
                cell = sheet.getRow(i + 1).getCell(10);
                cell.setCellValue(totalUnitStatus);
                cell = sheet.getRow(i + 1).getCell(11);
                cell.setCellValue(grandtotalStatus);
                cell = sheet.getRow(i + 1).getCell(12);
                cell.setCellValue(transcationStatus);
                cell = sheet.getRow(i + 1).getCell(13);
                cell.setCellValue(orderStatus);
                cell = sheet.getRow(i + 1).getCell(15);

                if (unitStatus.equals("Pass") && totalUnitStatus.equals("Pass") && grandtotalStatus.equals("Pass")
                        && transcationStatus.equals("Pass") && orderStatus.equals("Pass")) {

                    cell.setCellValue("Pass");
                } else {
                    cell.setCellValue("Fail");
                }

                translation.emailId(username);
                translation.clickPrivacyPolicy();

                if (processings.get(i).equalsIgnoreCase("Pay")) {
                    translation.clickProceedPayment();
                    waitForProcessCompletion(10);
                    currentUrl = driver.getCurrentUrl();
                    cell = sheet.getRow(i + 1).getCell(14);
                    cell.setCellValue(checkCondition(currentUrl, "paypal"));
                } else if (processings.get(i).equalsIgnoreCase("Email")) {
                    translation.clickEmailMeGetQuote();
                    waitForProcessCompletion(10);
                    currentUrl = driver.getCurrentUrl();
                    cell = sheet.getRow(i + 1).getCell(14);
                    cell.setCellValue(checkCondition(currentUrl, "additional-information.php"));
                }
                CellStyle wrapText = workbook.createCellStyle();
                wrapText.setWrapText(true);
                cell.setCellStyle(wrapText);
            } else {
                cell = sheet.getRow(i + 1).getCell(15);
                if (translation.isCustomMessageDisplayed()) {

                    cell.setCellValue("Pass");
                } else {
                    cell.setCellValue("Fail");
                }
                translation.emailId(username);
                translation.clickPrivacyPolicy();
                translation.clickGetQuote();
                waitForProcessCompletion(10);
                currentUrl = driver.getCurrentUrl();
                cell = sheet.getRow(i + 1).getCell(14);
                cell.setCellValue(checkCondition(currentUrl, "additional-information.php"));
            }

            System.out.println();
        }
        workbook.write(fileOutput);
        fileOutput.close();
    }

    private String checkStatus(double data1, double data2, String message) {
        String status;
        System.out.println(message);
        if (data1 == data2) {
            System.out.print(": Pass\n");
            status = "Pass";
        } else {
            System.out.print(": Fail\n");
            System.out.println("Expected : " + data1);
            System.out.println("Actual : " + data2);
            status = "Fail\n" + "Expected : " + data1 + "\nActual : " + data2;
        }
        return status;
    }

    @BeforeClass
    public void beforeClass() throws IOException {
        System.setProperty("webdriver.chrome.driver", "/tmp/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        fullScreen(driver);
        readTranslateData();
        getEmailCreadential();
        fileOutput = new FileOutputStream(file);
    }

    @AfterClass
    public void afterClass() {

        //driver.quit();
    }

    public static void readTranslateData() throws IOException {

        file = new File("src/test/resources/Translation/TranslationL1.xls");
        fileInput = new FileInputStream(file);
        workbook = new HSSFWorkbook(fileInput);
        sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            cell = sheet.getRow(i).getCell(0);
            sourceLanguages.add(cell.getStringCellValue());
            cell = sheet.getRow(i).getCell(1);
            targetLanguages.add(cell.getStringCellValue());
            cell = sheet.getRow(i).getCell(2);
            unitCosts.add(cell.getNumericCellValue());
            cell = sheet.getRow(i).getCell(3);
            processings.add(cell.getStringCellValue());
            cell = sheet.getRow(i).getCell(4);
            pages.add(cell.getNumericCellValue());
        }
    }

    private static void getEmailCreadential() throws IOException {

        FileReader fileReader = new FileReader(System.getProperty("user.dir")
                + "/src/test/resources/gmail.txt");
        Properties properties = new Properties();
        properties.load(fileReader);
        username = properties.getProperty("USERNAME");
        password = properties.getProperty("PASSWORD");
    }

    private String checkCondition(String currentUrl, String site) {
        String status = "";
        if (currentUrl.contains(site)) {
            System.out.println(currentUrl + " and it pass");
            status = currentUrl + " and it pass";
        } else {
            System.out.println(currentUrl + " and it fail");
            status = currentUrl + " and it fail";
        }
        return status;
    }
}

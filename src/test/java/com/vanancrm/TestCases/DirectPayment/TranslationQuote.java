package com.vanancrm.TestCases.DirectPayment;

import java.awt.AWTException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

import com.vanan.CRM.PageObjects.WholeSitePages.*;
import com.vanancrm.Common.TranslationPrice;
import com.vanancrm.PageObjects.MainPages.Translation;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.vanancrm.PageObjects.MainPages.AdditionalInformation;
import com.vanan.CRM.PageObjects.MainPages.DashBoardPage;
import com.vanan.CRM.PageObjects.MainPages.Edit;
import com.vanan.CRM.PageObjects.MainPages.EmailConversation;

import com.vanan.CRM.PageObjects.MainPages.UnknownEmail;


import com.vanancrm.Common.TestBase;
import com.vanancrm.Common.TranslationPrice;

/**
 * Author - Manikavasagam (manikavasagam@vananservices.com)
 */
public class TranslationQuote extends TestBase implements TranslationPrice {

    private static String username = "";
    private static String password = "";
    private WebDriver driver;
    private String[] additionalService = {"Captioning/Subtitling", "Voice Over"};
    private String[] channels = {"Email Quote", "Direct Payment", "Request for Quote"};
    private String[] categorys = {"General", "Legal"};

    private String[] timecodes = {"Every 3 sec", "Every 1 minute",
            "Not required"};

    private String[] fileTypes = {"Document", "Audio/Video"};
    private String[] srclanguages = {"English", "Japanese", "Afrikaans"};
    private String[] tarlanguages = {"Spanish", "English"};
    // 0 represents Tier 1 and 2
    // 1 represents Tier 3
    // 2 represents Tier 4
    // 3 represents Tier 5
    private int[] tiers = {0, 1, 3};
    private String address = "Vanan";
    private String country = "Alaska";
  //  private String UScountry = "United States";
    private String comment = "Automation Testing";

    private String fileName = "";
    private String fileExtention = "";
    private String minute = "10";
    private String mailId = "automation@vananservices.com";
    private String name = "Automation";
    private String phoneNumber = "1-888-535-5668";
    private String service = "Translation";
    private String serviceType = "Weekly";
    private String url = "";

    private double bPrice = 0;
    private double gtot = 0;
    private int min;
    private double offerPr = 0;
    private double orderTot = 0;
    private double certification = 0;
    private double exper = 0;
    private double trans = 0;

    private DashBoardPage dashBoardPage;
    private EmailConversation emailConversation;
    private Menus menus;
    private ReadTableData readTableData;
    private ViewTicketDetails viewTicketDetails;

    private static void getCRMCreadential() throws IOException {

        FileReader fileReader = new FileReader(System.getProperty("user.dir")
                + "/src/test/resources/CRM.txt");
        Properties properties = new Properties();
        properties.load(fileReader);
        username = properties.getProperty("USERNAME");
        password = properties.getProperty("PASSWORD");
    }

    @Test
    public void translationServices() throws IOException,
            InterruptedException, AWTException {
        url = System.getProperty("website");
        System.out.println("\n======================================");
        System.out.println("\nScenario Started");
        System.out.println("\n======================================");
        System.out.println("\nScenario #1");
         if (!url.contains("Upload")) {
            testScenario(fileTypes[0], srclanguages[0], tarlanguages[0],
                    false, true, tiers[0], channels[0], false);
            System.out.println("\n======================================");
            System.out.println("\nScenario #2");
            testScenario(fileTypes[0], srclanguages[1], tarlanguages[1],
                    true, false, tiers[1], channels[1],false);
            /*System.out.println("\n======================================");
            System.out.println("\nScenario #3");
            testScenario(fileTypes[0], srclanguages[2], tarlanguages[1],
                    true, false, tiers[2], channels[0], false);*/
            System.out.println("\n======================================");
            System.out.println("\nScenario #4");
            testScenario(fileTypes[1], srclanguages[2], tarlanguages[1],
                    true, false, 0, channels[2], false);
        } else if (url.contains("Quote")){
            testScenario(fileTypes[0], srclanguages[1], tarlanguages[1],
                    true, false, tiers[1], channels[1],false);
            System.out.println("\n======================================");
            System.out.println("\nScenario #2");
            testScenario(fileTypes[1], srclanguages[2], tarlanguages[1],
                    true, false, 0, channels[2], false);
        }
           //     else if(url.contains("Free-Trial")){
          //      testScenario(fileTypes[0], srclanguages[0], tarlanguages[0],
             //           false, true, tiers[0], channels[0], false);
            //        System.out.println("\nScenario #2");
            //    testScenario(fileTypes[1], srclanguages[2], tarlanguages[1],
                //        true, false, 0, channels[2], false);

            //}


        System.out.println("Test Completed");

        System.out.println("======================================");
    }

    @BeforeClass
    public void beforeClass() {

        System.setProperty("webdriver.chrome.driver", "/tmp/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        fullScreen(driver);
    }

    @AfterClass
    public void afterClass() throws IOException {

        screenshot(driver, "TranslationQuote");
        driver.quit();
    }

    private void raiseTicket(String fileType, String srcLang, String targetLang,
                             boolean additionalQty, boolean tat, int tier,
                             boolean offer)
            throws AWTException,
            InterruptedException, IOException {

        driver.get(url);
        Translation translation = new Translation(driver);
        translation.clickBusiness();
        translation.selectFileType(fileType);
        if (fileType.equals(fileTypes[1])) {
            translation.minutes(minute);
        } else {
            translation.pageCount(minute);
        }
        translation.selectLanguageFrom(srcLang);
        translation.selectLanguageTo(targetLang);
        if (!targetLang.equals(srclanguages[0])) {
            translation.selectCertification();
        } else {
            translation.selectNotarization();
        }

        if (additionalQty) {
            translation.selectAdditionalQtyCheck(1);
        }
                 
         if (fileType.equals(fileTypes[0])) {
            translation.selectFreeTrail();
        }
         translation.selectRequestMailCopy(country, address);
                
        fileName = "AutomationTesting";
        if (fileType.equals(fileTypes[0])) {
            fileExtention = ".txt";
            translation.uploadFile(driver, fileName, fileExtention);
        } else {
            fileExtention = ".mp3";
            translation.uploadFile(driver, fileName, fileExtention);
        }

        waitForProcessCompletion(50);
        if(fileType.equals(fileTypes[1])) {
            translation.enterFileLength(minute);
        }
         
        translation.enterComments(comment);
        translation.emailId(mailId);

        if (tat) {
            translation.selectTAT(1);
        }
        if (fileType.equals(fileTypes[1])) {
            translation.selectNeedTranscript(0);
            translation.selectNativeSpeaker();
            translation.selectTimeCode(0);
            waitForProcessCompletion(40);
            if (translation.isCustomMessageDisplayed()) {
                System.out.println("Quote info message is displayed");
            }
        } else {
            waitForProcessCompletion(40);
            double off = 0;
            if(offer) {
                off = translation.getOfferFee();
            }
            if (!targetLang.equals(srclanguages[0])) {


                checkPrice(tier, offer,
                        additionalQty, tat, off, translation.getActualCost(),
                        0.5, translation.getExpeditedCost(), translation.getSubTotal(),
                        translation.getAdditionalQualityAmount(), translation
                                .getTransactionFee(), translation
                                .getGrandTotal(), translation.getTranslationCost(),
                        false, 0, 0);
            } else {
                checkPrice(tier, offer,
                        additionalQty, tat, off, translation.getActualCost(),
                        0.5, translation.getExpeditedCost(), translation.getSubTotal(),
                        translation.getAdditionalQualityAmount(), translation
                                .getTransactionFee(), translation
                                .getGrandTotal(), translation.getTranslationCost(),
                        true, translation.getNotaryFee(), translation.getProcessFee());
            }
        }
        if (srcLang.equals(srclanguages[2])) {

            if (translation.isCustomMessageDisplayed()) {
                System.out.println("Quote info message is displayed");
            }
        }
        if (fileType.equals(fileTypes[1])) {
            /*translation.clickGetQuote();
            if(translation.getToolTipMessage().contains("Please agree to terms and conditions to proceed")) {
                System.out.println("Accept button checked = > Pass");
            } else {
                System.out.println("Fail");
            }*/
            translation.clickPrivacyPolicy();
            translation.clickGetQuote();
        } else if (srcLang.equals(srclanguages[1])) {
            /*translation.clickProceedPayment();
            if(translation.getToolTipMessage().contains("Please agree to terms and conditions to proceed")) {
                System.out.println("Accept button checked = > Pass");
            } else {
                System.out.println("Fail");
            }*/
            translation.clickPrivacyPolicy();
            translation.clickProceedPayment();
        } else if (srcLang.equals(srclanguages[0]) || srcLang.equals
                (srclanguages[2])) {
            /*translation.clickEmailMeGetQuote();
            if(translation.getToolTipMessage().contains("Please agree to terms and conditions to proceed")) {
                System.out.println("Accept button checked = > Pass");
            } else {
                System.out.println("Fail");
            }*/
            translation.clickPrivacyPolicy();
            translation.clickEmailMeGetQuote();
        }

        waitForProcessCompletion(15);
        String currentUrl = driver.getCurrentUrl();
        if (!srcLang.equals(srclanguages[1])) {

            checkCondition(currentUrl, "additional-information.php");
            AdditionalInformation additionalInformation = new AdditionalInformation
                    (driver);
            additionalInformation.enterCustomerName(name);
          //  additionalInformation.selectCountry(UScountry);
            additionalInformation.enterPhoneNumber(phoneNumber);
            additionalInformation.selectServiceType(serviceType);
            //additionalInformation.selectAdditionalServices(additionalService);
            additionalInformation.enterComments(comment);
            additionalInformation.clickPrivacyPolicy();
            additionalInformation.clickSubmitButton();
        } else {
            checkCondition(currentUrl, "paypal");
        }
        waitForProcessCompletion(20);
    }

    private void checkPrice(int tier, boolean offer,
                            boolean additionalQty, boolean tat, double oPri, double baPrice,
                            double expe, double expa, double gtotal,
                            double additional, double transFee, double total, double ucost,
                            boolean notary, double notFee, double processfee) {

        String message = "";
        if (tier == 0) {
            bPrice = basePrices[0];
        } else if (tier == 1) {
            bPrice = basePrices[1];
        } else if (tier == 2) {
            bPrice = basePrices[2];
        } else if (tier == 3) {
            bPrice = basePrices[3];
        }
        min = Integer.parseInt(minute);
        message = "Base price Actual";
        evaluateCondition(message, bPrice, baPrice);
        message = "Base price Total";
        evaluateCondition(message, (bPrice * min), ucost);

        if (offer) {
            message = "New year offer";
            if ((bPrice * min) < 1000) {
                offerPr = (bPrice * min) - (offerPri[0] * (bPrice * min));
            } else {
                offerPr = (bPrice * min) - (offerPri[1] * (bPrice * min));
            }
            evaluateCondition(message, offerPr, oPri);
        } else {
            offerPr = (bPrice * min);
        }
        if (notary) {
            certification = notaryFee + notaryProcessFee;
            message = "Notary Fee ";
            evaluateCondition(message, notaryFee, notFee);
            message = "Notary Processing Fee ";
            evaluateCondition(message, notaryProcessFee, processfee);
        } else {
            certification = certificationFee;
            message = "Expedited service fee";
            evaluateCondition(message, (bPrice * min) * expe, expa);
            exper = (bPrice * min) * expe;
        }
        if (tat) {

            message = "Expedited service fee";
            evaluateCondition(message, (bPrice * min) * expe, expa);
            gtot = certification + offerPr + ((bPrice * min) * expe) +
                    mailingFee;

        }
        if (additionalQty) {
            message = "Additional Quality Check";
            evaluateCondition(message, additionalQtyPri * min, additional);
            gtot = certification + offerPr + (additionalQtyPri * min) +
                    mailingFee;
        }
        trans = (double) Math.round((gtot * transcationPri) * 100) / 100;
        orderTot = gtot + trans;
        message = "Grand total";
        evaluateCondition(message, gtot, gtotal);
        message = "Transaction Fee";
        evaluateCondition(message, trans, transFee);
        message = "Order total";
        evaluateCondition(message, orderTot, total);

    }

    private void evaluateCondition(String message, double first,
                                   double second) {

        System.out.print(message + " : " + second);
        if (first == second) {

            System.out.print("\t Status : Pass\n");
        } else {

            System.out.print("\t Status : [Fail]\n");
            System.out.print("\t Expected : " + first + "\n");
            System.out.print("\t Actual : " + second + "\n");
        }
    }

    private void checkCondition(String currentUrl, String site) {
        if (currentUrl.contains(site)) {
            System.out.println(currentUrl + " and it pass");
        } else {
            System.out.println(currentUrl + " and it fail");
        }
    }

    private void checkCRM(String fileType, String srcLang, String targetLang,
                          boolean additionalQty, boolean tat, int tier, String
                                  channel, boolean offer) {

        driver.get("https://secure-dt.com/crm/user/login");
        Cookie name = new Cookie("TEST_MODE", "TEST_MODE");
        driver.manage().addCookie(name);
        Login login = new Login(driver);
        dashBoardPage = login.signIn(username, password);
        menus = dashBoardPage.clickAllProcess();
        checkTickets(menus, fileType, srcLang, targetLang, additionalQty, tat,
                tier, channel, offer);
        waitForProcessCompletion(10);
        menus.clickSignOut();
    }

    private void checkTickets(Menus menus, String fileType, String srcLang, String targetLang,
                              boolean additionalQty, boolean tat, int tier,
                              String channel, boolean offer) {

        String ticketID = "";
        readTableData = menus.clickNewMenu();
        List<String> tickets = readTableData.readTableRows();

        for (int i = 0; i < tickets.size(); i++) {

            if (tickets.get(i).contains(service)) {

                waitForProcessCompletion(20);
                viewTicketDetails = new ViewTicketDetails(driver);
                viewTicketDetails = readTableData.clickService(service,
                        (i + 1));
               waitForProcessCompletion(10);
                 driver.findElement(By.id("view_btn")).click();
                waitForProcessCompletion(10);

                if (viewTicketDetails.getEmailId()
                        .contains(mailId) && viewTicketDetails
                        .getRunTimeTicketFieldValues("Channel")
                        .contains(channel) && url.contains(viewTicketDetails
                        .getWebsite())) {

                    ticketID = tickets.get(i).substring(tickets.get(i).indexOf("VS"),
                            tickets.get(i).indexOf(service) - 1);
                    System.out.println((i + 1) + " : Channel = " +
                            viewTicketDetails.getRunTimeTicketFieldValues(
                                    "Channel"));
                    checkViewTicketInfo(channel);

                    System.out.println(channel + " Ticket ID: " + ticketID);
                    changeTicketStatus();
                    checkCRMEmailConversation(fileType, srcLang, targetLang, additionalQty, tat,
                             offer);
                    break;
                } else {
                    ticketID = "\n\nEither ticket is Not created or Still" +
                            " waiting for ticket";
                    System.out.println("Ticket Id="+driver.findElement(By.id("header_ticket")).getText());
                }
                waitForProcessCompletion(60);
            }
        }
    }

    private void evaluateCondition(String message, String first,
                                   String second) {

        System.out.print(message + " : " + second);
        if ((first.replace("$", "")).contains(second)) {

            System.out.print("\t Status : Pass\n");
        } else {

            System.out.print("\t Status : [Fail]\n");
            System.out.print("\t Expected : " + second + "\n");
            System.out.print("\t Actual : " + first + "\n");
        }
    }

    private void changeTicketStatus() {

        // Edit a ticket and moved the status into Others
        Edit edit = menus.clickEdit();
        
        edit.selectPaymentType("Full payment");
        edit.selectPaymentMode("Square");
        edit.selectStatus("Others");
        edit.clickUpdateButton();
        waitForProcessCompletion(10);
    }

    private void checkCRMEmailConversation(String fileType, String srcLang, String targetLang,
            boolean additionalQty, boolean tat, boolean offer) {

        System.out.println("\n===========================================");
        System.out.println("Checking Email Conversation");
        System.out.println("===========================================\n");
        emailConversation = menus.clickEmailConversation();
        emailConversation.clickReadMore("Quote");
        if (emailConversation.getServiceDetailsFromEmailHeading(service)) {
            System.out.println(service + " heading is correct");
        } else {
            System.out.println(service + " heading is wrong");
        }
        if (!srcLang.equals(srclanguages[2])) {
            evaluateCondition("Source language", emailConversation
                    .getTicketFieldValuesFromPayment("Source language", false),
                    srcLang);
            evaluateCondition("Target language", emailConversation
                    .getTicketFieldValuesFromPayment("Target language", false),
                    targetLang);
            if(fileType.equals(fileTypes[0])) {

                evaluateCondition("Number of page(s)", emailConversation
                        .getTicketFieldValuesFromPayment("Number of page(s)",
                        false), minute);

                evaluateCondition("Cost per page", emailConversation
                        .getTicketFieldValuesFromPayment("Cost per page",
                        false), "" + bPrice);
            } else {

                evaluateCondition("Minutes", emailConversation
                        .getTicketFieldValuesFromPayment("Minutes", false),
                        minute);
            }

            evaluateCondition("Cost", emailConversation
                    .getTicketFieldValuesFromPayment("Cost",
                   false), "" + (bPrice * min));
            if(offer) {
                evaluateCondition("New year offer", emailConversation
                                .getTicketFieldValuesFromPayment("New year offer", false),
                        "" + offerPr);
            }


            if(tat) {
                evaluateCondition("Expedited service fee", emailConversation
                        .getTicketFieldValuesFromPayment("Expedited service fee",
                        false),"" + exper);
            }
            if (!targetLang.equals(srclanguages[0])) {
                evaluateCondition("Certification fee", emailConversation
                                .getTicketFieldValuesFromPayment("Certification fee", false),
                        "" + certification);
            } else {
                evaluateCondition("Notary", emailConversation
                         .getTicketFieldValuesFromPayment("Notary", false),
                        "" + notaryFee);
                evaluateCondition("Processing fee", emailConversation
                        .getTicketFieldValuesFromPayment("Processing fee", false),
                        "" + notaryProcessFee);
            }

            evaluateCondition("Mailing", emailConversation
                    .getTicketFieldValuesFromPayment("Mailing", false),
                    "" + mailingFee);

            evaluateCondition("Grand total",
                    emailConversation.getTicketFieldValuesFromPayment(
                            "Grand total", false), "" + gtot);
            if (additionalQty) {

                evaluateCondition("Quality check fee",
                        emailConversation.getTicketFieldValuesFromPayment(
                        "Quality check fee", false), "" +
                        (additionalQtyPri * min));
            }

            evaluateCondition("Transaction fee",
                    emailConversation.getTicketFieldValuesFromPayment(
                            "Transaction fee", false), "" + trans);
            evaluateCondition("Order total",
                    emailConversation.getTicketFieldValuesFromPayment(
                            "Order total", false), "" + orderTot);
            /*evaluateCondition("File name", emailConversation
                            .getTicketFieldValuesFromPayment("File name", true),
                    fileName + fileExtention);
            evaluateCondition("Link", emailConversation
                    .getTicketFieldValuesFromPayment("Link", true), "Download");

            evaluateCondition("Special words", emailConversation
                    .getTicketFieldValuesFromPayment("Special words",
                            false), comment);
            if (tat) {

                evaluateCondition("Rush delivery", emailConversation
                        .getTicketFieldValuesFromPayment("Rush delivery",
                                false), "Rush delivery");
            } else {
                evaluateCondition("Standard delivery", emailConversation
                        .getTicketFieldValuesFromPayment("Standard delivery",
                                false), "Standard delivery");
            }
            if (!timeCode.equals(timecodes[2])) {
                evaluateCondition("Time code", emailConversation
                        .getTicketFieldValuesFromPayment("Time code will",
                                false), "Time code will");
            }
            evaluateCondition("Free trial", emailConversation
                    .getTicketFieldValuesFromPayment("free trial",
                            false), "free trial");*/
        } else {

            evaluateCondition("Email Id",
                    emailConversation
                            .getTicketValuesFromPayment("Email Id",
                                    false), mailId);
            evaluateCondition("File type",
                    emailConversation.getTicketValuesFromPayment("File type",
                            false), fileType);
            evaluateCondition("Translate from",
                    emailConversation.getTicketValuesFromPayment
                            ("Translate from", false),
                    srcLang);

            evaluateCondition("Translate to",
                    emailConversation.getTicketValuesFromPayment("Translate " +
                            "to", false), targetLang);

            evaluateCondition("Minutes",
                    emailConversation.getTicketValuesFromPayment
                            ("Minutes", false),
                    minute);

            evaluateCondition("Time code",
                    emailConversation.getTicketValuesFromPayment
                            ("Time code", false),
                    "Yes");

            evaluateCondition("Need transcript",
                    emailConversation.getTicketValuesFromPayment
                            ("Need transcript", false),
                    "Yes");

            evaluateCondition("Notarized",
                    emailConversation.getTicketValuesFromPayment
                            ("Notarized", false),
                    "Yes");
            evaluateCondition("Additional quality check",
                    emailConversation.getTicketValuesFromPayment
                            ("Additional quality check", false),
                    "Yes");

            evaluateCondition("Mailing country",
                    emailConversation.getTicketValuesFromPayment
                            ("Mailing country", false),
                    country);
            evaluateCondition("Physical address",
                    emailConversation.getTicketValuesFromPayment
                            ("Physical address", false),
                    address);

            evaluateCondition("Special words",
                    emailConversation.getTicketValuesFromPayment
                            ("Special words", false), comment
                    );
            /*evaluateCondition("File name",
                    emailConversation.getTicketValuesFromPayment
                            ("File name", true),
                    fileName + fileExtention);
            evaluateCondition("Link",
                    emailConversation.getTicketValuesFromPayment
                            ("Link", true),
                    "Download");*/
        }

        if (!srcLang.equals(srclanguages[1])) {
            if(emailConversation
                    .getParticularHeadingDetailsFromAllMsg(service + " " +
                            "Additional Information")){
                System.out.println("Additional Info Message is present");
            } else {
                System.out.println("Additional Info Message is not present");
            }
        } else {
            if(emailConversation
                    .getParticularHeadingDetailsFromAllMsg(service + " Additional Information")){
                System.out.println("This scenario not have, Additional Info " +
                        "page. But the Message is present");
            }
        }

        if(emailConversation
                .getParticularHeadingDetailsFromAllMsg("Others")){
            System.out.println("Others message is present");
        } else {
            System.out.println("Others message is not present");
        }
        System.out.println("===========================================");
        emailConversation.clickNoActionButton();
        waitForProcessCompletion(5);
        if(emailConversation
                .getParticularHeadingDetailsFromAllMsg("No Action")){
            System.out.println("No Action message is present");
        } else {
            System.out.println("No Action  message is not present");
        }
    }

    private void checkViewTicketInfo(String channel) {

        System.out.println("===========================================");
        System.out.println("Checking View Ticket Details");
        System.out.println("===========================================\n");
        evaluateCondition("Email", viewTicketDetails
                .getEmailId(), mailId);
        evaluateCondition("Websites", url,
                viewTicketDetails.getWebsite());
        evaluateCondition("Channel", viewTicketDetails
                .getRunTimeTicketFieldValues("Channel"), channel);
        evaluateCondition("Service", viewTicketDetails
                .getServiceValues(), service);
        System.out.println("===========================================\n");
    }

    private void testScenario(String fileType, String srcLang, String targetLang,
                              boolean additionalQty, boolean tat, int tier,
                              String channel, boolean offer) throws
            AWTException,
            InterruptedException, IOException {

        raiseTicket(fileType, srcLang, targetLang, additionalQty, tat, tier, offer);
        getCRMCreadential();
        checkCRM(fileType, srcLang, targetLang, additionalQty, tat, tier,
                channel, offer);
    }
}

package basetest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import configbrowser.BrowserType;
import file.FileHelper;
import email.EmailManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ExtentManager;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    private static  WebDriver driver;
    private static   ExtentReports extent = ExtentManager.getInstance();
    private static   ExtentTest scenario;
    private  static ExtentTest testCase;
    private  static String scenarioName ="";
    static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    public void openBrowser(String url){
        driver.navigate().to(url);
    }
    private static String optionaEmailTo="";
    private static String optionaEmailCc="";

    @BeforeMethod
    @Parameters(value = {"browserName","emailTo","emailCc"})
    public static void beforeMethodMethod(String browserName, Method testMethod, ITestContext context,@Optional String emailTo,@Optional String emailCc){
        if(!scenarioName.equalsIgnoreCase(context.getCurrentXmlTest().getName())){
            scenarioName =context.getCurrentXmlTest().getName();
            scenario =extent.createTest(scenarioName);
        }
        optionaEmailTo =emailTo;
        optionaEmailCc=emailCc;
        testCase= scenario.createNode(testMethod.getAnnotation(Test.class).description());
        testCase.assignCategory(scenarioName);
        testCase.assignCategory("<b>TOTALS</b>");

        if (!BrowserType.valueOf(browserName).equals(BrowserType.NONE)){
            setupDriver(BrowserType.valueOf(browserName));
            scenario.assignDevice(browserName);
        }

        testCase.info("Iniciar Automatizacion");

    }

    @AfterMethod
    public void afterMethodMethod(ITestResult result){
        String methodName = result.getMethod().getMethodName();
        String logText = "Test case: "+methodName;
        if(result.getStatus()==ITestResult.FAILURE){
            logText += "Failure";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
            testCase.log(Status.FAIL,m);
        }else if(result.getStatus()==ITestResult.SKIP){
            logText += "Test case: "+methodName + "Skipped";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
            testCase.log(Status.SKIP,m);
        }

        if(driver!=null){
            driver.quit();
        }

    }
    public void createStep(String textInfo){

        testCase.log(Status.PASS, "<pre>"+textInfo+"</pre>");
    }

    public static void  createStep(String description, boolean decision, boolean takeScreenShot){

        String screenshot="";

        screenshot=takeScreenShot? FileHelper.getInstance().takeScreenShotByte(driver) :"";

        testCase= decision&&takeScreenShot?testCase.pass(description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()):testCase;

        testCase= decision&&!takeScreenShot?testCase.pass(description):testCase;

        testCase= !decision&&takeScreenShot?testCase.fail( description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()): testCase;

        testCase= !decision&&!takeScreenShot?testCase.fail( description):testCase;
    }
    @AfterTest

    public void afterTestMethod(){
        extent.flush();

        EmailManager.getInstance().sendEmail(getOptionaEmailTo(),getEmailCc());

        if(driver!=null){
            driver.quit();
        }

    }


    public static WebDriver getBrowser(BrowserType browserType) {

        try{
            switch(browserType){
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    return driver;
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    return driver;

                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    return driver;
                case IEXPLORER:
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    return driver;

                default:

            }
        }
        catch(Exception e){
            logger.error("Error Class AccionManager method waitWhileSystemIsBusy", e);
        }

        return null;
    }
    public static void setupDriver(BrowserType browserName){
        getBrowser(browserName);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }
    public static String getEmailCc() {
        return optionaEmailCc;
    }

    public static String getOptionaEmailTo() {
        return optionaEmailTo;
    }

    public static WebDriver getDriver() {
        return driver;
    }
}

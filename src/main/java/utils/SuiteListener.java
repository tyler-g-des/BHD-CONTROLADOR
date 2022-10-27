package utils;

import basetest.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static basetest.BaseTest.getDriver;


public class SuiteListener implements ITestListener, IAnnotationTransformer {

    static Logger logger = LoggerFactory.getLogger(SuiteListener.class);
     BaseTest baseTest = new BaseTest();

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String fileName = System.getProperty("user.dir") + File.separator + "screenshots"+ File.separator+ iTestResult.getMethod().getMethodName();
        File file = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(file, new File(fileName+".png"));
        } catch (IOException e) {
            logger.error("Error Class SuiteListener method onTestFailure", e);
        }

    }

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method){
        iTestAnnotation.setRetryAnalyzer(utils.RetryAnalyser.class);
    }
}

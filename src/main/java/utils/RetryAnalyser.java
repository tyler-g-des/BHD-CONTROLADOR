package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyser implements IRetryAnalyzer {
    int count = 0;
    int retryCount = 1;
    @Override
    public boolean retry(ITestResult iTestResult){
        boolean estado=false;
        while(count<retryCount){
            count++;
            estado=true;

        }
        return estado;
    }
}

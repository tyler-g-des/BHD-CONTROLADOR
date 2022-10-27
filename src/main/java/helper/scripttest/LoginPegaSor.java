package scripttest;

import pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import basetest.BaseTest;

public class LoginPegaSor extends BasePage {

    By loginUser = By.xpath("//input[@id='txtUserID']");
    By loginPassword = By.xpath("//input[@id='txtPassword']");
    By btnLogin = By.xpath("//button[@id='sub']");
    By bhdImageLogo = By.xpath("//img[@title='BHD Logo']");
    String urlPega = "https://bhd-bhdcrm-stg1.pegacloud.net/prweb/app/BHDCLM/7GW28EW-6-ZdvsEZ4h-AINKSMtlwResh*/!STANDARD";



    public LoginPegaSor(WebDriver driver) { super(driver);   }

    public boolean loginPegaSor(String user, String password){

        boolean status = false;
        BaseTest baseTest = new BaseTest();
        baseTest.openBrowser(urlPega);
        writeTextOn(loginUser, user);
        writeTextOn(loginPassword,password);
        clickOn(btnLogin);
        status = isElementPresent(bhdImageLogo);

    return status;
}

}

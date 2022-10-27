package helper.scripttest;

import basetest.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.BasePage;
import utils.Route;

import java.io.IOException;
import java.util.Properties;

import static utils.ReadProperty.readPropertiesFile;

public class LoginT24 extends BasePage {
    public LoginT24(WebDriver webDriver) throws IOException { super(webDriver);   }
    static Logger logger = LoggerFactory.getLogger(LoginT24.class);

    By userInput = By.xpath("//input[@id='userId']");
    By passwordInput = By.xpath("//input[@id='password']");
    By singInButton = By.xpath("//input[@id='sign-in']");
    By imageLoginT24 = By.xpath("//input[@id='BUT_--HOME--']");

    Properties prop = readPropertiesFile(Route.getInstance().getUrlenviromentsProperties());

    String urlT24 = prop.getProperty("urlT24");
    /**
     * login de T24
     * @param  user usuario T24
     * @param password clave T24
     * @return boolean
     * @author Juan Castro
     * @since 13/10/2022
     */
    public boolean loginT24(String user, String password){
        BaseTest baseTest = new BaseTest();

        try{
            baseTest.openBrowser(urlT24);
            writeTextOn(userInput,user);
            writeTextOn(passwordInput,password);
            clickOn(singInButton);
            return isElementPresent(imageLoginT24);
        }catch (NoSuchElementException e){
            logger.error("Error Class LoginT24 in method loginT24", e);
        }
        return false;
    }

}

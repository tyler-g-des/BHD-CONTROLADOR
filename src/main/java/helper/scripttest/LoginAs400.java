package helper.scripttest;

import helper.as400.AccionManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.BasePage;
import utils.Route;

import java.util.Properties;

import static utils.ReadProperty.readPropertiesFile;

public class LoginAs400 extends BasePage {

    public LoginAs400(WebDriver webDriver) {super(webDriver);    }
    private boolean status=false;
    private static AccionManager as400;


    static Logger logger = LoggerFactory.getLogger(LoginAs400.class);

    By principalScreenLabel = By.xpath(createXpahByText("Inicio de Ses"));
    By userInput = By.id("1");
    By sesionVerifyInput = By.id("1");
    By passwordInput = By.xpath("2");
    By messageDisplayScreenLabel = By.xpath(createXpahByText("Visualizar Mensajes de Programa"));
    By screenRecoveryWorkInteractive = By.xpath(createXpahByText("Intentar Recuperar Trabajo Interactivo"));
    By screenCloseSesion = By.xpath(createXpahByText("Finalizar sesi"));
    By principalMenu = By.xpath(createXpahByText("Menu Principal"));


    Properties prop = readPropertiesFile(Route.getInstance().getUrlenviromentsProperties());

    String hostAs400 = prop.getProperty("hostAs400");
    /**
     * login de As400
     * @param  user usuario As400
     * @param password clave As400
     * @return boolean
     * @author Juan Castro
     * @since 13/10/2022
     */
    public boolean login(String user, String password) {

         boolean resumeScreen=false;
        try {

            status = false;

            AccionManager.navigateToAs400(hostAs400);
            isElementPresent(principalScreenLabel);
            as400.setObjectValue(driver.findElement(userInput), user);
            as400.setObjectValue(driver.findElement(passwordInput), password);
            AccionManager.sendKeyEnter();
            as400.waitWhileSystemIsBusy();
            resumeScreen = verifyResumeScreen();

            if (resumeScreen) {
                login(user, password);

            } else if (isElementPresent(messageDisplayScreenLabel)) {

                AccionManager.sendKeyEnter();
                as400.waitWhileSystemIsBusy();
                isElementPresent(principalMenu);
                status = true;

            }


        } catch (Exception e) {
            logger.error("Error Class LoginAs400 in method login", e);
        }
        return status;

    }

    private boolean verifyResumeScreen() {
        status = false;

        if (isElementPresent(screenRecoveryWorkInteractive)) {

            as400.setObjectValue(driver.findElement(sesionVerifyInput), "90");
            AccionManager.sendKeyEnter();
            as400.waitWhileSystemIsBusy();
            status = true;
            if (isElementPresent(screenCloseSesion)) {

                AccionManager.sendKeyEnter();
                as400.waitWhileSystemIsBusy();
            }

        }

        return status;
    }
}

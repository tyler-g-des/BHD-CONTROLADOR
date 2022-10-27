package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static basetest.BaseTest.getDriver;


public class BasePage {
    protected WebDriver driver;
    static Logger logger = LoggerFactory.getLogger(BasePage.class);
    public BasePage(WebDriver webDriver){
        driver = webDriver;
    }

    WebDriverWait wait = new WebDriverWait(getDriver(), 30);
    /**
     * Escribe un texto en el elemento seleccionado
     * @param  element
     * @param  text
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void writeTextOn(By element, String text){
        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(element)));
            driver.findElement(element).clear();
            driver.findElement(element).sendKeys(text);
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method writeTextOn", e);
        }


    }
    /**
     * Realiza la funcion click en la pagina
     * @param  element
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void clickOn(By element){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(element)));
            driver.findElement(element).click();
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method clickOn", e);
        }


    }
    /**
     * Verifica si un elemento esta presenta en la pagina
     * @param  element
     * @author Juan Castro
     * @since 13/10/2022
     */
    public  boolean isElementPresent(By element){
        boolean status = false;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            status = true;
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method isElementPresent", e);
        }

        return status;
    }
    public void refreshBrowser(){
        driver.navigate().refresh();
    }
    /**
     * ejecuta funciones javaScript desde selenium
     * @param  script
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void executeJavaScript(String script){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(script);
    }

    /**
     * Obtiene el texto de un elemento
     * @param  element
     * @author Juan Castro
     * @since 13/10/2022
     */
    public String getText(By element){
        String text = null;
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
           text =  driver.findElement(element).getText();

        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method getText", e);
        }
        return text;
    }
    /**
     * Selecciona un elemento en base al texto
     * @param  text
     * @author Juan Castro
     * @since 13/10/2022
     */
    public boolean verifyTitile(String text){
        Boolean status = false;
        try{
            status =  driver.getTitle().equalsIgnoreCase(text);
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method verifyTitile", e);
        }
            return status;
    }
    /**
     * Realiza un Switch entre ventanas del navegador
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void switchToWindow(){
        for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
            driver.manage().window().maximize();
        }

    }
    /**
     * realiza scroll sobre un elemento
     * @param  element
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void scrollToElement(By element){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(element));
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method scrollToElement", e);
        }

    }
    /**
     * Resalta un web element
     * @param  element
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void highlightObjetoOn(By element){
        String scriptHighligh = "arguments[0].setAttribute('style','background: default; border: 0px solid dafault;');";
        try{
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript(scriptHighligh,element);
        }catch (NoSuchElementException e){
            logger.error("Error Class BasePage in method highlightObjetoOn", e);
        }
    }
    /**
     * Selecciona un elemento en base al valor
     * @param  element
     * @param value
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void selectByValue(By element, String value){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            Select selectTypeOfSearch = new Select(driver.findElement(element));
            selectTypeOfSearch.selectByValue(value);
        }catch(NoSuchElementException e){
            logger.error("Error Class BasePage in method selectByValue", e);
        }
    }
    /**
     * Selecciona un elemento en base al texto
     * @param  element
     * @param index
     * @author Juan Castro
     * @since 13/10/2022
     */
    public void selectByIndex(By element, int index){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            Select selectTypeOfSearch = new Select(driver.findElement(element));
            selectTypeOfSearch.selectByIndex(index);
        }catch(NoSuchElementException e){
            logger.error("Error Class BasePage in method selectByIndex", e);
        }
    }
    /**
     * genera un xpath en base al texto
     * @param  text recibe un texto
     * @return String
     * @author Juan Castro
     * @since 13/10/2022
     */
    public String createXpahByText(String text) {
        String xpath = "";
          try{
               xpath = "//*[contains(text(),'" + text + "')]";
          }
        catch (Exception e){
            logger.error("Error Class BasePage in method CreateXpahByText", e);
        }
       return xpath;
    }



}

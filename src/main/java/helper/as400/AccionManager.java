package helper.as400;


import basetest.BaseTest;
import com.verges.term2web.TerminalInterface;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import pages.BasePage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public class AccionManager  extends BasePage{

    private static TerminalInterface as400;
    private static int port=992;
    private static String basePath=System.getProperty("user.dir");
    private static String fileName="\\Term2Web.html";
    private static String pagePath= basePath + fileName;
    private static String color="";


    static Logger logger = LoggerFactory.getLogger(AccionManager.class);
    private  static BaseTest baseTest = new BaseTest();

    public AccionManager(WebDriver driver) {
        super(driver);
    }
    /**
     * Este metodo abre la coneccion con As400 y renderiza el aplicativo en el navegador
     * @param  host ambiente de AS400
     * @return none
     * @author Juan Castro
     * @since 16/10/2022
     */
    public static void navigateToAs400( String host)  {

        as400=TerminalInterface.getInstance(host);

        saveStyle();
        saveScript();

        as400.connectSecureTLS(port);
        as400.connect();

        as400.waitWhileSystemIsBusy();
        as400.generateScreenHtml(basePath + fileName);
        baseTest.openBrowser(basePath + fileName);


    }

    private static void saveStyle() {
        String path = basePath + "\\Term2Web.css";
        String style;
        File file = new File(path);

        cleanUp(file.toPath());

        while (!file.exists()) {
            try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                if (color.equals("")) {
                    style = "body\n{\tbackground-color: black;\n\ttext-align: center;\n\tfont-family: courier;\n}\n\npre\n{\n\tmargin-top: 7px;\n\tmargin-bottom: 7px;\n\tmargin-right: 0px;\n\tmargin-left: 0px;\n\tfont-size: 20px;\n\theight: 16px;\n\tcolor: #00FF04;\n}\n\ninput\n{\n\tbackground-color: black;\n\tborder-color: #00FF04;\n\tcolor: #00FF04;\n\tborder-top-style: none;\n\tborder-right-style: none;\n\tborder-bottom-style: solid;\n\tborder-left-style: none;\n\toutline: none;\n\tfont-size: 11.7px;\n\tfont-weight: bold;\n\tfont-kerning: none;\n}\n\ntextarea\n{\n\tbackground-color: black;\n\tborder-color: #00FF04;\n\tcolor: #00FF04;\n\tborder-top-style: none;\n\tborder-right-style: none;\n\tborder-bottom-style: solid;\n\tborder-left-style: none;\n\toutline: none;\n\tfont-size: 15px;\n\tfont-weight: bold;\n\tresize: none;\n}";
                } else {
                    style = "body\n{\tbackground-color: black;\n\ttext-align: center;\n\tfont-family: courier;\n}\n\npre\n{\n\tmargin-top: 7px;\n\tmargin-bottom: 7px;\n\tmargin-right: 0px;\n\tmargin-left: 0px;\n\tfont-size: 20px;\n\theight: 16px;\n\tcolor: " + color + ";\n}\n\ninput\n{\n\tbackground-color: black;\n\tborder-color: " + color + ";\n\tcolor: " + color + ";\n\tborder-top-style: none;\n\tborder-right-style: none;\n\tborder-bottom-style: solid;\n\tborder-left-style: none;\n\toutline: none;\n\tfont-size: 11.7px;\n\tfont-weight: bold;\n\tfont-kerning: none;\n}\n\ntextarea\n{\n\tbackground-color: black;\n\tborder-color: " + color + ";\n\tcolor: " + color + ";\n\tborder-top-style: none;\n\tborder-right-style: none;\n\tborder-bottom-style: solid;\n\tborder-left-style: none;\n\toutline: none;\n\tfont-size: 15px;\n\tfont-weight: bold;\n\tresize: none;\n}";
                }

                br.write(style);
                br.flush();

            } catch (Exception e) {
                logger.error("Error Class AccionManager in method saveStyle", e);


            }
        }

    }
    public static void cleanUp(Path path){
        try {
            Files.deleteIfExists(path);
        } catch (Exception e){
            logger.error("Error Class AccionManager in method cleanUp", e);

        }

    }

    private static void saveScript() {
        String path = basePath + "\\Term2Web.js";
        String script = "function SetReloadPage()\n{\n\ttimeoutID = setTimeout('ReloadPage()', 3000);\n}\n\nfunction ReloadPage()\n{\n\tUnloadData();\n\tlocation.reload();\n\tReloadData();\n}\nfunction UnloadData()\n{\n\tfield1 = document.getElementById(\"1\").value;\n\tconsole.log(\"Unloading Data: \" + field1);\n\tlocalStorage.setItem(\"field1\", field1);\n}\n\nfunction ReloadData()\n{\n\tfield1 = localStorage.getItem(\"field1\");\n\tconsole.log(\"Reloading Data\");\n\tdocument.getElementById(\"2\").value = field1;\n}\n\nfunction GetTextWidth(text, font)\n{\n\tvar canvas = GetTextWidth.canvas || (GetTextWidth.canvas = document.createElement(\"canvas\"));\n\tvar context = canvas.getContext(\"2d\");\n\tcontext.font = font;\n\tvar metrics = context.measureText(text);\n\n\treturn metrics.width;\n}\n\nfunction SetTextWidth(id)\n{\n\tvar el = document.getElementById(id);\n\tvar tex = el.value;\n\tvar font = window.getComputedStyle(el, null).font;\n\tvar pix = window.getComputedStyle(el, null).fontSize;\n\n\tfor(i = 0; i < el.maxLength; i++)\n\t{\n\n\t\ttex = tex.substring(0, i) + \"W\" + tex.substring(i + 1);\n\t}\n\n\tvar canvas = GetTextWidth.canvas || (GetTextWidth.canvas = document.createElement(\"canvas\"));\n\tvar context = canvas.getContext(\"2d\");\n\tcontext.font = font;\n\tvar metrics = context.measureText(tex);\n\n\tconsole.log(Math.ceil(metrics.width));\n\n\tel.style = \"width: \" + Math.ceil(metrics.width) + \"px\";\n}function highlight(text) {\n var inputText = document.getElementById(\"inputText\");  var innerHTML = inputText.innerHTML; var index = innerHTML.indexOf(text); if (index >= 0) {   innerHTML = innerHTML.substring(0,index) + \"<span class='highlight'style='border: 2px solid rgb(253, 1, 1);'>\" + innerHTML.substring(index,index+text.length) + \"</span>\" + innerHTML.substring(index + text.length); inputText.innerHTML = innerHTML;}\n}\nfunction setId(){\n elements = document.getElementsByTagName(\"b\");for(i=0; i<elements.length; i++){index = parseInt(i);elements[index].setAttribute(\"id\", \"b-\"+index);}}\n";
        File file = new File(path);

        while (!file.exists()) {
            try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {

                br.write(script);
                br.flush();

            }
            catch(IOException e){
                logger.error("Error Class AccionManager in method saveScript", e);
            }
        }
    }

    public static String getHtmlPath() {
        return pagePath;
    }

    public static String getScreenAsString() {
        return as400.getScreenAsString();
    }

    public void setObjectValue(WebElement we, String value) {
        try {

            int objectId = Integer.parseInt(we.getAttribute("id"));
            as400.setFieldValue(objectId, value);
            refreshScreen();


        }catch (Exception e){
            logger.error("Error Class AccionManager method getHtmlPath", e);
        }
    }


    public void refreshScreen() {

        as400.generateScreenHtml(pagePath);
        refreshBrowser();


    }


    public void loaderScreen(){

        String startLoader = "function startLoader(){var div = document.createElement('div');div.setAttribute('id','loader'); var pre = document.getElementsByTagName('pre')[5];pre.setAttribute('class','test'); pre.appendChild(div); div.style.border = '10px solid #070707'; div.style.borderRadius = '50%'; div.style.borderTop = '10px solid #03801e'; div.style.width = '50px'; div.style.height = '50px'; div.style.animation = 'spin 2s linear infinite';div.style.margin = 'auto'; div.animate([{ transform: \'rotate(0deg)\'},{ transform: \'rotate(360deg)\' }],{duration: 1000,iterations:Infinity})} startLoader()";
        executeJavaScript(startLoader);
    }

    public void waitWhileSystemIsBusy() {
        try {
            loaderScreen();
            TimeUnit.SECONDS.sleep(1);
            as400.waitWhileSystemIsBusy();
            refreshScreen();

            /***refactor***/


        }catch (InterruptedException e) {
            /***refactor***/
            logger.error("Error Class AccionManager method waitWhileSystemIsBusy", e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

    }

    public static void sendKeyEnter() {

        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDENTER);
    }

    public void sendKeyClear() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDCLEAR);
    }

    public void sendKeyF01() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF1);
    }

    public void sendKeyF02() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF2);
    }

    public void sendKeyF03() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF3);
    }

    public void sendKeyF04() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF4);
    }

    public void sendKeyF05() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF5);
    }

    public void sendKeyF06() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF6);
    }

    public void sendKeyF07() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF7);
    }

    public void sendKeyF08() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF8);
    }

    public void sendKeyF09() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF9);
    }

    public void sendKeyF10() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF10);
    }

    public void sendKeyF11() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF11);
    }

    public void sendKeyF12() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF12);
    }

    public void sendKeyF13() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF13);
    }

    public void sendKeyF14() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF14);
    }

    public void sendKeyF15() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF15);
    }

    public void sendKeyF16() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF16);
    }

    public void sendKeyF17() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF17);
    }

    public void sendKeyF18() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF18);
    }

    public void sendKeyF19() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF19);
    }

    public void sendKeyF20() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF20);
    }

    public void sendKeyF21() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF21);
    }

    public void sendKeyF22() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF22);
    }

    public void sendKeyF23() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF23);
    }

    public void sendKeyF24() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPF24);
    }

    public void sendKeyHelp() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDHELP);
    }

    public void sendKeyRollDown() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDROLLDOWN);
    }

    public void sendKeyRollLeft() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDROLLLEFT);
    }

    public void sendKeyRollRight() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDROLLRIGHT);
    }

    public void sendKeyRollUP() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDROLLUP);
    }

    public static void sendKeyPrint() {
        as400.sendAidKeyToTerminal(ConstantAs400Key.AIDPRINT);
    }
}

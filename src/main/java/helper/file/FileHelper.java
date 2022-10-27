package file;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Route;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class FileHelper {

    private static FileHelper instance=null;

    public static FileHelper getInstance(){
        if(instance==null){
            instance=new FileHelper();
        }
        return instance;
    }
    static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    /** @implNote  Metodo leer el contenido de un archivo de plano en una ruta especifica
     * @param route, la direccion donde se encuentra el archivo ubicado
     * @return String
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public String readPlaneText(String route){
        StringBuilder stringBuilder=new StringBuilder();
        String ls = System.getProperty("line.separator");
        try (BufferedReader br = new BufferedReader(new FileReader(route))) {
            String text = "";
            while ((text = br.readLine()) != null) {
                stringBuilder.append(text);
                stringBuilder.append(ls);
            }

        } catch (IOException e) {
            logger.error("Error Class FileHelper in method readPlaneText ", e);
        }
        return stringBuilder.toString();

    }
    /** @implNote  Metodo leer el contenido de un archivo dentro del jar, ejem (.txt,.xml,.json, etc)
     * @param routeFile Ruta del archivo dentro del jar
     * @return String
     * @author FERNANDO PINEDA
     * @since 04/09/2022
     */

    public String readPlaneTextInsideJar(String routeFile){

        StringBuilder stringBuilder=new StringBuilder();
        String ls = System.getProperty("line.separator");
        InputStreamReader streamReader = new InputStreamReader(getFileInsideJar(routeFile));

        try (BufferedReader br = new BufferedReader(streamReader)){
            String text = "";
            while ((text = br.readLine()) != null) {
                stringBuilder.append(text);
                stringBuilder.append(ls);
            }

        } catch (IOException e) {
            logger.error("Error Class FileHelper in method readPlanTexInsideJar ", e);
        }
        return stringBuilder.toString();

    }
    /** @implNote  Metodo para crear archivo plano dentro del proyecto. El archivo se crea en la carpeta de resouces del proyecto
     * si el archivo existe lo sustituye siempre que no este en uso o abierto.
     * @param name nombre que se le desea dar al archivo
     * @param body contenido que que tendra el archivo.
     * @param extension es la extension del archivo ejem (.txt,.xml,.json,.js ...)
     * @return File
     * @author FERNANDO PINEDA
     * @since 04/09/2022
     */

    public File createFile(String name, String body, String extension){

        File homeDir=new File(Route.getInstance().getPath()+"\\src\\main\\resources\\"+name+extension);


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(homeDir), StandardCharsets.UTF_8))) {

            writer.write(body);

        }  catch (IOException e) {
            logger.error("Error Class FileHelper in method createFile ", e);
        }

        return homeDir;

    }


    /** @implNote  Metodo crear carpeta de reportes de (Extent Report) en el proyecto local si no existe
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void createReportFolder(){
        try {
            File homeDir = new File(Route.getInstance().getPath());
            File dir = new File(homeDir, "Reports");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Unable to create " + dir.getAbsolutePath());
            }
        }
        catch (IOException e){
            logger.error("Error Class FileHelper in method createFolder ", e);
        }
    }

    /** @implNote Metodo crear la nomenclarura del nombre de un archivo
     * @param constant es una valor predeterminado que se coloca al principio del nombre
     * @param extension es la extension del archivo. ejem (.xml , .txt , .json, etc...)
     * @return String
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public String createFileName(String constant,String extension){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;

        return   constant+" "+dateFormat.format(date) + extension;

    }

    /** @implNote Metodo para tomar una captura de pantalla al browser ejecutado y devolverla en un string convertido la imagen en base 64
     * @return String
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */

    public String takeScreenShotByte(WebDriver driver) {
        String destination="";
        try {

            File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

            byte[] imageByte= IOUtils.toByteArray(new FileInputStream(file));

            destination= Base64.getEncoder().encodeToString(imageByte);

        } catch (Exception e) {
            logger.error("Error Class FileHelper in method takeScreenShotByte ", e);
        }

        return destination;

    }

    /** @implNote Metodo para buscar un archivo dentro del jar
     * @param route es la ruta relativa dentro del jar donde se encuentra el archivo
     * @return InputStream
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public InputStream getFileInsideJar(String route){
        return getClass().getResourceAsStream(route);
    }


}

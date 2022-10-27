package email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExtentManager;
import utils.Route;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailManager {

    private static EmailManager instance=null;

    static Logger logger = LoggerFactory.getLogger(EmailManager.class);

    public static EmailManager getInstance(){
        if(instance==null){
            instance=new EmailManager();
        }
        return instance;
    }
    private String bodyEmail="";
    String emailAutomatization="";
    String header="";
    String footer="";
    String domain1="";
    String domain2="";
    String subject="";
    Properties properties;
    MimeMessage message;
    Session session;
    Address[] addresses;

    /** @implNote  Metodo para cargar las propiedades del correo en la clase
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void loadPropertyEmail(){
        try {
            properties=new Properties();
            properties.load(file.FileHelper.getInstance().getFileInsideJar(Route.getInstance().getEmailpropertyroute()));

            emailAutomatization=properties.getProperty("mail.smtp.correoQuienEnvia");
            header=properties.getProperty("mail.smtp.header");
            footer=properties.getProperty("mail.smtp.footer");
            domain1=properties.getProperty("mail.smtp.domain1");
            domain2=properties.getProperty("mail.smtp.domain2");
            subject=properties.getProperty("mail.smtp.subject");


        } catch (IOException e) {
            logger.error("Error Class ExtentManager in method loadPropertyEmail",e);
        }


    }
    /** @implNote  Metodo para inicializar la Sesion de correo
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void createSession(){
        session=Session.getInstance(properties, getInstance().setAuthentication());
    }

    /** @implNote  Metodo para inicializar mensaje y realizar autenticacion con el correo institucional.
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void initializeMineMessage(){
        message=new MimeMessage(session);
    }
    public Authenticator setAuthentication(){
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAutomatization,"");
            }
        };
    }
    /** @implNote  Metodo para validar correo institucional
     * @param emails correo al cual se desea validar que cumpla correctamente con la sintaxis de correo de la institycion
     * @author FERNANDO PINEDA
     * @return List
     * @since 03/08/2022
     */

    public List <Address> emailsValidator(String emails){


        String[] emailsArray=emails.replace(";",",").replace(" ","").split(",");

        List <Address> emailListTemp=new ArrayList<>();

        String regex="([a-zA-Z]+_+[a-zA-Z]+@("+domain1+"|"+domain2+")+\\.com.do)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        try {
            for (int i = 0; i < emailsArray.length ; i++) {
                matcher=pattern.matcher(emailsArray[i].toLowerCase());

                if (matcher.find()){
                    emailListTemp.add(new InternetAddress(emailsArray[i]));
                }
                else{
                    logger.info("Correo no cumple con sintaxis valida: [{}]",emailsArray[i]);
                }
            }
            addresses= new Address[emailListTemp.size()];
            for (int i = 0; i <emailListTemp.size() ; i++) {
                addresses[i]=emailListTemp.get(i);
            }


        } catch (AddressException e) {
            logger.error("Error Class ExtentManager in method emailsValidator",e);
        }

        return emailListTemp;
    }
    /** @implNote  Metodo para inicializar lista de correos validos para las personas a quien va dirigido el mensaje.
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void createEmailToRecipient(String emails){

        try {
            emailsValidator(emails);
            if(addresses.length>0) {
                message.setRecipients(Message.RecipientType.TO, addresses);
            }
        } catch (MessagingException e) {
            logger.error("Error Class ExtentManager in method createEmailToRecipient",e);
        }
    }
    /** @implNote  Metodo para inicializar lista de correos validos para las personas en copia que del mensaje.
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void createEmailCcRecipient(String emails){

        try {
            emailsValidator(emails);

            if (addresses.length>0) {
                message.setRecipients(Message.RecipientType.CC, addresses);
            }
        } catch (MessagingException e) {
            logger.error("Error Class ExtentManager in method createEmailCcRecipient",e);
        }
    }

    /** @implNote  Metodo para inicializar el contenido del correo (asunto, cuerpo y adjuntos)
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */

    public void createBody(){

        try {
            message.setFrom(new InternetAddress(emailAutomatization));
            message.setSubject(subject+" - "+Route.getInstance().getProjectName());

            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setHeader("Content-Type","text/html");

            if (bodyEmail!=null && !bodyEmail.isEmpty()) {
                messageBodyPart.setContent(header + bodyEmail + footer, "text/html;charset=UTF-8");
            }
            else {
                messageBodyPart.setContent(header+footer, "text/html;charset=UTF-8");
            }
            multipart.addBodyPart(messageBodyPart);

            BodyPart reportAttachBodyPart = new MimeBodyPart();
            reportAttachBodyPart.setDataHandler(attactReport());
            reportAttachBodyPart.setFileName(ExtentManager.getReportName());
            multipart.addBodyPart(reportAttachBodyPart);

            message.setContent(multipart);

        } catch (MessagingException e) {
            logger.error("Error Class ExtentManager in method createBody",e);
        }
    }
    /** @implNote  Metodo para crear el documento adjunto del ultimo reporte ejecutado
     * @return DataHandler
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */

    public DataHandler attactReport(){
        File file= new File(ExtentManager.getReportRoute());
        DataSource source;
        if (file.exists()){
            source = new FileDataSource(file);

            return new DataHandler(source);
        }
        return null;
    }
    /** @implNote  Metodo para enviar correos a traves de una lista a quien va dirigido y quien van en copia
     * @param emailTo es la lista de correo a quien va dirigido el mensaje
     * @param emailCc es la lista de correo a quien se colocara en copia del mensaje
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void sendEmail(String emailTo,String emailCc){
        try {

            if (emailTo!=null && !emailTo.isEmpty()){
                loadPropertyEmail();
                createSession();
                initializeMineMessage();
                createEmailToRecipient(emailTo);
                if (emailCc!=null && !emailCc.isEmpty()){
                    createEmailCcRecipient(emailCc);
                }
                createBody();
                Transport.send(message);
            }

        } catch (MessagingException e) {
            logger.error("Error Class ExtentManager in method sendEmail",e);
        }
    }


    /** @implNote  Metodo para definir un texto o mensaje dentro del correo
     * @param bodyEmail es el contenido que se desea colocar en el correo
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public void setBodyEmail(String bodyEmail) {
        this.bodyEmail = bodyEmail;
    }
}

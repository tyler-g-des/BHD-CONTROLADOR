package webservice;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WebServiceHelper {


    private StopWatch stopWatch=new StopWatch();

    private static WebServiceHelper instance =null;

    private HttpURLConnection connection=null;

    public static WebServiceHelper getInstance(){
        if (instance ==null){
            instance =new WebServiceHelper();
        }
        return instance;
    }

    static Logger logger = LoggerFactory.getLogger(WebServiceHelper.class);

    public StopWatch getStopWatch() {
        return stopWatch;
    }
    @AfterClass
    public void stopWatch(){
        stopWatch.stop();
    }


    public HttpURLConnection getWsResponse(String serviceUrl, Map<String,String> headers, String metodo, String body, int timeout){
        try {
            if(connection!=null) {
                connection.disconnect();
            }

            URL url = new URL(serviceUrl);

            stopWatch=new StopWatch();
            stopWatch.start();

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(metodo);
            connection.setConnectTimeout(timeout*1000);
            connection.setReadTimeout(timeout*1000);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {

                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (!body.equals("")) {
                connection.setDoOutput(true);
                PrintStream ps = new PrintStream(connection.getOutputStream());
                ps.println(body);
                ps.close();
            }

            connection.disconnect();

        }

        catch (IOException e) {
            logger.error("Error WebServiceHelper SqlServer in method getWsResponse ", e);
        }

        return connection;
    }

    /**@i
     * Método que convierte el json de respuesta de un StringBuffer
     * param HttpURLConnection connection
     * @return StringBuffer
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */

    public String getJsonContent(HttpURLConnection connection){
        StringBuilder stringBuilder=new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {

            BufferedReader reader;

            String line="";

            if(connection!=null ) {

                if (connection.getResponseCode()==200) {
                    reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                }
                else
                {
                    reader = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                }
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

            }

        }catch (IOException e) {
            logger.error("Error WebServiceHelper SqlServer in method getJsonContent ", e);
        }

        return stringBuilder.toString();

    }
}

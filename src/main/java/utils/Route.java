package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Route {

    private static Route instance=null;

    public static Route getInstance(){
        if(instance==null){
            instance=new Route();
        }
        return instance;
    }
    private static final String DATABASEPROPERTYROUTE ="/properties/database/";
    private static final String EMAILPROPERTYROUTE ="/properties/email/";
    private static final String EXTENTREPORTCONFIGROUTE ="/configfile/extentreport/";
    private static final String URLENVIROMENTS = "/properties/urlEnviroment.properties";


    public String getPath(){
        return System.getProperty("user.dir");
    }
    public String getProjectName(){
        Path path = Paths.get(getPath());
        return path.getFileName().toString();

    }

    public String getRouteJavaScriptReport(){
        return EXTENTREPORTCONFIGROUTE +"ExtentReport.js";
    }
    public String getRouteCssReport(){
        return EXTENTREPORTCONFIGROUTE +"ExtentReport.css";
    }
    public String getRouteXmlReport(){
        return EXTENTREPORTCONFIGROUTE +"ExtentReport.xml";
    }
    public String getRouteReportFolder(){
        return getPath()+"\\Reports\\";
    }
    public String getBhdasdt1property(){

        return DATABASEPROPERTYROUTE +"propiedadBaseDatosBHDASDT1.properties";
    }
    public String getPadronJceproperty(){
        return DATABASEPROPERTYROUTE +"propiedadBaseDatosSQLSERVPadronJCE.properties";
    }
    public String getUrlenviromentsProperties(){return URLENVIROMENTS ;}

    public String getOffertMbp(){
        return DATABASEPROPERTYROUTE +"propiedadBaseDatosOracleOFFERS_MBP.properties";
    }

    public String getEmailpropertyroute(){
        return EMAILPROPERTYROUTE+"email.properties";
    }
}

package utils;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import file.FileHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExtentManager {
    static Logger logger = LoggerFactory.getLogger(ExtentManager.class);
    private static ExtentReports extent;
    private  ExtentManager(){}

    public static ExtentReports getInstance(){
        if(extent == null) {
            createInstance();
        }

        return extent;

    }

    private static String reportRoute="";
    private static String reportName="";

    /** @implNote  Metodo para inicializar extent report
     * @return ExtentReports
     * @author FERNANDO PINEDA
     * @since 03/08/2022
     */
    public static ExtentReports createInstance() {
        try {
            extent = new ExtentReports();
            FileHelper.getInstance().createReportFolder();
            reportName=FileHelper.getInstance().createFileName(Route.getInstance().getProjectName(), ".html");
            reportRoute=Route.getInstance().getRouteReportFolder() +reportName;
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportRoute);
            sparkReporter.config().setEncoding(String.valueOf(StandardCharsets.UTF_8));
            sparkReporter.config().setDocumentTitle("Reportes de Automatizcaion");
            sparkReporter.config().setReportName(Route.getInstance().getProjectName());
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setCss(FileHelper.getInstance().readPlaneTextInsideJar(Route.getInstance().getRouteCssReport()));
            sparkReporter.config().setJs(FileHelper.getInstance().readPlaneTextInsideJar(Route.getInstance().getRouteJavaScriptReport()).replace("projectName", Route.getInstance().getProjectName()));
            String xml=FileHelper.getInstance().readPlaneTextInsideJar(Route.getInstance().getRouteXmlReport());
            File xmlFile=FileHelper.getInstance().createFile("ExtentReport",
                    xml,
                    ".xml"
            );
            extent.setSystemInfo("USERNAME", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            sparkReporter.loadXMLConfig(xmlFile);

            extent.attachReporter(sparkReporter);
            extent.setAnalysisStrategy(AnalysisStrategy.SUITE);

        } catch (IOException e) {
            logger.error("Error Class ExtentManager in method createInstance", e);
        }
        return extent;
    }

    /** @implNote  Metodo para obtener html del dashboard del reporte creado
     * @return String
     * @author FERNANDO PINEDA
     * @since 10/04/2022
     */
    public static String getDashboardHtmlFromReport(){
        String resultDashboard="";

        try {
            File file = new File(reportRoute);

            Document document = Jsoup.parse(file, "UTF-8");

            Elements elements = document.getElementsByTag("script");
            String scriptSrc = elements.get(3).toString();

            String scriptResult = elements.get(2).toString();

            String dashboard = document.getElementsByAttributeValue("class", "container-fluid p-4 view dashboard-view").toString();

            String head = document.getElementsByTag("head").toString();

            resultDashboard=head+dashboard+scriptResult+scriptSrc;
        } catch (IOException e) {
            logger.error("Error Class ExtentManager in method getDashboardHtmlFromReport", e);
        }
        return resultDashboard;

    }

    public static String getReportRoute() {
        return reportRoute;
    }

    public static String getReportName() {
        return reportName;
    }

}


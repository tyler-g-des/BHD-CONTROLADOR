package dbconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DbConnectionManager {

    private static DbConnectionManager instance=null;
    Connection connection =null;
    String port = "";
    String ip = "";
    String user = "";
    String password = "";
    String driver = "";
    String stringConnection = "";

    static Logger logger = LoggerFactory.getLogger(DbConnectionManager.class);

    public static DbConnectionManager getInstance(){
        if (instance==null){
            instance=new DbConnectionManager();
        }
        return instance;
    }

    private DbConnectionManager(){}

    /**
     * @implNote Método que extra las informaciones de las propiedades o properties de las conexiones
     * a base de datos.
     * @param  propertyRoute es la ruta de la propiedad de conexion a la base de datos
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */
    public void getPropertyDataBase(String propertyRoute){

        try {
            Properties props = new Properties();
            props.load(file.FileHelper.getInstance().getFileInsideJar(propertyRoute));
            port = props.getProperty("PORT");
            ip = props.getProperty("IP");
            user = props.getProperty("USER");
            password = props.getProperty("PASS");
            driver = props.getProperty("DRIVERNAME");
            stringConnection = props.getProperty("STRINGCONEXION");

        } catch (IOException e) {
            logger.error("Error Class DbConnectionManager in method getPropertyDataBase ", e);
        }

    }
    /**
     * @implNote Método para cerrar la conexion a la base de dato
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */

    public void closeDbConnection(){
        try {
            if (connection != null) {
                connection.close();
                if (connection.isClosed()) {
                    connection = null;
                }
            }
        } catch (SQLException e) {
            logger.error("Error Class DbConnectionManager in method closeDbConnection ", e);
        }
    }

    /**
     * @implNote Método que realiza la conexion a la base de datos a partir de las informaciones de las propiedades.
     * @param  propertyRoute es la ruta de la propiedad de conexion a la base de datos
     * @return Connection
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */

    public Connection dataBaseConnection(String propertyRoute){
        closeDbConnection();
        if (connection == null) {
            try {
                getPropertyDataBase(propertyRoute);
                Class.forName(driver).getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(stringConnection, user, password);

            } catch (Exception e) {
                logger.error("Error Class DbConnectionManager in method dataBaseConnection ", e);
            }

        }
        return connection;
    }
    /**
     * @implNote Método metodo para convertir un Resultset a una lista de mapas.
     * Coloca los nombres de las columnas igual a las extraidas de la consulta.
     * @param  resultSet es es resultado de la consulta a la base de datos que se desea recorrer.
     * @return List
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */

    public List<Map<String,String>> iterateResultSet(ResultSet resultSet){

        Map<String,String> map;
        List<Map<String,String>> elementList = new ArrayList<>();
        try {

            ResultSetMetaData rsmd=resultSet.getMetaData();

            int numberOfColumns = rsmd.getColumnCount();
            while(resultSet.next()) {
                map = new LinkedHashMap<>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    if (resultSet.getString(i)==null) {
                        map.put(rsmd.getColumnName(i), "NULL");
                    }
                    else{
                        map.put(rsmd.getColumnName(i), resultSet.getString(i).trim());
                    }
                }
                elementList.add(map);
            }

        }catch (SQLException e) {
            logger.error("Error Class DbConnectionManager in method iterateResultSet ", e);
        }
        return elementList;

    }

}

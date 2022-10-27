package dbconnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Route;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlServer {

    private static SqlServer instance=null;
    public static SqlServer getInstance(){
        if (instance==null){
            instance=new SqlServer();
        }
        return instance;
    }

    static Logger logger = LoggerFactory.getLogger(SqlServer.class);

    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    List<Map<String,String>> result;


    /**
     * @implNote es un metodo que realiza consulta o query tipo (SELECT) a la instancia de la base de datos
     * del la Padron de la JCE.
     * @param  sql es una cadena de caracteres o String donde se detalla el query o consultar a la base
     * de datos.
     * @return List
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */
    public List<Map<String,String>> queryJceSqa(String sql){
        try {
            preparedStatement= dbconnection.DbConnectionManager.getInstance().dataBaseConnection(Route.getInstance().getPadronJceproperty()).prepareStatement(sql);

            resultSet=preparedStatement.executeQuery();

            result= dbconnection.DbConnectionManager.getInstance().iterateResultSet(resultSet);

        } catch (SQLException e) {
            logger.error("Error Class SqlServer in method queryJceSqa ", e);
        }
        return result;
    }

}
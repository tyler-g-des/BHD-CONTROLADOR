package dbconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Db2{
    private static Db2 instance=null;
    public static Db2 getInstance(){
        if (instance==null){
            instance=new Db2();
        }
        return instance;
    }

    static Logger logger = LoggerFactory.getLogger(Db2.class);

    PreparedStatement preparedStatement=null;
    List<Map<String,String>> result;
    ResultSet resultSet=null;

    /**
     * @implNote es un metodo que realiza consulta o query tipo (SELECT) a la instancia de la base de datos
     * del equipo QA de AS400 llamado BHDASDT1.
     * @param  sql es una cadena de caracteres o String donde se detalla el query o consultar a la base
     * de datos.
     * @return List
     * @author FERNANDO PINEDA
     * @since 27/02/2022
     */
    public List<Map<String,String>> queryBHDASDT1(String sql){
        try {
            preparedStatement= dbconnection.DbConnectionManager.getInstance().dataBaseConnection(utils.Route.getInstance().getBhdasdt1property()).prepareStatement(sql);

            resultSet=preparedStatement.executeQuery();

            result= dbconnection.DbConnectionManager.getInstance().iterateResultSet(resultSet);

        } catch (SQLException e) {
            logger.error("Error Class DB2 in method queryBHDASDT1 ", e);
        }
        return result;
    }


}
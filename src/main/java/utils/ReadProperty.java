package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Properties;

public class ReadProperty {
    private ReadProperty(){}
    static Logger logger = LoggerFactory.getLogger(ReadProperty.class);
    static Properties props = new Properties();

    /**
     * Este metodo devuelve las propiedades almacenadas en un archivo .property
     * @param  fileName Ruta del archivo .property
     * @return property
     * @author Juan Castro
     * @since 16/10/2022
     */
    public static Properties readPropertiesFile(String fileName) {
        try {
            props.load(file.FileHelper.getInstance().getFileInsideJar(fileName));

        } catch (IOException e) {
            logger.error("Error in method readPropertiesFile on class  readPropertiesFile");
        }
        return props;
    }

}

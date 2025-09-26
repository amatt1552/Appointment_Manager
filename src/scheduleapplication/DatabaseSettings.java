package scheduleapplication;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * Handles passwords and such for the database.
 * Look out for a db.properties file if you need to make edits.
 * @author Austin Matthews
 */
public class DatabaseSettings 
{
    private static String propertyFileName = "db.properties";
    private final static String URL_PROPERTY = "MYSQL_DB_URL";
    private final static String USER_PROPERTY = "MYSQL_DB_USERNAME";
    private final static String PASS_PROPERTY = "MYSQL_DB_PASSWORD";
    
    /**
     * Gets DataSource based on the db.properties file.
     * @return 
     */
    protected static DataSource getDataSourceMySQL()
    {
        Properties dbProperties = new Properties();
        FileInputStream propertyFile;
        MysqlDataSource mysqlDS = null;
        try
        {
            propertyFile = new FileInputStream(propertyFileName);
            dbProperties.load(propertyFile);
            mysqlDS = new MysqlDataSource(); 
            mysqlDS.setURL(dbProperties.getProperty(URL_PROPERTY));
            mysqlDS.setUser(dbProperties.getProperty(USER_PROPERTY));
            mysqlDS.setPassword(dbProperties.getProperty(PASS_PROPERTY));
            propertyFile.close();
        }
        catch(IOException ioE)
        {
            ExceptionManager.displayError(ioE);
        }
        return mysqlDS;
    }
    
    /**
     * Sets the location for the filename.
     * @param url 
     */
    protected static void setPropertyFileLocation(String url)
    {
        propertyFileName = url;
    }
}

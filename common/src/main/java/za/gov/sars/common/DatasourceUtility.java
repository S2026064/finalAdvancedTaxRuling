package za.gov.sars.common;

import com.zaxxer.hikari.HikariDataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author S2028398
 */
public class DatasourceUtility {

    public static DataSource getDatasource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
//
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAXR60\\SQLEXPRESS:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Jiyeza");
//        dataSource.addDataSourceProperty("password", "P@ssw0rd");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAXR60\\SQLEXPRESS:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Jiyeza");
//        dataSource.addDataSourceProperty("password", "P@ssw0rd");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAXR60\\SQLEXPRESS:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Jiyeza");
//        dataSource.addDataSourceProperty("password", "P@ssw0rd");

//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTASU55;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Mpelwane");
//        dataSource.addDataSourceProperty("password", "Mpelwane12345");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTASU55;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Mpelwane");
//        dataSource.addDataSourceProperty("password", "Mpelwane12345");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTASU55;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Mpelwane");
//        dataSource.addDataSourceProperty("password", "Mpelwane12345");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAYA61\\SQLEXPRESS:1433;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "amogelang");
//        dataSource.addDataSourceProperty("password", "amogelang"); 
        
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTBFZ34\\SQLEXPRESS:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "regina");
//        dataSource.addDataSourceProperty("password", "Al1c$@95");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTASU55;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "Mpelwane");
//        dataSource.addDataSourceProperty("password", "Mpelwane12345");
//        
        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAXX56\\SQLEXPRESS:2010;databaseName=ATR_DB");
        dataSource.addDataSourceProperty("user", "tebx");
        dataSource.addDataSourceProperty("password", "tebx1234");
        
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTAXN73\\SQLEXPRESS14:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "terry");
////        dataSource.addDataSourceProperty("password", "P@sswords.");
//        dataSource.addDataSourceProperty("url", "jdbc:sqlserver://LPTBGH96\\SQLEXPRESS:2010;databaseName=ATR_DB");
//        dataSource.addDataSourceProperty("user", "nicho");
//        dataSource.addDataSourceProperty("password", "Qwerty6@");

        return dataSource;
    }

    public static DataSource getDatasourceLookup() {
        try {
            InitialContext initialContext = new InitialContext();
//            DataSource dataSource = (DataSource) initialContext.lookup("jdbc/atrDS");
            DataSource dataSource = (DataSource) initialContext.lookup("java:/atrDS");
            return dataSource;
        } catch (NamingException ex) {
            Logger.getLogger(DatasourceUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

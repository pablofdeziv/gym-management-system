package Vista;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class VistaInfoBD {
    
    public void infoMetadatos (DatabaseMetaData dbmd) throws SQLException
    {
        System.out.println(dbmd.getDatabaseProductName());
        System.out.println(dbmd.getDatabaseProductVersion());
        System.out.println(dbmd.getURL());
        System.out.println(dbmd.getDriverName());
        System.out.println(dbmd.getDriverVersion());
        System.out.println(dbmd.getUserName());
        System.out.println(dbmd.getSQLKeywords());
        
    }
}

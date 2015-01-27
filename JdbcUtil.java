/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interchangeautomation;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

    public static void enable_dbms_output(Connection conn, int buffer_size) {
        System.out.print("Enabling DBMS_OUTPUT - ");
        try {
            CallableStatement stmt = conn.prepareCall("{call sys.dbms_output.enable(?) }");
            stmt.setInt(1, buffer_size);
            stmt.execute();
            System.out.println("Enabled!");
        } catch (Exception e) {
            System.out.println("Problem occurred while trying to enable dbms_output! " + e.toString());
        }
    }

    public static void print_dbms_output(Connection conn) {
        System.out.println("Dumping DBMS_OUTPUT to System.out : ");
        try {
            CallableStatement stmt = conn.prepareCall("{call sys.dbms_output.get_line(?,?)}");
            stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
            int status = 0;
            do {
                stmt.execute();
                System.out.println(" " + stmt.getString(1));
                status = stmt.getInt(2);
            } while (status == 0);
            System.out.println("End of dbms_output!");
        } catch (Exception e) {
            System.out.println("Problem occurred during dump of dbms_output! " + e.toString());
        }
    }

    /**
     * Simply creates a connection to an Oracle-database
     */
    public static Connection getConnection(String jdbc, String userid, String passwd, boolean autocommit) throws SQLException {

        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection conn = DriverManager.getConnection(jdbc, userid, passwd);
        conn.setAutoCommit(autocommit);
// ((OracleConnection)conn).setDefaultExecuteBatch (150);
        return conn;
    }
}

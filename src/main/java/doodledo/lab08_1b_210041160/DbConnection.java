package doodledo.lab08_1b_210041160;
import java.sql.*;

public class DbConnection {
    public static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:src/main/resources/doodledo/lab08_1b_210041160/veekun-pokedex.sqlite");
        System.out.println("Connected");
        return con;
    }

    public static ResultSet executeQuery(String query) throws SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = connect();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String s) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = connect();
            stmt = con.createStatement();
            stmt.executeUpdate(s);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
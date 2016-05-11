package dataservice;

import utility.DatabaseConstant;

import java.sql.*;

/**
 * Created by raychen on 16/5/9.
 */
public class Database {

    Connection con;
    PreparedStatement pst;
    static Database mysql;
    static int count = 0;

    public static Database getMysql(){
        if (count == 0) {
            count ++;
            mysql = new Database();
        }
        return mysql;
    }

    private Database(){
        connect();
    }

    public Connection connect(){
        try {
            Class.forName(DatabaseConstant.name);
            con = DriverManager.getConnection(
                    DatabaseConstant.url,
                    DatabaseConstant.user,
                    DatabaseConstant.password);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean execute(String sql){
        try {
            pst = con.prepareStatement(sql);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet query(String sql){
        try {
            pst = con.prepareStatement(sql);
            return pst.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Database db = Database.getMysql();

    }
}

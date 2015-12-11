/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao.Conector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao-u
 */
public class DaoDefault {

    private PreparedStatement preState;
    private ResultSet resuSet;
    private Connection con;
    public final static String SUCESS = "yes";

    public PreparedStatement getPreState() {
        return preState;
    }

    public void createPreState(String sql) {
        con = Conector.getCon();
        try {
            preState = con.prepareStatement(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createPreStateRet(String sql) {
        con = Conector.getCon();
        try {
            preState = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getResuSet() {
        return resuSet;
    }

    public ResultSet executeQuery() {
        try {
            SQL.setLastSQL(preState.toString());
            resuSet = preState.executeQuery();
            return resuSet;
        } catch (SQLException ex) {
            Logger.getLogger(DaoDefault.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String executeUpdate() {
        try {
            SQL.setLastSQL(preState.toString());
            preState.executeUpdate();
            return SUCESS;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public void close() {
        Conector.free(resuSet, preState, con);
    }

    public Object getGeneteredKey() {
        try {
            resuSet = preState.getGeneratedKeys();
            if (resuSet.next()) {
                return resuSet.getObject(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoDefault.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

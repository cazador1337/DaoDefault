/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao.Conector;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author JOAO
 */
public class Conector {

    public static Connection getCon() {
        try {
            return DriverManager.getConnection(SQL.url+SQL.dataBase, SQL.user, SQL.pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void free(AutoCloseable x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void free(AutoCloseable... x) {
        for (int i = 0; i < x.length; i++) {
            free(x[i]);
        }
    }
}

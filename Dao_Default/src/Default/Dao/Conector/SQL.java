/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao.Conector;

/**
 *
 * @author JOAO
 */
public class SQL {

    static String url = "jdbc:mysql://localhost:3306/";
    static String user = "root";
    static String pass = "root";
    static String dataBase = "?";
    static String lastSQL;

    public static String getDataBase() {
        return dataBase;
    }

    public static void setDataBase(String dataBase) {
        SQL.dataBase = dataBase;
    }

    public static void setUser(String user) {
        SQL.user = user;
    }

    public static void setPass(String pass) {
        SQL.pass = pass;
    }

    public static String getLastSQL() {
        return lastSQL;
    }

    static void setLastSQL(String lastSQL) {
        SQL.lastSQL = lastSQL;
    }

}

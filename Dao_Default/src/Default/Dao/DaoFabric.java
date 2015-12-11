/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao;

import Default.Dao.Conector.SQL;
import Default.Dao.Exception.DaoExceptionAnnotation;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author joao
 */
public class DaoFabric {
    
    public static final void setDataBase(String database, String user, String pass){
        SQL.setDataBase(database);
        SQL.setPass(pass);
        SQL.setUser(user);
    }
    
    public static final DaoUniversal newDaoUniversal(Class<?> classe){
        try {
            return new DaoUniversal(classe);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(DaoFabric.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DaoExceptionAnnotation ex) {
            Logger.getLogger(DaoFabric.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static final DaoSimple newDaoSimple(){
        return new DaoSimple();
    }
    
}

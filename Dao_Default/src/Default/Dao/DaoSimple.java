/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao;

import Default.Dao.Conector.DaoDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JOAO
 */
public class DaoSimple extends DaoEngine {

    DaoSimple() {
    }

    final ArrayList<Object[]> getListaObject(ResultSet rs, int i) {
        ArrayList<Object[]> r = new ArrayList<>();
        try {
            while (rs.next()) {
                Object[] list = new Object[i];
                for (int j = 0; j < i; j++) {
                    list[j] = rs.getObject(j + 1);
                }
                r.add(list);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoGeneric.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            getDao().close();
        }
        return r;
    }

    public ArrayList<Object[]> select(String SQL, int i) {
        initializeDao(SQL);
        return getListaObject(getDao().executeQuery(), i);
    }

    public ArrayList<Object[]> select(String SQL, int i, Object... args) {
        initializeDao(SQL);
        setValueAtState(args);
        return getListaObject(getDao().executeQuery(), i);
    }

    public String insert(String SQL, Object... args) {
        initializeDao(SQL);
        setValueAtState(args);
        String ret = getDao().executeUpdate();
        getDao().close();
        return ret;
    }

    public Object[] insertRet(String SQL, Object... args) {
        Object id = null;

        setDao(new DaoDefault());
        getDao().createPreStateRet(SQL);

        setValueAtState(args);

        String r = getDao().executeUpdate();
        id = getDao().getGeneteredKey();

        getDao().close();

        return new Object[]{r, id};
    }

}

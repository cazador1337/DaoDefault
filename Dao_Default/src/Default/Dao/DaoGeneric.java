package Default.Dao;

import Default.Dao.Conector.DaoDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao-u Version: 1.1
 */
public abstract class DaoGeneric<MyObject> extends DaoEngine{

    public DaoGeneric(DaoCaster caster) {
        super(caster);
    }

    //Métodos de uso para as funcionalidades.    
    private final ArrayList<MyObject> getMyLista(ResultSet rs) {
        ArrayList<MyObject> myLista = new ArrayList<>();
        try {
            while (rs.next()) {
                MyObject my = create();
                setValue(rs, my);
                myLista.add(my);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoGeneric.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            getDao().close();
        }

        return myLista;
    }

    public abstract MyObject create();

    //Termina aqui
    public ArrayList<MyObject> select(String SQL) {
        initializeDao(SQL);
        return getMyLista(getDao().executeQuery());
    }

    public ArrayList<MyObject> select(String SQL, Object... list) {
        initializeDao(SQL);
        setValueAtState(list);
        return getMyLista(getDao().executeQuery());
    }

    public String insert(String SQL, MyObject my) {
        initializeDao(SQL);
        setValueAtState(my);
        String ret = getDao().executeUpdate();
        getDao().close();
        return ret;
    }

    public Object[] insertRet(String SQL, MyObject my) {
        Object id = null;

        setDao(new DaoDefault());
        getDao().createPreStateRet(SQL);

        setValueAtState(my);

        String r = getDao().executeUpdate();
        id = getDao().getGeneteredKey();

        getDao().close();

        return new Object[]{r, id};
    }

}

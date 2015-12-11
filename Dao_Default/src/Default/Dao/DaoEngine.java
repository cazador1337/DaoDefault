/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao;

import Default.Dao.Conector.DaoDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author JOAO
 *
 */
public abstract class DaoEngine<MyObject> {

    private DaoDefault dao;
    private DaoCaster<MyObject> caster;

    public DaoEngine() {
    }

    DaoEngine(DaoCaster<MyObject> caster) {
        this.caster = caster;
    }

    public DaoCaster getCaster() {
        return caster;
    }

    void initializeDao(String SQL) {
        dao = new DaoDefault();
        dao.createPreState(SQL);
    }

    DaoDefault getDao() {
        return dao;
    }

    void setDao(DaoDefault dao) {
        this.dao = dao;
    }

    final void setValueAtState(PreparedStatement ps, Object o, int i) {
        try {
            ps.setObject(i, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final void setValueAtState(MyObject aux) {
        Object[] o = caster.extract(aux);
        for (int j = 0; j < o.length; j++) {
            setValueAtState(dao.getPreState(), o[j], j + 1);
        }
    }

    final void setValueAtState(Object... o) {
        for (int j = 0; j < o.length; j++) {
            setValueAtState(dao.getPreState(), o[j], j + 1);
        }
    }

    void setValue(ResultSet rs, MyObject my) {
        caster.inserir(my, rs);
    }

    public String update(String SQL, Object... list) {
        initializeDao(SQL);

        setValueAtState(list);
        String ret = getDao().executeUpdate();

        dao.close();
        return ret;
    }

}

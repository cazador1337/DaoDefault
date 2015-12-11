/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao;

import Default.Dao.Exception.DaoExceptionAnnotation;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JOAO
 */
public class DaoUniversal<MyObject> extends DaoGeneric<MyObject> {
    
    private Class<?> classe;

    DaoUniversal(Class<?> classe) throws NoSuchMethodException, DaoExceptionAnnotation{
        super(new DaoCaster(classe));
        this.classe = classe;
    }

    @Override
    public MyObject create() {
        try {
            return (MyObject) classe.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(DaoUniversal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DaoUniversal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object[] insertRet(MyObject my) {
        return super.insertRet(super.getCaster().getSqlInsert(), my);
    }

    public String insert(MyObject my) {
        return super.insert(super.getCaster().getSqlInsert(), my); 
    }
    

    public ArrayList<MyObject> select() {
        return super.select(getCaster().getSqlSelect()); 
    }

    public String update(MyObject fonte) {
        return super.update(getCaster().getSqlUpdate(), getCaster().extractWithKey(fonte));
    }
    
    public String delete(Object... ids){
        return super.update(getCaster().getSqlDelete(), ids);
    }
    
    public String delete(MyObject fonte){
        return super.update(getCaster().getSqlDelete(), getCaster().extractOnlyKey(fonte));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao.Exception;

/**
 *
 * @author JOAO
 */
public class DaoExceptionAnnotation extends Exception{

    public DaoExceptionAnnotation(Class<?> tipo) {
        super(tipo.toString() + " erro nas anotações");
    }
    
    
    
}

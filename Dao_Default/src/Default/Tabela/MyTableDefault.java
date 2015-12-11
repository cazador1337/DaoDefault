/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Tabela;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author joao-u
 */
public class MyTableDefault extends AbstractTableModel {

    private String[] coluna;
    private List<TableObjectInterface> lista;
    private Class<?>[] classes;
    private boolean onlySelect = false;
    private int pos = -1;

    public MyTableDefault(List<TableObjectInterface> lista, String... coluna){
        this.coluna = coluna;
        this.lista = lista;
        initilizeClasses();
    }
    
    public MyTableDefault(Boolean onlySelect ,List<TableObjectInterface> lista, String... coluna){        
        this(lista, coluna);
        this.onlySelect = onlySelect;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TableObjectInterface o = lista.get(rowIndex);
        return o.atributeToTable()[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return coluna.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (!onlySelect) {
            return classes[columnIndex] == Boolean.class;
        } else if(classes[columnIndex] == Boolean.class) {
            if (pos == -1) {
                pos = rowIndex;
                return true;
            } else if (rowIndex == pos) {
                pos = -1;
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return coluna[column];
    }

    public List<TableObjectInterface> getLista() {
        return lista;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return classes[i];
    }

    private void initilizeClasses() {
        TableObjectInterface o = lista.get(0);
        Object[] list = o.atributeToTable();
        classes = new Class<?>[list.length];
        for (int i = 0; i < list.length; i++) {
            classes[i] = list[i].getClass();
        }
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        TableObjectInterface d = lista.get(i);
        d.atributeToTable()[i1] = o;
    }
    
    public TableObjectInterface getSelect(){
        return !onlySelect ? null : pos == -1 ? null : lista.get(pos);
    }

}

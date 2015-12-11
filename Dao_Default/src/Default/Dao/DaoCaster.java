/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default.Dao;

import Default.Dao.Exception.DaoExceptionAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import Default.Dao.Annotation.Getter;
import Default.Dao.Annotation.PrimaryKey;
import Default.Dao.Annotation.Setter;
import Default.Dao.Annotation.Table;

/**
 *
 * @author JOAO
 */
public class DaoCaster<MyObject> {

    private Class<MyObject> classe;
    private String sqlInsert, sqlSelect, sqlUpdate, sqlDelete;
    private ArrayList<Method> colunasGetter, colunasSetter;

    DaoCaster(Class<MyObject> classe) throws NoSuchMethodException, DaoExceptionAnnotation {
        this.classe = classe;
        colunasGetter = new ArrayList<>();
        colunasSetter = new ArrayList<>();
        gerarSqlInsert();
        gerarSqlSelect();
        gerarSqlUpdate();
        gerarSqlDelete();
    }

    public String getSqlInsert() {
        return sqlInsert;
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    public String getSqlUpdate() {
        return sqlUpdate;
    }

    public String getSqlDelete() {
        return sqlDelete;
    }

    public int getSize() {
        return colunasGetter.size();
    }

    private void gerarSqlInsert() throws NoSuchMethodException, DaoExceptionAnnotation {
        String sql = "insert into ";
        Table table = null;
        ArrayList<Getter> colunas = new ArrayList<>();

        Constructor<?>[] constructor = classe.getConstructors();
        for (Constructor<?> con : constructor) {
            table = con.getAnnotation(Table.class);
            if (table != null) {
                break;
            }
        }

        Method[] met = classe.getDeclaredMethods();
        for (int i = 0; i < met.length; i++) {
            Getter g = met[i].getAnnotation(Getter.class);
            if (g != null) {
                colunas.add(g);
                this.colunasGetter.add(met[i]);
            }
            Setter s = met[i].getAnnotation(Setter.class);
            if (s != null) {
                this.colunasSetter.add(met[i]);
            }
        }

        if (table != null && !colunas.isEmpty()) {
            sql += table.tableName() + " (";
            String aux = "";
            for (int i = 0; i < colunas.size(); i++) {
                aux += "?,";
                sql += colunas.get(i).coluna() + ",";
            }
            aux = aux.substring(0, aux.length() - 1);
            sql = sql.substring(0, sql.length() - 1) + ") values (" + aux + ");";
            sqlInsert = sql;
        } else {
            throw new DaoExceptionAnnotation(classe);
        }
    }

    public void inserir(MyObject destino, ResultSet res) {
        try {
            for (Method coluna : colunasSetter) {
                Setter s = coluna.getAnnotation(Setter.class);
                coluna.invoke(destino, cast(res.getObject(s.coluna()), s.tipo()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] extract(MyObject fonte) {
        Object[] list = new Object[colunasGetter.size()];
        for (int i = 0; i < list.length; i++) {
            try {
                list[i] = colunasGetter.get(i).invoke(fonte);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Object[] extractWithKey(MyObject fonte) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < colunasGetter.size(); i++) {
            try {
                list.add(colunasGetter.get(i).invoke(fonte));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Method[] met = classe.getMethods();

        for (Method method : met) {
            PrimaryKey p = method.getAnnotation(PrimaryKey.class);
            if (p != null) {
                try {
                    list.add(method.invoke(fonte));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return list.toArray();
    }
    
    public Object[] extractOnlyKey(MyObject fonte) {
        ArrayList<Object> list = new ArrayList<>();
        
        Method[] met = classe.getMethods();
        for (Method method : met) {
            PrimaryKey p = method.getAnnotation(PrimaryKey.class);
            if (p != null) {
                try {
                    list.add(method.invoke(fonte));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return list.toArray();
    }

    private Object cast(Object aux, Class<?> tipo) {
        if (tipo == Integer.class || tipo == int.class) {
            return Integer.parseInt(aux.toString());
        } else if (tipo == Double.class || tipo == double.class) {
            return Double.parseDouble(aux.toString());
        } else if (tipo == Float.class || float.class == tipo) {
            return Float.parseFloat(aux.toString());
        }
        return tipo.cast(aux);//Falta estrategias para outros tipos de objetos como timestamp.
    }

    private void gerarSqlSelect() {
        String sql = "select * from ";
        Table table = null;
        ArrayList<Getter> colunas = new ArrayList<>();

        Constructor<?>[] constructor = classe.getConstructors();
        for (Constructor<?> con : constructor) {
            table = con.getAnnotation(Table.class);
            if (table != null) {
                break;
            }
        }

        sqlSelect = sql + table.tableName() + ";";
    }

    private void gerarSqlUpdate() {
        try {
            Method[] met = classe.getMethods();

            Table table = null;

            Constructor<?>[] constructor = classe.getConstructors();
            for (Constructor<?> con : constructor) {
                table = con.getAnnotation(Table.class);
                if (table != null) {
                    break;
                }
            }

            String sql = "update " + table.tableName() + " set ";
            String aux = " where ";
            for (Method method : met) {
                Getter g = method.getAnnotation(Getter.class);
                if (g != null) {
                    sql += g.coluna() + " = ?,";
                }
                PrimaryKey p = method.getAnnotation(PrimaryKey.class);
                if (p != null) {
                    aux += p.coluna() + " = ? and ";
                }
            }
            sql = sql.substring(0, sql.length() - 1);
            aux = aux.substring(0, aux.length() - 5) + ";";
            sql += aux;
            sqlUpdate = sql;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gerarSqlDelete() {
        try {
            Method[] met = classe.getMethods();

            Table table = null;

            Constructor<?>[] constructor = classe.getConstructors();
            for (Constructor<?> con : constructor) {
                table = con.getAnnotation(Table.class);
                if (table != null) {
                    break;
                }
            }

            String sql = "delete from " + table.tableName();
            String aux = " where ";
            for (Method method : met) {
                PrimaryKey p = method.getAnnotation(PrimaryKey.class);
                if (p != null) {
                    aux += p.coluna() + " = ? and ";
                }
            }
            aux = aux.substring(0, aux.length() - 5) + ";";
            sql += aux;
            sqlDelete = sql;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

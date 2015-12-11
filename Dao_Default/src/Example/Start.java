/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Example;

import Default.Dao.Conector.SQL;
import Default.Dao.DaoCaster;
import Default.Dao.DaoFabric;
import Default.Dao.DaoSimple;
import Default.Dao.DaoUniversal;
import java.util.ArrayList;

/**
 *
 * @author joao
 */
public class Start {
    public static void main(String[] args) {
        DaoFabric.setDataBase("teste", "root", "root");//importante ressaltar, é obrigatorio setar o database, user e pass por este metodo.
        DaoUniversal<Modelo> dao = DaoFabric.newDaoUniversal(Modelo.class);
        
        for(Modelo m : dao.select()){//utiliza o sql gerado pelo DaoCaster que se baseia nas Anotações deixadas na classe Modelo
            System.out.println(m.getCampo());//O metodo converte o resultSet em um array do tipo Modelo, ou do que fosse especificado
        }//ele também se utilizar das anotações, sendo as com @Setter as utilizadas.
        
        Modelo m = new Modelo();//Inserir usando também a string gerada pelos @Getter na classe Modelo junto com a @Table, que contém o nome da tabela.
        m.setCampo("exemplo");//set nos atributos.
        m.setIdade(25);
        m.setValor(20.6);//o Id do objeto será gerado mais tarde.
        
        String saida = dao.insert(m);//Este insert utiliza-se da sql gerada pelo DaoCaster.
        System.out.println(saida);//Todos os metodos update, insert, delete retornam um padrão 'yes' para tudo OK e caso erro retorna a mensagem de erro.
        System.out.println(SQL.getLastSQL());//é possivel com qualquer comando ver o sql que ele executou.
        
        for (Modelo e : dao.select("select * from tabela where id = ?", m.getId())){//existem selects personalizados, 
            //diferentes mas que devem retornar apenas Modelo, pois assim foi especificado no começo
            System.out.println(e.getIdade());//Só para mostrar que realmente retornou algo
        }
        
        System.out.println(dao.delete(m));//o delete faz uso do metodo update só que utiliza o sql gerado pelo DaoCaster para deletar o objeto do banco.
        
        Object[] ret = dao.insertRet(m);//Existem a opção de se inserir e já obter a id auto gerada pelo MySQL, neste retorno, é 
        //recuperado dois objetos o primeiro a mensagem de deu tudo certo ou não e o segundo a id.
        
        dao.update("delete from tabela where id = ?", 1);//é possivel realizar updates por aqui ou deletes, mais personalizados.
        
        //Mas metodos ocultos.
        
        DaoCaster<Modelo> cast = dao.getCaster();//É possivel capturar o DaoCaster que é utilizado pela classe DaoUniversal
        //com ele é possivel fazer coisa bem interessantes.
        
        System.out.println(cast.getSqlDelete());
        System.out.println(cast.getSqlInsert());
        System.out.println(cast.getSqlSelect());
        System.out.println(cast.getSqlUpdate());//Como ver as SQL gerada por ele
        
        Object[] att = cast.extract(m);//retirar todos os campos que serão utilizados para insert, de uma vez
        for (Object ob : att) {
            System.out.println(ob.toString());
        }
        
        att = cast.extractOnlyKey(m);//extrair somente as keys do objeto.
        
        att = cast.extractWithKey(m);//ou os atributos mais as keys.
        
        cast.inserir(m, null);//no caso este metodo não é util aqui, mas é muito util quando se executa o executeQuery ele insere um resultSet
        //e seus dados no objeto.
        
        //Eu não vou colocar exemplo para todas as coisas.
        
        //Outro objeto mais simples, mas poderoso.
        
        DaoSimple simples = DaoFabric.newDaoSimple();//Este objeto utiliza-se de arrays nos retornos do select;
        ArrayList<Object[]> array = simples.select(cast.getSqlSelect(), 2);// o primeiro número seria a quantidade de colunas a retornar.
        
    }
}

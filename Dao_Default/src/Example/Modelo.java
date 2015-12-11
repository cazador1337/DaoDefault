/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Example;

import Default.Dao.Annotation.*;

/**
 *
 * @author joao
 */
public class Modelo {

    private String campo;
    private double valor;
    private int idade;
    private int id;

    @Table(tableName = "tabela")
    public Modelo() {

    }

    @PrimaryKey (coluna = "campo")
    @Getter(coluna = "campo")
    public String getCampo() {
        return campo;
    }

    @Getter(coluna = "valor")
    public double getValor() {
        return valor;
    }

    @Getter(coluna = "idade")
    public int getIdade() {
        return idade;
    }

    @Setter(coluna = "campo", tipo = String.class)
    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Setter(coluna = "valor", tipo = Double.class)
    public void setValor(double valor) {
        this.valor = valor;
    }

    @Setter(coluna = "idade", tipo = int.class)
    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Setter(coluna = "id", tipo = int.class)
    public void setId(int id) {
        this.id = id;
    }
    
    @PrimaryKey (coluna = "id")
    public int getId() {
        return id;
    }

}

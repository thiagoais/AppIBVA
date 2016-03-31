package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thiago on 19/03/2016.
 */
public class CelulaDAO {
    private final String TABELA = "celula";

    public ArrayList<Celula> retornaCelulas() throws SQLException {
        ArrayList<Celula> celulas = new ArrayList<>();
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            //Garantir no banco que o login será único
            statement = conexao.prepareStatement(
                    " SELECT id_celula, nome, lider, dia, horario, local_celula, dia_jejum, periodo, versiculo, imagem  " +
                    "   FROM celula                                                                    " +
                    "  ORDER BY nome                                                                   ");

            rs = statement.executeQuery();

            while (rs.next()) {
                celula = new Celula();
                celula.setId_celula(rs.getInt(1));
                celula.setNome(rs.getString(2));
                celula.setLider(rs.getString(3));
                celula.setDia(rs.getString(4));
                celula.setHorario(rs.getString(5));
                celula.setLocal_celula(rs.getString(6));
                celula.setDia_jejum(rs.getInt(7));
                celula.setPeriodo(rs.getString(8));
                celula.setVersiculo(rs.getString(9));
                celula.setImagem(rs.getBlob(10));
                celulas.add(celula);
            }
        } catch (Exception e) {
            //TODO LOG ERRO
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception mysqlEx) {
                //TODO LOG ERRO
                mysqlEx.printStackTrace();
            }
        }
        return celulas;
    }
}

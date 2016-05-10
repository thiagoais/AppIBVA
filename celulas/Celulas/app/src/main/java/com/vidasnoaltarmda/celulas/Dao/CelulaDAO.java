package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.io.InputStream;
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

    //retorna celula por meio do id
    public Celula retornaCelulaPorId(int idCelula) throws SQLException {
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            //Garantir no banco que o login será único
            statement = conexao.prepareStatement(
                    " SELECT id_celula, nome, lider, dia, horario, local_celula, dia_jejum, periodo, versiculo, imagem  " +
                            "   FROM celula                                                                             " +
                            " WHERE id_celula = ?                                                                       ");

            statement.setInt(1, idCelula);
            rs = statement.executeQuery();

            while (rs.next()) {
                celula = new Celula();
                celula.setId_celula(rs.getInt(1));
                celula.setNome(rs.getString(2));
                celula.setLider(rs.getString(3));
                celula.setDia(rs.getString(4));
                celula.setHorario(Utils.converteHoraApp(rs.getString(5)));
                celula.setLocal_celula(rs.getString(6));
                celula.setDia_jejum(rs.getString(7));
                celula.setPeriodo(rs.getString(8));
                celula.setVersiculo(rs.getString(9));
                celula.setImagem(rs.getBlob(10)); //TODO verificar necessidade e se consome tempo e uso de dados
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
        return celula;
    }

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
                celula.setHorario(Utils.converteHoraApp(rs.getString(5)));
                celula.setLocal_celula(rs.getString(6));
                celula.setDia_jejum(rs.getString(7));
                celula.setPeriodo(rs.getString(8));
                celula.setVersiculo(rs.getString(9));
                celula.setImagem(rs.getBlob(10)); //TODO verificar necessidade e se consome tempo e uso de dados
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

    //metodo que retorna a imagem da celula passada por parametro e salva no caminho especificado
    //TODO verificar possibilidade de implementacao de um cache de imagens
    public boolean retornaCelulaImagem(Celula celula, String caminhoArquivo, String nomeArquivo) throws SQLException {
        InputStream isCelula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT imagem            " +
                            "   FROM celula       " +
                            "  WHERE id_celula = ? ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();

            if (rs.next()) {
                isCelula = rs.getBinaryStream(1);
            }

            Utils.downloadImagemBanco(caminhoArquivo, isCelula, nomeArquivo);

            return true;
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
        return false;
    }
}

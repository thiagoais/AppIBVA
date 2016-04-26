package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class GrupoEvangelisticoDAO {

    private final String TABELA = "grupo_evangelistico"; //Nome da tabela do banco dados que a classe vai trabalhar

    public ArrayList<GrupoEvangelistico> retornaGruposEvangelisticos(Celula celula) throws SQLException {
        ArrayList<GrupoEvangelistico> gruposEvangelisticos = new ArrayList<>();
        GrupoEvangelistico grupoEvangelistico = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();

        try {
            statement = conexao.prepareStatement(
                    " SELECT id_ge, id_celula, nome, datediff(now(), data_cadastro) as dias "+
                    "   FROM grupo_evangelistico                                            " +
                    " WHERE id_celula = ?  ORDER BY data_cadastro DESC ;                           ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();
            while (rs.next()) {
                grupoEvangelistico = new GrupoEvangelistico();
                grupoEvangelistico.setId_ge(rs.getInt(1));
                grupoEvangelistico.setId_celula(rs.getInt(2));
                grupoEvangelistico.setNome(rs.getString(3));
                grupoEvangelistico.setDias(rs.getInt(4));
                gruposEvangelisticos.add(grupoEvangelistico);
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
        return gruposEvangelisticos;
    }

    public boolean insereGE(GrupoEvangelistico grupoEvangelistico) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " INSERT INTO grupo_evangelistico (id_celula, nome, data_cadastro) values (?,?,NOW())");
            statement.setInt   (1, grupoEvangelistico.getId_celula());
            statement.setString(2, grupoEvangelistico.getNome());

            int row = statement.executeUpdate();
            if (row > 0) {
                inserido = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO LOG ERRO
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception mysqlEx) {
                System.out.println(mysqlEx.toString());
                //TODO LOG ERRO
            }
        }
        return inserido;
    }
}


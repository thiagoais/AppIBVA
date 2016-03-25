package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thiago on 24/03/2016.
 */
public class ProgramacaoDAO {
    public ArrayList<Programacao> retornaProgramacoes() throws SQLException {
        ArrayList<Programacao> programacoes = new ArrayList<>();
        Programacao programacao = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT id_programacao, id_celula, nome, data_prog, horario, " +
                    "        local_prog, telefone, valor, imagem                  " +
                    "   FROM programacao                                          " +
                    " ORDER BY data_prog desc;                                    ");

            rs = statement.executeQuery();

            while (rs.next()) {
                programacao = new Programacao();
                programacao.setId_programacao(rs.getInt(1));
                programacao.setId_celula(rs.getInt(2));
                programacao.setNome(rs.getString(3));
                programacao.setData_prog(Utils.coverteDataApp(rs.getString(4)));
                programacao.setHorario(rs.getString(5));
                programacao.setLocal_prog(rs.getString(6));
                programacao.setTelefone(rs.getString(7));
                programacao.setValor(rs.getString(8));
                programacoes.add(programacao);
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
        return programacoes;
    }
}

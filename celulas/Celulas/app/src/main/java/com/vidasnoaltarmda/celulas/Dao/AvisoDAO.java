package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by barque on 24/03/2016.
 */
public class AvisoDAO {

    private final String TABELA = "aviso"; //Nome da tabela do banco dados que a classe vai trabalhar

    public ArrayList<Aviso> retornaAvisos(Celula celula) throws SQLException {
        ArrayList<Aviso> avisos = new ArrayList<>();
        Aviso aviso = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();

        try {
            statement = conexao.prepareStatement(
                    " SELECT id_aviso, id_celula, titulo, conteudo "+
                            "   FROM aviso                                          " +
                            " WHERE id_celula = ? ORDER BY id_aviso desc;            ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();
            while (rs.next()) {
                aviso = new Aviso();
                aviso.setId_aviso(rs.getInt(1));
                aviso.setId_celula(rs.getInt(2));
                aviso.setTitulo(rs.getString(3));
                aviso.setConteudo(rs.getString(4));
                avisos.add(aviso);
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
        return avisos;
    }
}


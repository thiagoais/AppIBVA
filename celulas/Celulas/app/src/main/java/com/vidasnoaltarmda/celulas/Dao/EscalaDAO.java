package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by barque on 21/04/2016.
 */
public class EscalaDAO {
    private final String TABELA = "escala";
        public Escala retornaEscala(Celula celula) throws SQLException {
            boolean escalaPreenchida = false;
            Escala escala = null;
            ArrayList<Escalacao> escalacoes = new ArrayList<>();
            Escalacao escalacao;
            ResultSet rs = null;
            PreparedStatement statement = null;
            Connection conexao = null;
            conexao = ConnectionManager.getConnection();

        try {

            statement = conexao.prepareStatement(
                    " SELECT e.id_escala, e.id_celula, e.data_celula, e.hora_celula, e.local_celula, " +
                    "        ec.id_escalacao, ec.membro, ec.tarefa                                   " +
                    "   FROM escala e INNER JOIN escalacao ec on (e.id_escala = ec.id_escala)        " +
                    "  WHERE id_celula = ?                                                           ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();

            while (rs.next()) {
                if (!escalaPreenchida) {
                    escala = new Escala();
                    escala.setId_escala(rs.getInt(1));
                    escala.setId_escala(rs.getInt(2));
                    escala.setData_celula(Utils.coverteDataApp(rs.getString(3)));
                    escala.setHora_celula(Utils.coverteHoraApp(rs.getString(4)));
                    escala.setLocal_celula(rs.getString(5));
                }

                escalacao = new Escalacao();
                escalacao.setMembro(rs.getString(7));
                escalacao.setTarefa(rs.getString(8));
                escalacoes.add(escalacao);
            }
            escala.setEscalacoes(escalacoes);
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
        return escala;
    }


}

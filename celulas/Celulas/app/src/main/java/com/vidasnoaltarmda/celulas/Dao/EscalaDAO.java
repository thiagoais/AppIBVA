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
import java.util.List;


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
                    escala.setData_celula(Utils.converteDataApp(rs.getString(3)));
                    escala.setHora_celula(Utils.converteHoraApp(rs.getString(4)));
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

    public boolean insereEscala(Escala escala) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = true;
        ResultSet rs = null;
        conexao = ConnectionManager.getConnection();
        try {
            //TODO utilizar transacao no banco para manter integridade dos dados com previsao de rollback caso ocorra algum erro
            //DELETE escala anterior - Nao eh necessario excluir escalacoes pois ja existe trigger no banco ondelete com essa funcionalidade
            String delCommand = " DELETE FROM escala WHERE id_celula = ?";
            statement = conexao.prepareStatement(delCommand);
            statement.setInt(1, escala.getId_celula());

            statement.executeUpdate();
            //-- DELETE escala anterior

            //INSERT escala
            statement = conexao.prepareStatement(
                    " INSERT INTO escala (id_celula, data_celula, hora_celula, local_celula) values (?,?,?,?)");
            statement.setInt   (1, escala.getId_celula());
            statement.setString(2, Utils.converteDataBanco(escala.getData_celula()));
            statement.setString(3, escala.getHora_celula());
            statement.setString(4, escala.getLocal_celula());

            inserido &= statement.executeUpdate() > 0;
            //INSERT escala

            //RECUPERA codigo da escala inserida
            statement = conexao.prepareStatement(
                    " SELECT id_escala      " +
                    "   FROM escala    " +
                    "  WHERE id_celula = ?  ");

            statement.setInt(1, escala.getId_celula());
            rs = statement.executeQuery();
            int idEscalaRecuperado = -1;
            if (rs.next()) {
                idEscalaRecuperado = rs.getInt(1);
            }
            //--RECUPERA codigo da escala inserida

            //INSERT escalacoes

            for (Escalacao escalacao: escala.getEscalacoes()) {
                statement = conexao.prepareStatement(
                        " INSERT INTO escalacao (id_escala, membro, tarefa) values (?,?,?)");
                statement.setInt(1, idEscalaRecuperado);
                statement.setString(2, escalacao.getMembro());
                statement.setString(3, escalacao.getTarefa());

                inserido &= statement.executeUpdate() > 0;
            }
            //--INSERT escalacoes

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

    public boolean deletaEscalas(List<Escala> escalas) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean sucesso = false;
        conexao = ConnectionManager.getConnection();
        try {

            String delCommand = " DELETE FROM aviso WHERE id_aviso IN (";
            for (int i = 0; i < escalas.size() - 1; i++) {
                delCommand = delCommand.concat("?,");
            }
            delCommand = delCommand.concat("?)");

            statement = conexao.prepareStatement(delCommand);

            for (int i = 1; i <= escalas.size(); i++) {
                statement.setInt(i, escalas.get(i-1).getId_escala());
            }

            int row = statement.executeUpdate();
            if (row > 0) {
                sucesso = true;
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
        return sucesso;
    }




}

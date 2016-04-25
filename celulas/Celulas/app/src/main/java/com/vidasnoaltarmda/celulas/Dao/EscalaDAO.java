package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Escala;
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
        public ArrayList<Escala> retornaEscalas(Celula celula) throws SQLException {
            ArrayList<Escala> escalas = new ArrayList<>();
            Escala escala = null;
            ResultSet rs = null;
            PreparedStatement statement = null;
            Connection conexao = null;
            conexao = ConnectionManager.getConnection();

        try {

            statement = conexao.prepareStatement(
                    " SELECT id_escala, id_celula, data_celula, hora_celula, local_celula " +
                            "   FROM escala " +
                            "   WHERE id_celula = ?          ");
           /* statement = conexao.prepareStatement(
                    " SELECT id_escalacao, id_escala, membro, tarefa " +
                            "   FROM escalacao " +
                            "   WHERE id_escala = ?          ");*/
            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();

            while (rs.next()) {
                escala = new Escala();
                escala.setId_escala(rs.getInt(1));
                escala.setId_escala(rs.getInt(2));
                escala.setData_celula(Utils.coverteDataApp(rs.getString(3)));
                escala.setHora_celula(Utils.coverteHoraApp(rs.getString(4)));
                escala.setLocal_celula(rs.getString(5));
                escala.setMembro(rs.getString(6));
                escala.setItem_responsavel(rs.getString(7));
                escalas.add(escala);
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
        return escalas;
    }


}

package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Programacao;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 24/03/2016.
 */
//Retorna as programacoes do banco filtrando por celula
public class ProgramacaoDAO {
    public ArrayList<Programacao> retornaProgramacoes(Celula celula) throws SQLException {
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
                    "  WHERE id_celula = ?                                        " +
                    " ORDER BY data_prog desc;                                    ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();

            while (rs.next()) {
                programacao = new Programacao();
                programacao.setId_programacao(rs.getInt(1));
                programacao.setId_celula(rs.getInt(2));
                programacao.setNome(rs.getString(3));
                programacao.setData_prog(Utils.converteDataApp(rs.getString(4)));
                programacao.setHorario(Utils.converteHoraApp(rs.getString(5)));
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

    //metodo que retorna a imagem da programacao passada por parametro e salva no caminho especificado
    //TODO verificar possibilidade de implementacao de um cache de imagens
    public boolean retornaProgramacaoImagem(Programacao programacao, String caminhoArquivo, String nomeArquivo) throws SQLException {
        InputStream isProgramacao = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT imagem            " +
                    "   FROM programacao       " +
                    "  WHERE id_programacao = ? ");

            statement.setInt(1, programacao.getId_programacao());
            rs = statement.executeQuery();

            if (rs.next()) {
                isProgramacao = rs.getBinaryStream(1);
            }

            Utils.downloadImagemBanco(caminhoArquivo, isProgramacao, nomeArquivo);

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

    public boolean insereProgramacao(Programacao programacao, String imagem) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            String sql =       " INSERT INTO programacao (id_celula, nome, data_prog, horario, local_prog, telefone, valor, imagem) " +
                            "values (?,?,?,?,?,?,?,?)";
            statement = conexao.prepareStatement(sql);
            statement.setInt(1, programacao.getId_celula());
            statement.setString(2, programacao.getNome());
            statement.setString(3, programacao.getData_prog());
            statement.setString(4, programacao.getHorario());
            statement.setString(5, programacao.getLocal_prog());
            statement.setString(6, programacao.getTelefone());
            statement.setString(7, programacao.getValor());
            InputStream inputStream = new FileInputStream(new File(imagem));

            statement.setBlob(8, inputStream);

            int row = statement.executeUpdate();
            if (row > 0) {
                System.out.println("O post foi inserido com a imagem.");
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

    public boolean deletaProgramacoes(List<Programacao> programacoes) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean sucesso = false;
        conexao = ConnectionManager.getConnection();
        try {

            String delCommand = " DELETE FROM programacao WHERE id_programacao IN (";
            for (int i = 0; i < programacoes.size() - 1; i++) {
                delCommand = delCommand.concat("?,");
            }
            delCommand = delCommand.concat("?)");

            statement = conexao.prepareStatement(delCommand);

            for (int i = 1; i <= programacoes.size(); i++) {
                statement.setInt(i, programacoes.get(i-1).getId_programacao());
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

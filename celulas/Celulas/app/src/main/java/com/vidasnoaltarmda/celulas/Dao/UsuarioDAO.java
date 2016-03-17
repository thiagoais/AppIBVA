package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by thiago on 06/03/2016.
 */
public class UsuarioDAO {
    private final String TABELA = "usuario";

    public Usuario retornaUsuarioLogin(String login, String senha) throws SQLException{
        Usuario usuario = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            //Garantir no banco que o login será único
            statement = conexao.prepareStatement(
                    "SELECT   id_usuario, id_escala, id_celula, nome    " +
                    "         sobrenome, data_nascimento, login, permissao " +
                    "  FROM usuario                                     " +
                    "  WHERE login = ? and senha = ?                    ");

            //TODO tratar sqlinjection
            statement.setString(1, login);
            statement.setString(2, senha);
            rs = statement.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(0));
                usuario.setNome(rs.getString(3));
                usuario.setSobrenome(rs.getString(4));
                usuario.setDataNascimento(rs.getString(5));
                usuario.setLogin(rs.getString(6));
                usuario.setPermissao(rs.getInt(7));
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
        return usuario;
    }

    public boolean insereUsuario(Usuario usuario) throws SQLException{
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " INSERT INTO usuario (nome, sobrenome, data_nascimento, login, senha, id_celula, permissao) values (?,?,?,?,?,?,?)");
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getSobrenome());
            statement.setString(3, usuario.getDataNascimento());
            statement.setString(4, usuario.getLogin());
            statement.setString(5, usuario.getSenha());
            statement.setInt(6, usuario.getId());
            statement.setInt(7, usuario.getPermissao());

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

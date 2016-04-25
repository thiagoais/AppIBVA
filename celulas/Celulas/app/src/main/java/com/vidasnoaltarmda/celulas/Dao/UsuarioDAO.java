package com.vidasnoaltarmda.celulas.Dao;

import com.vidasnoaltarmda.celulas.Dados.Celula;
import com.vidasnoaltarmda.celulas.Dados.Usuario;
import com.vidasnoaltarmda.celulas.Utils.ConnectionManager;
import com.vidasnoaltarmda.celulas.Utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thiago on 06/03/2016.
 */
public class UsuarioDAO {
    private final String TABELA = "usuario";

    // retorna o usuario correspondente ao login
    public Usuario retornaUsuarioLogin(String login) throws SQLException{
        return retornaUsuarioLogin(login, null);
    }

    // retorna o usuario correspondente aos dados login e senha
    public Usuario retornaUsuarioLogin(String login, String senha) throws SQLException{
        Usuario usuario = null;
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            String stmt =
                    " SELECT  u.id_usuario, u.id_escala, u.id_celula, u.nome,           " +
                    "         u.sobrenome, u.data_nascimento, u.login, u.permissao,     " +
                    "         c.nome, c.lider, c.dia, c.horario, c.local_celula,        " +
                    "         c.dia_jejum, c.periodo, c.versiculo, c.imagem             " +
                    "  FROM usuario u left join celula c on (u.id_celula = c.id_celula) " +
                    "  WHERE login = ?                                                  ";

            if (senha != null) {
                stmt = stmt.concat(" and senha = ?");
            }

            //TODO Garantir no banco que o login será único
            statement = conexao.prepareStatement(stmt);

            //TODO tratar sqlinjection
            statement.setString(1, login);

            if (senha != null)
                statement.setString(2, senha);

            rs = statement.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNome(rs.getString(4));
                usuario.setSobrenome(rs.getString(5));
                usuario.setDataNascimento(rs.getString(6));
                usuario.setLogin(rs.getString(7));
                usuario.setPermissao(rs.getInt(8));

                celula = new Celula();
                celula.setId_celula(rs.getInt(3));
                celula.setNome(rs.getString(9));
                celula.setLider(rs.getString(10));
                celula.setDia(rs.getString(11));
                celula.setHorario(Utils.coverteHoraApp(rs.getString(12)));
                celula.setLocal_celula(rs.getString(13));
                celula.setDia_jejum(rs.getString(14));
                celula.setPeriodo(rs.getString(15));
                celula.setVersiculo(rs.getString(16));
                celula.setImagem(rs.getBlob(17));
                usuario.setCelula(celula);
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

    public ArrayList<Usuario> retornaAniversariantes() throws SQLException{
        Usuario usuario = null;
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT   id_usuario, id_escala, id_celula, nome,      " +
                    "          sobrenome, data_nascimento, login, permissao " +
                    "   FROM usuario                                        " +
                    "   WHERE MONTH( data_nascimento ) = MONTH( NOW( ) )  " +
                            "   ORDER BY data_nascimento                     ");

            rs = statement.executeQuery();

            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNome(rs.getString(4));
                usuario.setSobrenome(rs.getString(5));
                usuario.setDataNascimento(Utils.coverteDataNiver(rs.getString(6)));
                usuario.setLogin(rs.getString(7));
                usuario.setPermissao(rs.getInt(8));
                usuarios.add(usuario);
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
        return usuarios;
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
            statement.setString(3, Utils.coverteDataBanco(usuario.getDataNascimento()));
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

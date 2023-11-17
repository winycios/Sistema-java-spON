package DAO;

import conexao.Conexao;
import conexao.ConexaoMySql;
import conexao.ConexaoServer;
import entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // conexão my sql
    private static final Conexao connectMy = new ConexaoMySql();

    // conexão sql server
    private static final Conexao connectserver = new ConexaoServer();
    private static String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean selectUser(Usuario user) {
        Boolean option = null;
        String sql = String.format("select nome from tbUsuario where cpf = '%s' AND senha = '%s';", user.getCpf(), user.getSenha());

        String nome = "";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
              /* Conexão my sql
                conn = connectMy.criarConexao();*/

            // conexao sql server//
            conn = connectserver.criarConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                nome = rset.getString("nome");
            }

            if (nome == "") {
                option = false;

            } else {
                option = true;
                setNome(nome);
            }

        } catch (
                SQLException ex) {
            ex.printStackTrace();
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }

                if (pstm != null) {
                    pstm.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return option;
    }
}

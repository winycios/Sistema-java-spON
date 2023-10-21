package DAO;

import conexao.Conexao;
import entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

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
            conn = Conexao.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                 nome = rset.getNString("nome");
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

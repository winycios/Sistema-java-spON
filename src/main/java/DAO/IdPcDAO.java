package DAO;

import conexao.Conexao;
import entities.Interface;
import entities.ItensDecoracao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IdPcDAO {

    // Pega o id do pc a partir do apelido fornecido
    public Integer pegarIdPc() {
        Scanner ler = new Scanner(System.in);
        Interface inicio = new Interface();

        //Solicita o apelido do computador
        ItensDecoracao.barra();
        System.out.print("Digite o apelido do computador: ");
        String apelido = ler.nextLine();
        ItensDecoracao.barra();

        // validacao caso o campo venha vazio
        if (apelido.isBlank()) {
            ItensDecoracao.barra();
            System.out.println("Campo nulo !!");
            ItensDecoracao.barra();

            // Envia de volta para as opções
            inicio.Opcoes();
        }

        String sql = String.format("SELECT idComputador FROM tbComputador WHERE apelidoComputador = '%s' AND fk_idEvento = (select e.idEvento from tbEvento e\n" +
                "inner join tbComputador c ON c.fk_idEvento = e.idEvento\n" +
                "where c.apelidoComputador = '%s' AND e.status = \"Em andamento\");", apelido, apelido);

        Integer id = null; // Inicializado como null para indicar que não foi encontrado

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                id = rset.getInt("idComputador");
            }

            if (id == null) {

                // Se id não foi encontrado, exibe a mensagem para o usuário
                ItensDecoracao.barra();
                System.out.println("Apelido inexistente");
                ItensDecoracao.barra();
            } else {
                ItensDecoracao.barra();
                System.out.println("Computador localizado");
                ItensDecoracao.barra();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
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
        return id;
    }
}

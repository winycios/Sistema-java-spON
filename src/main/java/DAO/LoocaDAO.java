package DAO;

import conexao.Conexao;
import entities.DadosLooca;
import entities.ItensDecoracao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class LoocaDAO {

    public Boolean InsertDados(DadosLooca dados) {
        IdPcDAO pc = new IdPcDAO();
        Integer id = pc.pegarIdPc();
        LocalDateTime hora = LocalDateTime.now();

        String sql = String.format("insert into tbMonitoramento (dataHora, fk_idComputador,cpuTemp,cpuFreq,redeLatencia, disco, ram)\n" +
                "VALUES ('%s', %d, %.0f, %.0f, %d, %.0f, %.0f);", hora, id, dados.getTemperatura(), dados.getUso(), dados.latenciaRede(), dados.espacoDisco(), dados.porcentualRam());

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (id == null) {

                return false;
            } else {
                conn = Conexao.createConnectionToMySQL();
                pstm = conn.prepareStatement(sql);
                int rset = pstm.executeUpdate();

                ItensDecoracao.barra();
                System.out.println("Dados enviados com sucesso !!");
                ItensDecoracao.barra();
            }
        } catch (Exception ex) {
            // Tratamento de exceção genérica
            ex.printStackTrace();
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }
}
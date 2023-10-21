package DAO;

import conexao.Conexao;
import entities.Chamado;
import entities.Interface;
import entities.ItensDecoracao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChamadosDAO {
    private static Integer idPc;

    public Integer getIdPc() {
        return idPc;
    }

    // Faz uma inserção na table chamados
    public void InsertChamado(Chamado chamado) {
        IdPcDAO pc = new IdPcDAO();
        Integer id = pc.pegarIdPc();

        if (id == null) {
            Interface inicio = new Interface();
            inicio.Opcoes();
        }else {
            idPc = id;
        }

        String sql = String.format("INSERT INTO tbOcorrencia (descricao, fk_idComputador, hora, status)\n" +
                "VALUES ('%s', %d, '%s', 'Pendente');", chamado.getProblema(), idPc, chamado.getHora());
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (procurarChamado() == null) {
                conn = Conexao.createConnectionToMySQL();
                pstm = conn.prepareStatement(sql);
                StatusPc();

                int rset = pstm.executeUpdate();

                ItensDecoracao.barra();
                System.out.println("Chamado feito com sucesso !");
                ItensDecoracao.barra();

            } else {
                ItensDecoracao.barra();
                System.out.println("Já tem um chamado em andamento !");
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
    }

    // Verifica se já tem chamados encaminhados para determinado computador
    public Integer procurarChamado() {

        String sql = String.format("select idComputador from tbComputador where idComputador = %d AND status != \"bom\";", getIdPc());

        Integer id = null;

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

    // atualiza o status do pc quando um chamado é feito
    public void StatusPc() {

        String sql = String.format("update tbComputador set status = 'bug' where idComputador = %d;", getIdPc());
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.createConnectionToMySQL();
            pstm = conn.prepareStatement(sql);

            int rset = pstm.executeUpdate();

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
    }
}


package DAO;

import conexao.Conexao;
import conexao.ConexaoMySql;
import conexao.ConexaoServer;
import entities.Chamado;
import entities.Interface;
import entities.ItensDecoracao;
import integracao.Slack;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChamadosDAO {

    // conexão my sql
    private static final Conexao connectMy = new ConexaoMySql();

    // conexão sql server
    private static final Conexao connectserver = new ConexaoServer();
    private static Integer idPc;

    public Integer getIdPc() {
        return idPc;
    }

    // Faz uma inserção na table chamados
    public void InsertChamado(Chamado chamado) {
        IdPcDAO pc = new IdPcDAO();
        Integer id = pc.pegarIdPc();
        JSONObject json = new JSONObject();
        String textoAlerta;

        if (id == null) {
            Interface inicio = new Interface();
            inicio.Opcoes();
        } else {
            idPc = id;
        }

        String sql = String.format("INSERT INTO tbOcorrencia (descricao, fk_idComputador, hora, status)\n" +
                "VALUES ('%s', %d, '%s', 'Pendente');", chamado.getProblema(), idPc, chamado.getHora());
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (procurarChamado() == null) {
                // conexao sql server//
                conn = connectserver.criarConexao();

                pstm = conn.prepareStatement(sql);

                // atualiza o status do pc
                StatusPc();
                // gera a contigencia de dados
                InsertChamadoMySql(chamado);

                // formatação de data
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                // Pega a data e hora atual
                Date data = new Date();

                textoAlerta = String.format("""
                        Foi aberto um chamado!
                        Descrição do problema: %s,
                        Pc com problema: %s,
                        Hora da abertura do chamado: %s
                        """, chamado.getProblema(), nomePc(), formato.format(data));

                json.put("text", textoAlerta);
                Slack.sendMessage(json);
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

        String sql = String.format("select idComputador from tbComputador where idComputador = %d AND status != 'bom';", getIdPc());

        Integer id = null;

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
              /* Conexão my sql
                conn = connectMy.criarConexao();*/

            // conexao sql server//
            conn = connectserver.criarConexao();

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

    public String nomePc() {

        String sql = String.format("select apelidoComputador from tbComputador where idComputador = %d;", getIdPc());
        Connection conn = null;
        PreparedStatement pstm = null;
        String apelido = "";
        ResultSet rset = null;

        try {
               /* Conexão my sql
                conn = connectMy.criarConexao();*/

            // conexao sql server//
            conn = connectserver.criarConexao();

            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();


            while (rset.next()) {
                apelido = rset.getString("apelidoComputador");
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

        return apelido;
    }


    // gerar contigencia de dados. Inserção no my sql
    public void InsertChamadoMySql(Chamado chamado) {
        String sql = String.format("INSERT INTO tbOcorrencia (descricao, apelidoComputador, hora)\n" +
                "VALUES ('%s', '%s', '%s');", chamado.getProblema(), nomePc(), chamado.getHora());
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // conexao sql server//
            conn = connectMy.criarConexao();

            pstm = conn.prepareStatement(sql);

            pstm.executeUpdate();

            ItensDecoracao.barra();
            System.out.println("Chamado enviado para o banco local!");
            ItensDecoracao.barra();

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


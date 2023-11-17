package DAO;

import conexao.Conexao;
import conexao.ConexaoMySql;
import conexao.ConexaoServer;
import entities.DadosLooca;
import entities.ItensDecoracao;
import entities.util.HardwareType;
import entities.util.LogLevel;
import entities.util.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class LoocaDAO {

    public static final LogManager logManager = new LogManager();

    // conexão my sql
    private static final Conexao connectMy = new ConexaoMySql();

    // conexão sql server
    private static final Conexao connectserver = new ConexaoServer();

    public Boolean InsertDados(DadosLooca dados) {
        //Valida para saber qual componente está acima do normal
        verificarComponentesLog(dados);

        // pega o id do pc que o usuario quer fazer a inserção
        IdPcDAO pc = new IdPcDAO();
        Integer id = pc.pegarIdPc();


        // Insere os dados no my sql
        InsertDadosMySql(dados, id);


        LocalDateTime hora = LocalDateTime.now();
        Integer num = ThreadLocalRandom.current().nextInt(4, 9);

        String sql = String.format("insert into tbMonitoramento (dataHora, fk_idComputador,cpuTemp,gpuTemp, cpuFreq,redeLatencia, disco, ram)\n" +
                "VALUES ('%s', %d, %.0f, %.0f, %.0f, %d, %.0f, %.0f);", hora, id, dados.getTemperatura(), (dados.getTemperatura() + num), dados.getUso(), dados.latenciaRede(), dados.atividadeDisco(), dados.porcentualRam());

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (id == null) {
                return false;

            } else {
                // salva a informação no log
                logManager.salvarLog(nomePc(id));

                // conexao sql server//
                conn = connectserver.criarConexao();

                pstm = conn.prepareStatement(sql);
                pstm.executeUpdate();

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

    public void inserirMassaDados() {

        IdPcDAO pc = new IdPcDAO();
        Integer id = pc.pegarIdPc();

        DadosLooca dados = new DadosLooca();

        if (id == null) {
            ItensDecoracao.barra();
            System.out.println("Sistema finalizado");
            exit(0);;
        } else {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    massaDados(dados, id);
                    //Valida para saber qual componente está acima do normal
                    verificarComponentesLog(dados);
                    // salva a informação no log
                    logManager.salvarLog(nomePc(id));
                }
            }, 0, 5000);
        }
    }


    public Boolean massaDados(DadosLooca dados, Integer id) {
        LocalDateTime hora = LocalDateTime.now();
        Integer num = ThreadLocalRandom.current().nextInt(4, 9);

        String sql = String.format("insert into tbMonitoramento (dataHora, fk_idComputador,cpuTemp,gpuTemp, cpuFreq,redeLatencia, disco, ram)\n" +
                "VALUES ('%s', %d, %.0f, %.0f, %.0f, %d, %.0f, %.0f);", hora, id, dados.getTemperatura(), (dados.getTemperatura() + num), dados.getUso(), dados.latenciaRede(), dados.atividadeDisco(), dados.porcentualRam());

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (id == null) {
              return false;
            } else {
                // conexao sql server//
                conn = connectserver.criarConexao();

                pstm = conn.prepareStatement(sql);
                pstm.executeUpdate();

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


    // Verificação dos componentes
    public void verificarComponentesLog(DadosLooca dados) {
        Integer lat = dados.latenciaRede();
        Double ram = dados.porcentualRam();
        Double disco = dados.atividadeDisco();
        Double frequencia = dados.getUso();
        Double temperatura = dados.getTemperatura();


        if ((lat > 30)) {
            logManager.setLog("Latência da rede acima do ideal", LogLevel.AVISO, HardwareType.REDE, lat.toString());
        } else if (lat > 50) {
            logManager.setLog("limite de latência da rede alcançado", LogLevel.PERIGO, HardwareType.REDE, lat.toString());
        }

        if (ram > 70) {
            logManager.setLog("uso de RAM fora do ideal", LogLevel.AVISO, HardwareType.RAM, String.format("%.0f", ram));
        } else if (ram > 90) {
            logManager.setLog("uso de RAM muito acima do ideal", LogLevel.PERIGO, HardwareType.RAM, String.format("%.0f", ram));
        }

        if (disco > 50) {
            logManager.setLog("uso do DISCO fora do ideal", LogLevel.AVISO, HardwareType.DISCO, String.format("%.0f", disco));

        } else if (disco > 80) {
            logManager.setLog("uso do DISCO muito acima do ideal", LogLevel.PERIGO, HardwareType.DISCO, String.format("%.0f", disco));
        }

        if (temperatura > 40) {
            logManager.setLog("Temperatura da CPU anormal", LogLevel.AVISO, HardwareType.CPU, String.format("%.0f", temperatura));

        } else if (temperatura > 80) {
            logManager.setLog("Temperatura da CPU muito acima do ideal", LogLevel.PERIGO, HardwareType.CPU, String.format("%.0f", temperatura));
        }

        if (frequencia > 30) {
            logManager.setLog("Frequência da CPU anormal", LogLevel.AVISO, HardwareType.CPU, String.format("%.0f", frequencia));
        } else if (frequencia > 70) {
            logManager.setLog("Frequência da CPU muito acima do ideal", LogLevel.PERIGO, HardwareType.CPU, String.format("%.0f", frequencia));
        }
    }


    // pegar o apelido do computador a partir do ID
    public String nomePc(Integer id) {

        String sql = String.format("select apelidoComputador from tbComputador where idComputador = %d;", id);
        Connection conn = null;
        PreparedStatement pstm = null;
        String apelido = "";
        ResultSet rset = null;

        try {
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


    // Criar a contigencia de dados. Mandando os arquivos para o mysql
    public Boolean InsertDadosMySql(DadosLooca dados, Integer id) {

        LocalDateTime hora = LocalDateTime.now();
        Integer num = ThreadLocalRandom.current().nextInt(4, 9);

        String sql = String.format("insert into tbMonitoramento (dataHora, apelidoComputador,cpuTemp,gpuTemp, cpuFreq,redeLatencia, disco, ram)\n" +
                "VALUES ('%s', '%s', %.0f, %.0f, %.0f, %d, %.0f, %.0f);", hora, nomePc(id), dados.getTemperatura(), (dados.getTemperatura() + num), dados.getUso(), dados.latenciaRede(), dados.atividadeDisco(), dados.porcentualRam());

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            if (id == null) {
                return false;

            } else {
                // conexao sql server//
                conn = connectMy.criarConexao();

                pstm = conn.prepareStatement(sql);
                pstm.executeUpdate();

                ItensDecoracao.barra();
                System.out.println("Dados enviados para o mySql com sucesso !!");
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

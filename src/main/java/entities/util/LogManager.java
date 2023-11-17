package entities.util;

import entities.ItensDecoracao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private Log log;
    private List<String> dadosLog = new ArrayList<>();

    private static Boolean apelidoEscrito = true;

    public LogManager() {
    }

    public void setLog(String mensagem, LogLevel logLevel, HardwareType hardwareType, String porcentagem) {

        log = new Log();
        log.setMensagem(mensagem);
        log.setLogLevel(logLevel);
        log.setHardwareType(hardwareType);
        log.setPorcentagem(porcentagem);


        try {
            dadosLog.add(log.toString());

        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    public void salvarLog(String apelido) {


        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logs.txt", true))) {
            Thread.sleep(3000);
            if (apelidoEscrito) {
                bw.write("Máquina: " + apelido);
                bw.newLine();
                apelidoEscrito = false;
            }


            // inicia o primeiro laço
            for (int i = 0; i < dadosLog.size(); i++) {

                // guarda o valor atual do laço em uma variavel
                String valor = dadosLog.get(i);
                boolean escrever = true;
                //Inicia um segundo contador com uma casa a frente do primeiro contador atual
                for (int j = i + 1; j < dadosLog.size(); j++) {

                    // Verifica se as tres ultimas palavras da string valor é igual as tres ultimas strings do log atual do segundo contador
                    // Caso seja iguais esse log não será escrito.
                    if (valor.substring(valor.length() - 3).equals(dadosLog.get(j).substring(dadosLog.get(j).length() - 3))) {
                        escrever = false;
                        break;
                    }
                }

                if (escrever) {
                    bw.write(valor);
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.write("----------------------------------------");
            bw.newLine();
            dadosLog.clear();

            ItensDecoracao.barra();
            System.out.println("Componentes críticos armazenado no log");
            ItensDecoracao.barra();
        } catch (IOException | InterruptedException e) {
            System.out.print("Erro: " + e.getMessage());
        }
    }
}

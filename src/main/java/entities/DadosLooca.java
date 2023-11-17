package entities;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import entities.util.Converter;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


public class DadosLooca {

    Looca looca = new Looca();
    File disc = new File("C:");
    Temperatura temperatura = new Temperatura();
    DiscoGrupo disco = new DiscoGrupo();


    // processador
    public Double getUso() {
        return looca.getProcessador().getUso();
    }

    // processador
    public Double getTemperatura() {
        return temperatura.getTemperatura();
    }

    // ram
    public Double porcentualRam() {
        Double porcentual = ((Converter.formater(looca.getMemoria().getEmUso()) / Converter.formater(looca.getMemoria().getTotal())) * 100);

        return porcentual;
    }

    // disco
    public Double atividadeDisco() {
        // Obtém informações do sistema
        SystemInfo systemInfo = new SystemInfo();

        try {
            HardwareAbstractionLayer hardware = systemInfo.getHardware();
            HWDiskStore disk = hardware.getDiskStores().get(0); // Pega o primeiro disco, você pode adaptar conforme necessário

            // Registra o tempo inicial
            long initialTime = System.currentTimeMillis();
            long initialTransferTime = disk.getTransferTime();

            Thread.sleep(5000); // Aguarda 5 segundos(Tirar essa parte quebra o resultado)

            // Atualiza os atributos do disco para obter os dados mais recentes
            disk.updateAttributes();

            // Registra o tempo final
            long finalTime = System.currentTimeMillis();
            long finalTransferTime = disk.getTransferTime();

            // Calcula a porcentagem de tempo de atividade
            double activeTime = finalTransferTime - initialTransferTime;
            double totalTime = finalTime - initialTime;

            double activityPercentage = (activeTime / totalTime) * 100;

            return activityPercentage;
        } catch (Exception e) {
            System.out.println("Erro ao pegar o tempo de atividade do disco: " + e);
            return null;
        }
    }

    // rede
    public Integer latenciaRede() {
        //Informações da Rede
        String host = "www.google.com"; // Você pode substituir pelo host desejado
        Integer latencia = 0;

        try {
            long tempoInicio = System.currentTimeMillis();
            InetAddress inetAddress = InetAddress.getByName(host);
            boolean isReachable = inetAddress.isReachable(1000);

            if (isReachable) {
                long tempoFim = System.currentTimeMillis();
                long lat = tempoFim - tempoInicio;
                latencia = Math.toIntExact(lat);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return latencia;
    }

    @Override
    public String toString() {

        return String.format("""
                Dados do seu computador
                Uso do Processador = %.0f%%
                Temperatura do processador = %.0f
                Porcentual da ram = %.0f%%
                Uso do disco = %.0f%%
                Latencia = %d MS
                """, getUso(), getTemperatura(), porcentualRam(), atividadeDisco(), latenciaRede());
    }
}
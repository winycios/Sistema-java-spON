package entities;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import entities.util.Converter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;


public class DadosLooca {

    Looca looca = new Looca();
    File disc = new File("C:");
    Temperatura temperatura = new Temperatura();
    DiscoGrupo disco = new DiscoGrupo();


    public Double getUso() {
        return looca.getProcessador().getUso();
    }

    public Double getTemperatura() {
        return temperatura.getTemperatura();
    }

    public Double porcentualRam() {
        Double porcentual = ((Converter.formater(looca.getMemoria().getEmUso()) / Converter.formater(looca.getMemoria().getTotal())) * 100);

        return porcentual;
    }

    public Double tamanhoDisco() {

        return Converter.formater(disco.getTamanhoTotal());
    }

    public Double espacoDisco() {

        return Converter.formater(disc.getFreeSpace());
    }

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
                Tamanho do disco = %.0f GB
                Espaço do disco = %.0f
                Latencia = %d MS
                """, getUso(),getTemperatura(), porcentualRam(), tamanhoDisco(), espacoDisco(), latenciaRede());
    }
}